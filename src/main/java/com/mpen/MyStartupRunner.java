package com.mpen;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.mp.shared.CompatUtils;
import com.mp.shared.common.BookInfo;
import com.mp.shared.common.BookInfo.BookInfoExpansion;
import com.mp.shared.common.ResourceVersion;
import com.mpen.api.bean.Hot;
import com.mpen.api.bean.NoClassBookSource;
import com.mpen.api.bean.Program;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Constants.CacheType;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbResourceBook;
import com.mpen.api.domain.DdbResourceCode;
import com.mpen.api.domain.OralTestDetail;
import com.mpen.api.mapper.OralTestDetailMapper;
import com.mpen.api.mapper.PeCustomMapper;
import com.mpen.api.mapper.ResourceBookMapper;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.service.RecordUserBookService;
import com.mpen.api.service.ResourceBookService;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.FileUtils;
import com.mpen.api.util.Impl;
import com.mpen.api.util.qcloud.SmsUtil;
import com.mpen.api.util.qcloud.sms.SmsSingleSenderResult;

/**
 * TODO 启动加载自定义属性.
 * 
 * @author zyt
 *
 */
@Component
@ImportResource(locations = { "classpath:application-bean.xml" })
public final class MyStartupRunner implements CommandLineRunner {
    @Value("${web.upload-path}")
    private String rootPath;
    @Value("${web.domain}")
    private String domain;
    @Value("${web.cdn-domain}")
    private String cdnDomain;
    @Value("${web.hosts}")
    private String hosts;
    @Value("${web.num-oraleval-shards}")
    private int numOralEvalShards;
    @Value("${conf-environment}")
    private String confEnvironment;
    @Autowired
    private ResourceBookService resourceBookService;
    @Autowired
    private ResourceBookMapper resourceBookMapper;
    @Autowired
    private PeCustomMapper peCustomMapper;
    @Autowired
    private RecordUserBookService recordUserBookService;
    @Autowired
    private MemCacheService memCacheService;
    @Autowired
    private OralTestDetailMapper oralTestDetailMapper;
    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);
    @Override
    public void run(String... arg0) throws Exception {
        if("dev".equals(confEnvironment) || "local_test".equals(confEnvironment)) {
            return;
        }
        init();
        final List<DdbResourceBook> validBooks = resourceBookMapper.getValidBooks();
        final List<DdbPeCustom> list = peCustomMapper.get();
        // 初始化本地保存bookList
        resourceBookService.updateLocalCache(CacheType.CACHE_BOOK, null);
        // 初始化获取文件本地地址方法
        BookInfo.expansion = new BookInfoExpansion() {
            @Override
            public void setLocalPath(BookInfo bookInfo, String localPath) {
            }

            @Override
            public String getLocalPath(BookInfo bookInfo) {
                try {
                    final DdbResourceBook book = resourceBookService.getById(bookInfo.id);
                    if (book != null) {
                        return FileUtils.getFileSaveRealPath(book.getBookLink(), false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

			@Override
			public ResourceVersion getLocalVersion(BookInfo bookInfo) {
				// for server side, we can assume bookInfo.version is the local version
				// for pen, it's not always true
				if (bookInfo != null) {
					return bookInfo.version;
				}
				return null;
			}
        };
        // 初始化获取本地书籍码值信息
        DdbResourceCode.initBookCodeDetail();
        // 初始化无老师班级vedio书籍资源
        NoClassBookSource.initVedioBookDetail();
        // 启动加载用户信息
        Constants.CACHE_THREAD_POOL.execute(() -> {
            validBooks.forEach((book) -> {
                try {
                    resourceBookService.getBookContent(book.getId(), Constants.CACHE_STUDY_PREFIX);
                    resourceBookService.getBookContent(book.getId(), Constants.CACHE_SPOKEN_PREFIX);
                    resourceBookService.updateLocalCache(CacheType.CACHE_PAGE, book.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // 初始化本地保存全部书pageInfoList
            resourceBookService.updateLocalCache(CacheType.CACHE_GLOBAL_PAGE, null);
            list.forEach((custom) -> {
                try {
                    recordUserBookService.getUserStydyMap(custom.getLoginId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        //初始化口语考试卷
        recordUserBookService.initOralTest();
    }

    /**
     * 初始化全局变量
     */
    private void init() throws Exception {
        FileUtils.localIp = InetAddress.getLocalHost().toString();
        FileUtils.root = rootPath;
        FileUtils.domain = domain;
        FileUtils.cdnDomain = cdnDomain;
        // TODO 启动加载小程序资源信息,以后Hot，Program这些信息应该从 数据库或者文件资源里面读出来
        Hot.init();
        Program.init();
        // TODO 启动加载so
        System.load(ResourceUtils.getURL(Constants.SSL_SO_PATH).getPath());
        System.load(ResourceUtils.getURL(Constants.SO_PATH).getPath());
        CompatUtils.impl = new Impl();
        FileUtils.numOralEvalShards = numOralEvalShards;
        if (StringUtils.isNotBlank(hosts)) {
            final String[] hostArray = hosts.split(",");
            if (hostArray.length <= 0) {
                return;
            }
            for (int i = 0; i < hostArray.length; i++) {
                if (CommUtil.checkLocalIp(hostArray[i])) {
                    FileUtils.localIp = hostArray[i];
                    FileUtils.shardNum = i;
                    break;
                }
            }
        }
        if (FileUtils.shardNum < numOralEvalShards && FileUtils.shardNum >= 0) {
            smsSchDuler();
            //TODO 现驰声与云知声同时评测笔端上传来的数据，检验驰声与云知声评测的准确性与稳定性
            Constants.CACHE_THREAD_POOL.execute(() -> {
                oralTestTask(Constants.AIENGINE_YZS);
            });
            Constants.CACHE_THREAD_POOL.execute(() -> {
            	oralTestTask(Constants.AIENGINE_CHIVOX);
            });
        }
    }

    /**
     * 发送异常报警短信定时任务。
     */
    private void smsSchDuler() {
        final MemCacheService memCacheServiceTemp = memCacheService;
        final String key = CommUtil.getCacheKey(Constants.CACHE_SEND_SMS_KEY);
        // 由于发送短信有频率限制，每次间隔30S轮询发送短信任务
        SCHEDULER.scheduleWithFixedDelay(() -> {
            try {
                List<ArrayList<String>> smsList = memCacheServiceTemp.get(key);
                if (smsList != null && smsList.size() > 0) {
                    SmsSingleSenderResult result = SmsUtil.sendSmsError(smsList.get(0));
                    if (result != null && result.result == 0) {
                        // 短信发送成功后移除
                        smsList.remove(0);
                        memCacheServiceTemp.set(key, smsList, Constants.DEFAULT_CACHE_EXPIRATION);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, 0, 30, TimeUnit.SECONDS);
    }

    /**
     * 口语考试评测任务。
     * 
     */
    private void oralTestTask(int aiengine) {
        while (true) {
            final List<OralTestDetail> details = oralTestDetailMapper.getNotDeal(FileUtils.shardNum,aiengine);
            if (details == null || details.size() <= 0) {
                try {
                    // TODO 演示需要即时，这里采用20s间隔
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            for (OralTestDetail detail : details) {
                
                recordUserBookService.oralEvaluate(detail);
            }
           
        }
    }
}
