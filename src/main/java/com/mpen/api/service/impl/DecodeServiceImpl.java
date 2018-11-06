/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mp.shared.common.BookInfo;
import com.mp.shared.common.Code;
import com.mp.shared.common.CodeInfo;
import com.mp.shared.common.ExtraBookInfo;
import com.mp.shared.common.HotArea;
import com.mp.shared.common.LearnWordStructureInfo;
import com.mp.shared.common.MpCode;
import com.mp.shared.common.PageInfo;
import com.mp.shared.common.QuickCodeInfo;
import com.mp.shared.common.ShCode;
import com.mp.shared.service.MpResourceDecoder;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbResourceVideo;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.ResourceVideoMapper;
import com.mpen.api.service.DecodeService;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.service.ResourceBookService;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.FileUtils;
import com.mpen.api.util.qcloud.SmsUtil;

/**
 * 解码相关服务.
 *
 * @author zyt
 *
 */
@Component
public class DecodeServiceImpl implements DecodeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DecodeServiceImpl.class);
    @Autowired
    private ResourceBookService resourceBookService;
    @Autowired
    private ResourceVideoMapper resourceVideoMapper;
    @Autowired
    private MemCacheService memCacheService;

    private CodeInfo getCodeInfoInternal(QuickCodeInfo quickCodeInfoParam) throws Exception {
        CodeInfo codeInfo = null;
        // TODO 临时构建的quickCodeInfo参数，以后会去掉
        final QuickCodeInfo quickCodeInfo = new QuickCodeInfo();
        final boolean inputHasMpPoint = quickCodeInfoParam.hasMpPoint();
        quickCodeInfo.code = quickCodeInfoParam.code;
        quickCodeInfo.language = quickCodeInfoParam.language;
        quickCodeInfo.setFunctionCode(quickCodeInfoParam.getFunctionCode());
        String key = "";
        switch (quickCodeInfo.code.type) {
        case SH:
        case OID3: {
            final BookInfo bookInfo = quickCodeInfoParam.getBookInfo();
            // 校验笔端点读码为SH,OID3时,bookInfo和curBookInfo都为null时(第一次点击不是封面),返回null;目前是笔端要做校验:如果不是封面的话,会提示一段语音
            if (bookInfo == null) {
                throw new SdkException(Constants.NO_BOOKINFO_EVER);
            }
            if (quickCodeInfoParam.code == null || quickCodeInfoParam.code.shCode == null) {
                throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
            }
            key = CommUtil.getCacheKey(
                Constants.CACHE_CODEINFO_KEY_PRIFIX + bookInfo.id + "_" + quickCodeInfoParam.getFunctionCode() + "_"
                    + quickCodeInfoParam.code.shCode.code + "_" + quickCodeInfoParam.language);
            try {
                codeInfo = memCacheService.get(key);
            } catch (CacheException e) {
                e.printStackTrace();
            }
            quickCodeInfo.bookInfo = resourceBookService.getBookInfo(bookInfo.id);
            break;
        }
        case POSITION: {
            final BookInfo bookInfo = quickCodeInfoParam.getBookInfo();
            if (bookInfo == null || quickCodeInfoParam.code == null || quickCodeInfoParam.code.mpPoint == null) {
                throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
            }
            quickCodeInfo.bookInfo = resourceBookService.getBookInfo(bookInfo.id);
            break;
        }
        case MP:
            getQuickCodeInfo(quickCodeInfo);
            break;
        default:
            throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
        }
        if (quickCodeInfo.bookInfo == null) {
            throw new SdkException(Constants.NO_MACHING_BOOK);
        }
        // 校验音频文件是否存在
        try {
            if (codeInfo != null && codeInfo.languageInfos != null) {
                outer: for (HotArea.LanguageInfo languageInfo : codeInfo.languageInfos) {
                    if (languageInfo.soundUrls != null) {
                        for (String url : languageInfo.soundUrls) {
                            if (!new File(FileUtils.getFileSaveRealPath(url, false)).exists()) {
                                codeInfo = null;
                                break outer;
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            codeInfo = null;
        }
        if (codeInfo == null || codeInfo.bookInfo == null
            || !codeInfo.bookInfo.version.toString().equals(quickCodeInfo.bookInfo.version.toString())) {
            final String filePath = BookInfo.expansion.getLocalPath(quickCodeInfo.bookInfo);
            if (!new File(filePath).exists()) {
                throw new SdkException(
                    Constants.NO_MACHING_ERROR_MSG + "-ip:" + InetAddress.getLocalHost() + "-filePath:" + filePath);
            }
            final String fileSavePath = FileUtils.getTempFileSavePath(quickCodeInfo.bookInfo,
                quickCodeInfo.code.type + "");
            codeInfo = MpResourceDecoder.decode(quickCodeInfo, true, filePath, fileSavePath, Constants.URL_GENERATOR,
                true);
            if (codeInfo == null) {
                return null;
            }
            // 判断是否是视频资源.
            if (codeInfo.isVideo()) {
                getVideo(codeInfo);
            }

            codeInfo.bookInfo = quickCodeInfo.bookInfo;

            if (quickCodeInfoParam.pageInfo == null || !quickCodeInfo.pageInfo.id.equals(quickCodeInfoParam.pageInfo.id)
                || quickCodeInfo.pageInfo.version.compareTo(quickCodeInfoParam.pageInfo.version) != 0) {
                codeInfo.pageInfo = quickCodeInfo.pageInfo;
            }
            // TODO 判断此书是否为单词学习
            if (LearnWordStructureInfo.isLearnWord(quickCodeInfo.bookInfo)) {
                final LearnWordStructureInfo structureInfo = resourceBookService
                    .getStructureInfo(quickCodeInfo.bookInfo.id);
                if (structureInfo == null) {
                    throw new SdkException(Constants.NO_MACHING_ERROR_MSG);
                }
                if (quickCodeInfoParam.extraBookInfo == null
                    || quickCodeInfoParam.extraBookInfo.learnWordStructureInfo == null
                    || !structureInfo.bookId.equals(quickCodeInfoParam.extraBookInfo.learnWordStructureInfo.bookId)
                    || structureInfo.version
                        .compareTo(quickCodeInfoParam.extraBookInfo.learnWordStructureInfo.version) != 0) {
                    codeInfo.extraBookInfo = new ExtraBookInfo();
                    codeInfo.extraBookInfo.learnWordStructureInfo = structureInfo;
                }
            }
            // only send code.mpPoint back in quickCodeInfo if needed
            QuickCodeInfo sendBackInfo = null;
            if (codeInfo.quickCodeInfo.hasMpPoint() && !inputHasMpPoint) {
                sendBackInfo = new QuickCodeInfo();
                sendBackInfo.code = codeInfo.quickCodeInfo.code;
            }
            if (codeInfo.quickCodeInfo.language != quickCodeInfoParam.language) {
                if (sendBackInfo == null) {
                    sendBackInfo = new QuickCodeInfo();
                }
                sendBackInfo.language = codeInfo.quickCodeInfo.language;
            }
            codeInfo.quickCodeInfo = sendBackInfo;
            // TODO 暂时只做sh码缓存,由于cdn有效期为七天，所以缓存也设为四小时
            switch (quickCodeInfo.code.type) {
            case SH:
            case OID3:
                try {
                    memCacheService.set(key, codeInfo, Constants.FOUR_HOUR_CACHE_EXPIRATION);
                } catch (CacheException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
            }
        }
        return codeInfo;
    }

    @Override
    public CodeInfo getCodeInfo(QuickCodeInfo quickCodeInfoParam) throws Exception {
        CodeInfo codeInfo = null;
        try {
            codeInfo = getCodeInfoInternal(quickCodeInfoParam);
        } catch (SdkException e) {
            // TODO: 之后加一个log收集错误日志包括(penId，userId，错误代码，错误描述等),提供数据pipline收集使用.
            LOGGER.error("云点读接收数据异常: " + e.getMessage());
            if (!e.getMessage().equals(Constants.NO_BOOKINFO_EVER)) {
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 云点读出现未知异常，发送异常报警，短信字数限制，削减参数保留核心信息。
            // 短信服务使用腾讯云，github地址：https://github.com/qcloudsms/qcloudsms
            final QuickCodeInfo sendParam = new QuickCodeInfo();
            sendParam.code = quickCodeInfoParam.code;
            sendParam.bookInfo = new BookInfo();
            sendParam.bookInfo.id = quickCodeInfoParam.getBookInfo().id;
            sendParam.language = quickCodeInfoParam.language;
            sendParam.setFunctionCode(quickCodeInfoParam.getFunctionCode());
            // 将参数保存如发送短息列表中等待队列发送
            final ArrayList<String> params = new ArrayList<>();
            params.add(Constants.GSON.toJson(sendParam));
            params.add(e.getMessage());
            SmsUtil.addToSmsQueue(params, memCacheService);
            throw e;
        }
        return codeInfo;
    }

    private void getQuickCodeInfo(QuickCodeInfo quickCodeInfo) throws Exception {
        final PageInfo pageInfo = resourceBookService.getPageInfoByMpCode(quickCodeInfo.code.mpCode);
        if (pageInfo != null) {
            final BookInfo bookInfo = resourceBookService.getBookInfo(pageInfo.bookId);
            if (bookInfo != null) {
                quickCodeInfo.bookInfo = bookInfo;
                quickCodeInfo.pageInfo = pageInfo;
            }
        }
    }

    @Override
    public boolean getVideo(CodeInfo codeInfo) {
        try {
            for (ShCode code : codeInfo.hotAreaCodes.shCodes) {
                final List<DdbResourceVideo> videos = resourceVideoMapper.getUrl(code.code, codeInfo.getBookInfo().id);
                if (videos != null && videos.size() > 0) {
                    final int size = videos.size();
                    codeInfo.languageInfos[0].videos = new HotArea.VideoInfo[size];
                    for (int i = 0; i < size; i++) {
                        codeInfo.languageInfos[0].videos[i] = new HotArea.VideoInfo();
                        final DdbResourceVideo video = videos.get(i);
                        codeInfo.languageInfos[0].videos[i].path = video.getUrl();
                        codeInfo.languageInfos[0].videos[i].type = video.getType();
                        codeInfo.languageInfos[0].videos[i].typeName = video.getType().getName();
                        if (video.getType() == HotArea.VideoType.JCDH) {
                            codeInfo.languageInfos[0].videoInfo = video.getUrl();
                        }
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return false;
    }
   
    /**
     * 
     * app端获取口语考试CodeInfo信息,主要获取Exam.json文件数据
     * 
     */
   @Override
   public CodeInfo getOralTestCodeInfo(String bookId, int pageNum) throws Exception {
       try {
           BookInfo  bookInfo=new BookInfo();
           bookInfo.id=bookId;
           QuickCodeInfo quickCodeInfo = MpResourceDecoder.getOralTestQuickCodeInfo(bookInfo,pageNum);
           // 获取bookinfo信息
           quickCodeInfo.bookInfo = resourceBookService.getBookInfo(quickCodeInfo.getBookInfo().id);
           // 获取文件路径
           final String filePath = BookInfo.expansion.getLocalPath(quickCodeInfo.bookInfo);
           if (!new File(filePath).exists()) {
               throw new SdkException(
                       Constants.NO_MACHING_ERROR_MSG + "-ip:" + InetAddress.getLocalHost() + "-filePath:" + filePath);
           }
           // 获取文件临时路径
           final String fileSavePath = FileUtils.getTempFileSavePath(quickCodeInfo.bookInfo,
                   quickCodeInfo.code.type + "");
           final CodeInfo codeInfo = MpResourceDecoder.decode(quickCodeInfo, true, filePath, fileSavePath, Constants.URL_GENERATOR,
                   false);
           return codeInfo;
       } catch (Exception e) {
           throw e;
       }
   }
}
