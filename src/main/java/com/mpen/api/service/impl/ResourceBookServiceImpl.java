/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import java.io.File;
import java.lang.reflect.Field;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mp.shared.common.BookInfo;
import com.mp.shared.common.Code;
import com.mp.shared.common.CodeInfo;
import com.mp.shared.common.Exam;
import com.mp.shared.common.Exam.Topic;
import com.mp.shared.common.FileModule;
import com.mp.shared.common.FullBookInfo;
import com.mp.shared.common.LearnWordStructureInfo;
import com.mp.shared.common.MpCode;
import com.mp.shared.common.Page;
import com.mp.shared.common.PageInfo;
import com.mp.shared.common.PageInfo.SubPageInfo;
import com.mp.shared.common.ResourceVersion;
import com.mp.shared.common.ShCode;
import com.mp.shared.common.SuccessResult;
import com.mp.shared.common.Utils;
import com.mp.shared.service.MpResourceDecoder;
import com.mp.shared.utils.FUtils;
import com.mpen.api.bean.Activity;
import com.mpen.api.bean.Book;
import com.mpen.api.bean.ExamResult;
import com.mpen.api.bean.FileParam;
import com.mpen.api.bean.ExamResult.SubTopicResult;
import com.mpen.api.bean.ExamResult.TopicResult;
import com.mpen.api.bean.OralTestInfo;
import com.mpen.api.bean.OralTestInfo.OralTestPaper;
import com.mpen.api.bean.PageScope;
import com.mpen.api.bean.PreBook;
import com.mpen.api.bean.Sentence;
import com.mpen.api.bean.Unit;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Constants.CacheType;
import com.mpen.api.common.Constants.ManualOp;
import com.mpen.api.common.Progress;
import com.mpen.api.domain.CacheInfos;
import com.mpen.api.domain.DdbBookCoreDetail;
import com.mpen.api.domain.DdbBookDetail;
import com.mpen.api.domain.DdbLearnWordStructureDetail;
import com.mpen.api.domain.DdbOraltestBook;
import com.mpen.api.domain.DdbPageDetail;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbResourceBook;
import com.mpen.api.domain.DdbResourceBookCatalog;
import com.mpen.api.domain.DdbResourceBookPrint;
import com.mpen.api.domain.DdbResourceCode;
import com.mpen.api.domain.DdbResourcePageCode;
import com.mpen.api.domain.DdbResourcePageScope;
import com.mpen.api.domain.OralTestDetail;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.BookCoreDetailMapper;
import com.mpen.api.mapper.BookDetailMapper;
import com.mpen.api.mapper.DdbOralTestBookMapper;
import com.mpen.api.mapper.LearnWordStructureDetailMapper;
import com.mpen.api.mapper.OralTestDetailMapper;
import com.mpen.api.mapper.PageCodeMapper;
import com.mpen.api.mapper.PageDetailMapper;
import com.mpen.api.mapper.PageScopeMapper;
import com.mpen.api.mapper.ResourceBookCatalogMapper;
import com.mpen.api.mapper.ResourceBookMapper;
import com.mpen.api.mapper.ResourceBookPrintMapper;
import com.mpen.api.service.DecodeService;
import com.mpen.api.service.FileService;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.service.ResourceBookService;
import com.mpen.api.service.ResourceCodeService;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.EmailUtil;
import com.mpen.api.util.FileUtils;
import com.mpen.api.util.MpCodeBuilder;
import com.mpen.api.util.MpCodeBuilder.FileType;

/**
 * ResourceBookService服务.
 *
 * @author kai
 *
 */
@Component
public class ResourceBookServiceImpl implements ResourceBookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceBookServiceImpl.class);

    @Autowired
    private ResourceBookMapper resourceBookMapper;
    @Autowired
    private MemCacheService memCacheService;
    @Autowired
    private ResourceBookCatalogMapper resourceBookCatalogMapper;
    @Autowired
    private PageScopeMapper pageScopeMapper;
    @Autowired
    private PageCodeMapper pageCodeMapper;
    @Autowired
    private ResourceBookPrintMapper resourceBookPrintMapper;
    @Autowired
    private BookDetailMapper bookDetailMapper;
    @Autowired
    private LearnWordStructureDetailMapper learnWordStructureDetailMapper;
    @Autowired
    private PageDetailMapper pageDetailMapper;
    @Autowired
    private BookCoreDetailMapper bookCoreDetailMapper;
    @Autowired
    private FileService fileService;
    @Autowired
    private ResourceCodeService resourceCodeService;
    @Autowired
    private DecodeService decodeService;
    @Autowired
    private OralTestDetailMapper oralTestDetailMapper;
    @Autowired
    private DdbOralTestBookMapper ddbOralTestBookMapper;
    @Autowired
    private ResourceBookService resourceBookService;
    @Autowired
    private EmailUtil emailUtil;

    /*
     * TODO currentVersion应该是和ArrayList<BookInfo>关联的。 应该： 1）增加一个
     * GlobalDianduData table 这个table就是一个 key：value。一行key是 BookInfoList，value存储
     * Page<BookInfo> 2）public void cacaheBookInfo() 里面会设置这个table的BookInfoList
     * 3）getAllValidBooks 如果有cache
     * miss，就直接从table读取数据，设置Constants.CACHE_BOOKINFO_VERSION_KEY 和
     * Constants.CACHE_BOOKINFO_KEY
     */
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Page<T> getCacheInfos(Class<T> c, String bookId, CacheType type, ResourceVersion version) throws Exception {
        String key = "";
        ResourceVersion currentVersion = null;
        Page<T> cachePage = null;
        switch (type) {
        case CACHE_BOOK:
            key = CommUtil.getCacheKey(Constants.CACHE_BOOKINFO_VERSION_KEY);
            cachePage = (Page<T>) Constants.bookInfos;
            break;
        case CACHE_PAGE:
            key = CommUtil.getCacheKey(Constants.CACHE_PAGEINFO_VERSION_KEY + bookId);
            cachePage = (Page<T>) Constants.pageInfoList.get(bookId);
            break;
        case CACHE_GLOBAL_PAGE:
            key = CommUtil.getCacheKey(Constants.CACHE_PAGEINFO_VERSION_KEY);
            cachePage = (Page<T>) Constants.pageInfos;
            break;
        case CACHE_FULL_BOOKS:
            key = CommUtil.getCacheKey(Constants.CACHE_FULLBOOKINFO_VERSION_KEY);
            cachePage = (Page<T>) Constants.fullBookInfos;
            break;
        case CACHE_LEARN_WORD_STRUCTURE_INFOS:
            key = CommUtil.getCacheKey(Constants.CACHE_STRUCTUREINFO_VERSION_KEY);
            cachePage = (Page<T>) Constants.learnWordStructureInfos;
            break;
        default:
            break;
        }
        try {
            currentVersion = memCacheService.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (currentVersion == null) {
            updateLocalCache(type, bookId);
            return cachePage;
        }
        // 获取bookList时如果申请的版本号和当前缓存版本号相同,返回null
        if (version != null && version.compareTo(currentVersion) == 0) {
            return null;
        }
        final Page<T> localPage = cachePage;
        if (localPage != null && localPage.getVersion() != null
                && localPage.getVersion().compareTo(currentVersion) == 0) {
            return localPage;
        }
        updateLocalCache(type, bookId);
        return cachePage;
    }

    @Override
    public synchronized void updateLocalCache(CacheType type, String bookId) {
        switch (type) {
        case CACHE_BOOK:
        case CACHE_FULL_BOOKS:
        case CACHE_LEARN_WORD_STRUCTURE_INFOS: {
            try {
                // 从数据库中查询booklists
                final DdbBookDetail bookDetails = bookDetailMapper.get();
                final DdbLearnWordStructureDetail learnWordStructureDetails = learnWordStructureDetailMapper.get();
                final DdbBookCoreDetail bookCoreDetails = bookCoreDetailMapper.get(true);
                if (bookDetails == null || bookCoreDetails == null || learnWordStructureDetails == null) {
                    return;
                }
                final Page<FullBookInfo> localFullBookPage = bookDetails.formDetail();
                final Page<LearnWordStructureInfo> localLearnWordPage = learnWordStructureDetails.formDetail();
                final Page<BookInfo> localBookPage = bookCoreDetails.formDetail();
                String key = CommUtil.getCacheKey(Constants.CACHE_FULLBOOKINFO_VERSION_KEY);
                memCacheService.set(key, localFullBookPage.getVersion(), Constants.DEFAULT_CACHE_EXPIRATION);
                Constants.fullBookInfos = localFullBookPage;
                key = CommUtil.getCacheKey(Constants.CACHE_BOOKINFO_VERSION_KEY);
                memCacheService.set(key, localBookPage.getVersion(), Constants.DEFAULT_CACHE_EXPIRATION);
                Constants.bookInfos = localBookPage;
                key = CommUtil.getCacheKey(Constants.CACHE_STRUCTUREINFO_VERSION_KEY);
                memCacheService.set(key, localLearnWordPage.getVersion(), Constants.DEFAULT_CACHE_EXPIRATION);
                Constants.learnWordStructureInfos = localLearnWordPage;
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
        case CACHE_PAGE: {
            try {
                if (null == bookId) {
                    return;
                }
                final DdbPageDetail pageDetails = pageDetailMapper.get(bookId, true);
                if (pageDetails == null) {
                    return;
                }
                final Page<PageInfo> localPage = pageDetails.formDetail();
                final String key = CommUtil.getCacheKey(Constants.CACHE_PAGEINFO_VERSION_KEY + bookId);
                memCacheService.set(key, localPage.getVersion(), Constants.DEFAULT_CACHE_EXPIRATION);
                // 单本书的pageInfo存放在本地
                Constants.pageInfoList.put(bookId, localPage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
        case CACHE_GLOBAL_PAGE: {
            try {
                // 查询出包含全部书籍的pageInfo
                final DdbPageDetail pageDetail = pageDetailMapper.get(Constants.GLOBAL_BOOKID_KEY, true);
                if (pageDetail == null) {
                    return;
                }
                final Page<PageInfo> globalPageInfo = pageDetail.formDetail();
                final String globalPageKey = CommUtil.getCacheKey(Constants.CACHE_PAGEINFO_VERSION_KEY);
                memCacheService.set(globalPageKey, globalPageInfo.getVersion(), Constants.DEFAULT_CACHE_EXPIRATION);
                // 生成全部书的pageInfoList,MP云点读使用
                Constants.pageInfos = globalPageInfo;
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
        default:
            break;
        }
    }

    @Override
    public DdbResourceBook getById(String id) throws CacheException {
        DdbResourceBook book = null;
        final String key = CommUtil.getCacheKey(Constants.CACHE_BOOKINFO_KEY_PRIFIX + id);
        try {
            book = memCacheService.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (book == null) {
            book = resourceBookMapper.getId(id);
            if (book != null) {
                try {
                    memCacheService.set(key, book, Constants.DEFAULT_CACHE_EXPIRATION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return book;
    }

    @Override
    public List<PreBook> getPrepownloadBooks() throws Exception {
        final Page<FullBookInfo> allBookInfoPage = getCacheInfos(FullBookInfo.class, null, CacheType.CACHE_FULL_BOOKS, null);
        if (allBookInfoPage == null) {
            return null;
        }
        final List<FullBookInfo> books = allBookInfoPage.getItems();
        if (books != null && books.size() > 0) {
            final List<PreBook> list = new ArrayList<PreBook>();
            PreBook preBook = null;
            for (FullBookInfo fullBookInfo : books) {
                preBook = new PreBook();
                preBook.setId(fullBookInfo.bookInfo.id);
                preBook.setResSize(fullBookInfo.bookInfo.downloadSize);
                preBook.setName(fullBookInfo.bookInfo.name);
                list.add(preBook);
            }
            return list;
        }
        return null;
    }

    @Override
    public List<Unit> getBookContent(String bookId, String str) throws SdkException, CacheException {
        final String key = CommUtil.getCacheKey(str + bookId);
        List<Unit> unitsList = null;
        unitsList = memCacheService.get(key);
        if (unitsList != null) {
            return unitsList;
        }
        final ResourceBookCatalogMapper bookCatalogMapper = resourceBookCatalogMapper;
        final ResourceCodeService codeService = resourceCodeService;
        final MemCacheService cacheService = memCacheService;
        Constants.CACHE_THREAD_POOL.execute(() -> {
            final List<DdbResourceBookCatalog> units = bookCatalogMapper.getByBookIdAndName(bookId, Constants.UNIT);
            if (units == null || units.size() == 0) {
                return;
            }
            final List<Unit> unitsLists = new ArrayList<Unit>();
            for (DdbResourceBookCatalog unitCatalog : units) {
                final Unit studyUnit = new Unit();
                final DdbResourceBookCatalog catalog = bookCatalogMapper.getById(unitCatalog.getFkCatalogId());
                studyUnit.setModel(StringUtils.isBlank(catalog.getItem()) ? Constants.MODULE + " " + catalog.getNumber()
                    : catalog.getItem());
                studyUnit.setUnit(unitCatalog.getNumber() == 0 ? "" : Constants.UNIT + " " + unitCatalog.getNumber());
                studyUnit.setName(unitCatalog.getItem() == null ? "" : unitCatalog.getItem().replace("#1", "'"));
                final List<DdbResourceBookCatalog> activitys = bookCatalogMapper
                    .getByBookIdAndNameAndFkCatalogId(bookId, Constants.ACTIVITY, unitCatalog.getId());
                final List<Activity> activityList = new ArrayList<Activity>();
                for (DdbResourceBookCatalog activityCatalog : activitys) {
                    Activity activity = new Activity();
                    activity.setId(activityCatalog.getId());
                    activity
                        .setName(activityCatalog.getItem() == null ? "" : activityCatalog.getItem().replace("#1", "'"));
                    activity.setSort(Constants.ACTIVITY + " "
                        + (activityCatalog.getNumber() == 0 ? 1 : activityCatalog.getNumber()));
                    if (!Constants.CACHE_SPOKEN_PREFIX.equals(str)) {
                        activityList.add(activity);
                    } else {
                        final List<DdbResourceCode> list = codeService.getByCatalogId(bookId, activityCatalog.getId());
                        if (list != null && list.size() > 0) {
                            List<Sentence> textList = new ArrayList<Sentence>();
                            for (DdbResourceCode code : list) {
                                final Sentence sentence = new Sentence();
                                sentence.setTitle(code.getText().replace("#1", "'"));
                                textList.add(sentence);
                            }
                            activity.setSentences(textList);
                            activityList.add(activity);
                        }
                    }
                }
                studyUnit.setActivities(activityList);
                unitsLists.add(studyUnit);
            }
            try {
                cacheService.set(key, unitsLists, Constants.DEFAULT_CACHE_EXPIRATION);
            } catch (CacheException e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    private ArrayList<PageInfo> updatePageInfos(String bookId) throws CacheException {
        final long time = System.currentTimeMillis();
        ArrayList<PageInfo> pageInfos = new ArrayList<PageInfo>();
        final List<PageScope> pageScopes = pageScopeMapper.get(bookId);
        for (PageScope pageScope : pageScopes) {
            PageInfo pageInfo = new PageInfo();
            pageInfo.id = pageScope.getId();
            pageInfo.bookId = pageScope.getBookId();
            pageInfo.pageNum = pageScope.getPageNum();
            pageInfo.startCode = new MpCode(pageScope.getCodeStart(), 0, new Byte("5"));
            pageInfo.matrixSize = pageScope.getMatrixSize().byteValue();
            pageInfo.matrixGap = pageScope.getMatrixGap().byteValue();
            pageInfo.dotDistanceInPixels = pageScope.getDotDistanceInPixels().byteValue();
            pageInfo.dotSize = pageScope.getDotSize().byteValue();
            pageInfo.dotShift = pageScope.getDotShift().byteValue();
            pageInfo.version = new ResourceVersion(time, 0);
            // 对铺码过的旧书做兼容处理
            if (StringUtils.isBlank(pageScope.getSubPages())) {
                pageInfo.xCodeNum = pageScope.getxCodeNum();
                pageInfo.yCodeNum = pageScope.getyCodeNum();
                pageInfo.margin = new int[4];
                pageInfo.margin[0] = pageScope.getLeftMargin();
                pageInfo.margin[1] = pageScope.getTopMargin();
                pageInfo.margin[2] = pageScope.getRightMargin();
                pageInfo.margin[3] = pageScope.getBottomMargin();
            } else {
                pageInfo.subPageInfos = Constants.GSON.fromJson(pageScope.getSubPages(), SubPageInfo[].class);
                pageInfo.xCodeNum = 1;
                pageInfo.yCodeNum = (int) (pageScope.getCodeEnd() - pageScope.getCodeStart() + 1);
            }
            pageInfos.add(pageInfo);
        }
        return pageInfos;
    }

    @Override
    public BookInfo getBookInfo(String bookId) throws Exception {
        if (StringUtils.isBlank(bookId)) {
            throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
        }
        final Page<BookInfo> bookInfoPage = getCacheInfos(BookInfo.class, bookId, CacheType.CACHE_BOOK, null);
        if (bookInfoPage == null) {
            return null;
        }
        final ArrayList<BookInfo> bookInfos = (ArrayList<BookInfo>) bookInfoPage.getItems();
        BookInfo bookInfo = new BookInfo();
        bookInfo.id = bookId;
        final int index = queryBookInfoFromBookInfos(bookInfos, bookInfo);
        if (index < 0) {
            throw new SdkException(Constants.NO_MACHING_ERROR_MSG);
        }
        bookInfo = bookInfos.get(index);
        return bookInfo;
    }

    @Override
    public LearnWordStructureInfo getStructureInfo(String bookId) throws Exception {
        if (StringUtils.isBlank(bookId)) {
            throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
        }
        final List<LearnWordStructureInfo> structureInfos = getCacheInfos(LearnWordStructureInfo.class, null,
                CacheType.CACHE_LEARN_WORD_STRUCTURE_INFOS, null).getItems();
        if (structureInfos == null) {
            return null;
        }
        for (LearnWordStructureInfo learnWordStructureInfo : structureInfos) {
            if (learnWordStructureInfo.bookId.equals(bookId)) {
                return learnWordStructureInfo;
            }
        }
        return null;
    }

    @Override
    public PageInfo getPageInfoByMpCode(MpCode mpCode) throws Exception {
        final Page<PageInfo> page = getCacheInfos(PageInfo.class, null, CacheType.CACHE_GLOBAL_PAGE, null);
        if (page == null) {
            return null;
        }
        final ArrayList<PageInfo> pageInfos = (ArrayList<PageInfo>) page.getItems();
        PageInfo pageInfo = new PageInfo();
        pageInfo.startCode = mpCode;
        if (pageInfos == null || pageInfos.size() == 0) {
            throw new SdkException(Constants.NO_MACHING_ERROR_MSG);
        }
        pageInfo = queryPageInfoFromPageInfos(pageInfos, pageInfo);
        if (pageInfo == null) {
            throw new SdkException(Constants.NO_MACHING_ERROR_MSG);
        }
        return pageInfo;
    }

    /**
     * 二分法查找所在索引.
     * 
     */
    private int queryIndexFromPageInfos(ArrayList<PageInfo> arrayList, PageInfo pageInfo) {
        if (arrayList == null || pageInfo == null) {
            return -1;
        }
        final int index = Collections.binarySearch(arrayList, pageInfo);
        return index;
    }

    /**
     * 二分法查找所在索引PageInfo对象.
     * 
     */
    private PageInfo queryPageInfoFromPageInfos(ArrayList<PageInfo> arrayList, PageInfo pageInfo) {
        if (arrayList == null || pageInfo == null) {
            return null;
        }
        int index = queryIndexFromPageInfos(arrayList, pageInfo);
        if (index >= 0) {
            return arrayList.get(index);
        } else if (index < -1) {
            index = Math.abs(index) - 2;
            // TODO checkInPage之后会用于计算笔尖位置
            int[] checkInPage = arrayList.get(index).checkInPage(pageInfo.startCode);
            if (checkInPage != null) {
                return arrayList.get(index);
            }
        }
        return null;
    }

    /**
     * 二分法查找所在索引.
     * 
     */
    private int queryBookInfoFromBookInfos(ArrayList<BookInfo> arrayList, BookInfo bookInfo) {
        if (arrayList == null || bookInfo == null) {
            return -1;
        }
        final Comparator<BookInfo> c = (BookInfo u1, BookInfo u2) -> u1.id.compareTo(u2.id);
        int index = Collections.binarySearch(arrayList, bookInfo, c);
        return index;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SuccessResult addBook(Book bookParam) throws SdkException {
        if (StringUtils.isBlank(bookParam.getBookId()) || StringUtils.isBlank(bookParam.getName())) {
            throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
        }
        final SuccessResult result = new SuccessResult();
        DdbResourceBook book = resourceBookMapper.getId(bookParam.getBookId());
        // 创建书时判断数据库中相同书名的书创建时间,如果在一天(86400秒)之内视为重复的,不允许创建(避免出现同时创建两本相同的书)
        final DdbResourceBook dbBook = resourceBookMapper.getByName(bookParam.getName());
        long time = 99999; // 默认大于一天
        if (dbBook != null) {
            time = CommUtil.diffInSeconds(dbBook.getCreateDatetime());
        }
        if (book == null && time > 86400) {
            book = new DdbResourceBook();
            book.setId(bookParam.getBookId());
            book.setName(bookParam.getName());
            book.setIsbn(bookParam.getIsbn());
            book.setCreateDatetime(Date.from(Instant.now()));
            book.setType(bookParam.getType() == null ? FullBookInfo.Type.OTHER : bookParam.getType());
            // 考虑到兼容,设置默认值为1(支持云点读)
            book.setIsLineRead(bookParam.getIsLineRead());
            // 出版系统生成的书序列号默认为999;范围:0-999;暂时有两处位置赋值:1.管理端手动赋值,2.数据库默认值0.
            book.setSequence(999);
            resourceBookMapper.create(book);
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        result.setUuid(book.getId());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SuccessResult createCode(Book bookParam) throws SdkException, CacheException {
        // 1、校验参数合法性
        if (StringUtils.isBlank(bookParam.getBookId()) || bookParam.getPageNum() == 0
            || StringUtils.isBlank(bookParam.getVersion()) || bookParam.getPageParam() == null) {
            throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
        }
        final DdbResourceBook book = resourceBookMapper.getId(bookParam.getBookId());
        if (book == null || StringUtils.isBlank(bookParam.getVersion())) {
            throw new SdkException(Constants.NO_MACHING_BOOK);
        }
        DdbResourceBookPrint version = resourceBookPrintMapper.getByBookIdAndName(bookParam.getBookId(),
            bookParam.getVersion());
        final DdbResourceBookPrint baseVersion = resourceBookPrintMapper.getByBookIdAndName(bookParam.getBookId(),
            bookParam.getBaseVersion());
        if (version != null || (baseVersion == null && bookParam.getPageNum() != bookParam.getPageParam().length)) {
            throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
        }
        if (Constants.createCodeIsLock) {
            throw new SdkException(Constants.IS_BUSY);
        }
        synchronized (this) {
            if (Constants.createCodeIsLock) {
                throw new SdkException(Constants.IS_BUSY);
            }
            Constants.createCodeIsLock = true;
        }
        final String key = CommUtil.getCacheKey(Constants.CREATE_CODE + book.getId());
        final Progress cacheProgress = new Progress(book.getId(), bookParam.getPageNum(), 0, false, Constants.SUCCESS,
            bookParam.getVersion());
        memCacheService.set(key, cacheProgress, Constants.DEFAULT_CACHE_EXPIRATION);

        try {
            // 2、创建版本信息
            version = new DdbResourceBookPrint(CommUtil.genRecordKey(), book.getId(), bookParam.getVersion());
            resourceBookPrintMapper.save(version);
            // 3、初始化获取页信息
            List<DdbResourcePageCode> bookPages = pageCodeMapper.getBookPages(book.getId());
            if (bookPages == null || bookPages.size() == 0) {
                bookPages = new ArrayList<>(bookParam.getPageNum());
                final Book.PageParam[] pageParam = bookParam.getPageParam();
                for (int i = 0; i < pageParam.length; i++) {
                    final DdbResourcePageCode page = new DdbResourcePageCode(
                        book.getName() + "第" + pageParam[i].getNum() + "页", pageParam[i].getNum(), book.getId(),
                        pageParam[i].getWidthMm(), pageParam[i].getHeightMm());
                    pageCodeMapper.save(page);
                    bookPages.add(page);
                }
            } else if (bookPages != null && bookParam.getPageNum() != bookPages.size()) {
                throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
            }
            // 4、新建线程生成点读码
            final DdbResourceBookPrint versionTemp = version;
            final List<DdbResourcePageCode> bookPagesTemp = bookPages;
            Constants.CACHE_THREAD_POOL.execute(() -> {
                createTif(pageScopeMapper, pageCodeMapper, resourceBookPrintMapper, memCacheService, cacheProgress,
                    versionTemp, baseVersion, key, book, bookParam, bookPagesTemp);
            });
        } catch (Exception e) {
            // 出现异常时需要解锁
            cacheProgress.msg = Constants.FAIL;
            cacheProgress.finish = true;
            memCacheService.set(key, cacheProgress, Constants.DEFAULT_CACHE_EXPIRATION);
            Constants.createCodeIsLock = false;
            throw e;
        }
        return new SuccessResult(true);
    }

    private void createTif(PageScopeMapper pageScopeMapper, PageCodeMapper pageCodeMapper,
        ResourceBookPrintMapper resourceBookPrintMapper, MemCacheService cache, Progress progress,
        DdbResourceBookPrint version, DdbResourceBookPrint baseVersion, String key, DdbResourceBook book,
        Book bookParam, List<DdbResourcePageCode> bookPages) {
        try {
            final FileType fileType = bookParam.getFileType() == null ? FileType.TIF : bookParam.getFileType();
            final MpCodeBuilder builder = new MpCodeBuilder(book);

            // 获取铺码起始码值与起始代号
            final Map<String, Object> temp = pageScopeMapper.getSignAndCode();
            int sign = (int) temp.get("SIGN") + 1;
            long code = (long) temp.get("CODE_END") + 1;

            final List<String> fileList = new ArrayList<String>();

            progress.uuid = book.getId();
            progress.allItems = bookPages.size();
            final Book.PageParam[] pageParams = bookParam.getPageParam();
            for (DdbResourcePageCode page : bookPages) {
                DdbResourcePageScope pageScope = null;
                inner: for (Book.PageParam pageParam : pageParams) {
                    if (pageParam.getNum() == page.getPageNum()) {
                        if (pageParam.getDotParam() == null) {
                            pageParam.setDotParam(bookParam.getDefaultDotParam());
                        }
                        if (page.getWidth() == 0 || page.getHeight() == 0) {
                            page.setWidth(book.getWidth());
                            page.setHeight(book.getHeight());
                        }
                        if (pageParam.getIsNewCode() == Constants.INT_ONE) {
                            pageScope = pageScopeMapper.getByTypeAndPage(baseVersion.getId(), page.getId());
                            // 生成点读码，保存码段信息
                            pageScope = builder.createCode(pageScope.getCodeStart(), sign, pageParam, page);
                        } else {
                            pageScope = builder.createCode(code, sign, pageParam, page);
                            code = pageScope.getCodeEnd() + 1;
                        }
                        break inner;
                    } else if (pageParam.getNum() > page.getPageNum()) {
                        break inner;
                    }
                }
                if (pageScope == null) {
                    pageScope = pageScopeMapper.getByTypeAndPage(baseVersion.getId(), page.getId());
                    pageScope.setId(CommUtil.genRecordKey());
                    pageScope.setSign(sign);
                    pageScope.setRebuild(Constants.ZERO);
                }
                pageScope.setFkTypeId(version.getId());
                pageScopeMapper.save(pageScope);
                sign++;

                final String tifFileRealPath = FileUtils.getFileSaveRealPath(pageScope.getTifLink(), false);
                String fileRealPath = "";
                switch (fileType) {
                case TIF:
                    fileRealPath = tifFileRealPath;
                    break;
                case PNG:
                    fileRealPath = tifFileRealPath.replace(FileType.TIF.getSuffix(), FileType.PNG.getSuffix());
                    MpCodeBuilder.changeFileType(tifFileRealPath, FileType.TIF, fileRealPath, FileType.PNG);
                    break;
                }
                fileList.add(fileRealPath);
                // 更新进度
                progress.nowItems = page.getPageNum();
                cache.set(key, progress, Constants.DEFAULT_CACHE_EXPIRATION);
            }
            final String fileSavePath = FileUtils.getBookFolderSavePath(FileUtils.BOOK_ZIP, book.getId());
            final String filePath = FileUtils.getFileSaveRealPath(fileSavePath);
            final String fileName = version.getName() + fileType.getTypeName() + ".zip";
            FileUtils.zipFile(fileList, filePath + fileName);
            final String zipFilePath = fileSavePath + fileName;
            version.setZipLink(zipFilePath);
            resourceBookPrintMapper.updateZipLink(version);
            progress.msg = FileUtils.getFullRequestPath(zipFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            progress.msg = Constants.FAIL;
        } finally {
            // 解锁
            try {
                progress.finish = true;
                cache.set(key, progress, Constants.DEFAULT_CACHE_EXPIRATION);
            } catch (Exception e) {
                e.printStackTrace();
            }
            synchronized (this) {
                Constants.createCodeIsLock = false;
            }
        }
    }

    
    @Override
    public Progress getProgress(Book book) throws CacheException {
        final String key = CommUtil.getCacheKey(Constants.CREATE_CODE + book.getBookId());
        final Progress progress = memCacheService.get(key);
        return progress;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SuccessResult updateBookLink(FileParam fileParam, String path, String fileMd5)
            throws CacheException, SdkException {
        final String fileName = fileParam.getFile().getOriginalFilename();
        final DdbResourceBook book = getById(fileParam.getUuid());
        if (book == null) {
            throw new SdkException(Constants.NO_MACHING_ERROR_MSG);
        }
        switch (FileUtils.getExtension(fileName)) {
        case "MP":
            book.setBookLink(path);
            book.setMpLinkMd5(fileMd5);
            break;
        case "MPV":
            book.setMpvLink(path);
            book.setMpvLinkMd5(fileMd5);
            break;
        case "MPP":
            book.setMppLink(path);
            book.setMppLinkMd5(fileMd5);
            break;
        case "zip":
        case "rar":
            book.setTeachLink(path);
            book.setTeachLinkMd5(fileMd5);
            break;
        default:
            break;
        }
        // ResSize代表课件资源总大小
        book.setResSize(fileParam.getFile().getSize() + book.getResSize());
        book.setVersion(new ResourceVersion(System.currentTimeMillis(), 0).toString());
        resourceBookMapper.update(book);
        final String key = CommUtil.getCacheKey(Constants.CACHE_BOOKINFO_KEY_PRIFIX + fileParam.getUuid());
        memCacheService.delete(key);
        final SuccessResult result = new SuccessResult();
        result.setSuccess(true);
        result.setUuid(fileParam.getUuid());
        // 更新完资源之后更新bookList与pageInfo
        Constants.CACHE_THREAD_POOL.execute(() -> {
            try {
                updateBookDetail();
                updatePageInfoDetail(fileParam.getUuid(), false, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return result;
    }
    
    // 便利方法：检查 文件名不为空，转换为实际目录，以及检查文件在不在
    private static final File checkAndCreate(String filePath) {
    	try {
	    	if (StringUtils.isNotBlank(filePath)) {
	    		final File file = new File(FileUtils.getFileSaveRealPath(filePath, false));
	    		if (file.exists()) {
	    			return file;
	    		}
	    	}
    	} catch (Exception e) {}
    	return null;
    }
    
    @Override
    public FullBookInfo toFullBookInfo(DdbResourceBook book, boolean checkFiles) {
        if (StringUtils.isBlank(book.getBookLink())
        		|| !book.getBookLink().toLowerCase().endsWith(".mp")) {
        	return null;  // 没有mp包，快速返回
        }
        final FullBookInfo fullBookInfo = new FullBookInfo();
        final BookInfo bookInfo = new BookInfo();
        // 设置 FullBookInfo 数据
        fullBookInfo.bookInfo = bookInfo;
        fullBookInfo.numPages = book.getBookPageNum();
        fullBookInfo.sequence = book.getSequence();
        fullBookInfo.isbn = book.getIsbn();
        fullBookInfo.photo = StringUtils.isBlank(book.getPhoto()) ? book.getSuitImage() : book.getPhoto();
        fullBookInfo.grade = book.getGrade();
        fullBookInfo.type = book.getType();
        fullBookInfo.author = book.getAuthor();
        fullBookInfo.introduction = book.getIntroduction();
        fullBookInfo.isPreDownload = book.getIsPreDownload();
        fullBookInfo.mppLink = book.getMppLink();
        fullBookInfo.mpvLink = book.getMpvLink();
        fullBookInfo.teachLink = book.getTeachLink();
        // 设置 BookInfo 数据
        bookInfo.id = book.getId();
        bookInfo.fullName = book.getName();

        // 刷新booklist接口需要返回是否仅支持本地点读(不支持云点读)字段
        bookInfo.isLineRead = book.getIsLineRead();
        if (!checkFiles) {
        	return fullBookInfo;
        }
        // 继续读文件检查
        // 检查并且读取 mp 文件； mp 文件必须存在而且可读
        final File mpFile = checkAndCreate(book.getBookLink());
        if (mpFile == null) {
        	return null;
        }
        bookInfo.downloadSize = mpFile.length();
        if (!MpResourceDecoder.getFullBookInfo(fullBookInfo, mpFile.getAbsolutePath())
        		|| !fullBookInfo.isValid() || !fullBookInfo.bookInfo.isValid()) {
        	return null;
        }
        
        // 检查其它文件，这些文件不必须存在
        if (checkAndCreate(fullBookInfo.mppLink) == null) {
        	fullBookInfo.mppLink = null;
        }
        if (checkAndCreate(fullBookInfo.mpvLink) == null) {
        	fullBookInfo.mpvLink = null;
        }
        if (checkAndCreate(fullBookInfo.teachLink) == null) {
        	fullBookInfo.teachLink = null;
        }
        
        return fullBookInfo;
    }

    @Override
    public void updateBookDetail() throws Exception {
        // LOGGER.error("cacaheBookInfo - 4");
        final List<DdbResourceBook> books = resourceBookMapper.getValidBooks();
        final List<FullBookInfo> fullBookInfos = new ArrayList<>();
        final List<BookInfo> bookInfos = new ArrayList<>();
        final List<LearnWordStructureInfo> learnWordStructureInfos = new ArrayList<>();
        // int numBooks = 0;
        int numErrors = 0;
        for (DdbResourceBook book : books) {
            final FullBookInfo fullBookInfo = toFullBookInfo(book, true);
            if (fullBookInfo == null) {
                numErrors++;
                LOGGER.error("updateBookDetail - invalid book: " + book.getId() + " -->#" + numErrors);
                continue;
            }
            fullBookInfos.add(fullBookInfo);
            bookInfos.add(fullBookInfo.bookInfo);
            // 生成LearnWordStructureInfo
            if (LearnWordStructureInfo.isLearnWord(fullBookInfo.bookInfo)) {
                final String fileSavePath = FileUtils.getTempFileSavePath(fullBookInfo.bookInfo, Code.Type.SH + "");
                final String mpPath = FileUtils.getFileSaveRealPath(book.getBookLink(), false);
                final LearnWordStructureInfo structureInfo = MpResourceDecoder.getLearnWordStructure(fullBookInfo,
                        mpPath, fileSavePath);
                if (structureInfo != null) {
                    learnWordStructureInfos.add(structureInfo);
                }
            }
        }
        LOGGER.error("updateBookDetail - processed valid:invalid books: " + fullBookInfos.size() + ":" + numErrors);
        // 对处理结果排序
        final Comparator<FullBookInfo> c1 = (FullBookInfo u1, FullBookInfo u2) -> u1.sequence - u2.sequence;
        final Comparator<BookInfo> c2 = (BookInfo u1, BookInfo u2) -> u1.id.compareTo(u2.id);
        Collections.sort(fullBookInfos, c1);
        Collections.sort(bookInfos, c2);
        Collections.sort(learnWordStructureInfos);
        // 判断生成的booklist是否有效
        if (!Utils.isValid(bookInfos) || !Utils.isContentValid(bookInfos)) {
            LOGGER.error("updateBookDetail - stop as book list is invalid");
            return;
        }
        final Page<FullBookInfo> fullBookPage = new Page<>(fullBookInfos, fullBookInfos.size());
        final Page<BookInfo> bookPage = new Page<>(bookInfos, bookInfos.size());
        final Page<LearnWordStructureInfo> learnWordPage = new Page<>(learnWordStructureInfos,
                learnWordStructureInfos.size());
        // TODO version生成需要修改
        final ResourceVersion version = new ResourceVersion(System.currentTimeMillis(), 0);
        fullBookPage.setVersion(version);
        bookPage.setVersion(version);
        learnWordPage.setVersion(version);
        saveDetail(fullBookPage, bookPage, learnWordPage);
    }

    @Transactional(rollbackFor = Exception.class)
    private void saveDetail(Page<FullBookInfo> fullBookPage, Page<BookInfo> bookPage,
            Page<LearnWordStructureInfo> learnWordPage) throws Exception {
        final DdbBookDetail bookDetail = new DdbBookDetail(fullBookPage);
        final DdbBookDetail localBookDetail = bookDetailMapper.get();
        if (checkDetail(FullBookInfo.class, fullBookPage, localBookDetail)) {
            bookDetailMapper.save(bookDetail);
            LOGGER.error("saveDetail - FullBookInfo save to database");
        } else {
            LOGGER.error("saveDetail - FullBookInfo didn't save to database");
        }
        final DdbBookCoreDetail bookCoreDetail = new DdbBookCoreDetail(bookPage);
        final DdbBookCoreDetail localBookCoreDetail = bookCoreDetailMapper.get(true);
        final String subject = "bookList更新结果";
        // checkAndCompareBookList返回结果为Map:key-->couldCreate(是否允许更新). value-->emailContent(邮件内容)
        final EmailResult emailResult = checkAndCompareBookList(bookCoreDetail, localBookCoreDetail);
        if (checkDetail(BookInfo.class, bookPage, localBookCoreDetail) && emailResult.couldCreate) {
            bookCoreDetailMapper.save(bookCoreDetail);
            LOGGER.error("saveDetail - BookInfo save to database");
        } else {
            // 如果没有更新,发送邮件说明
            emailResult.emailContent = "没有更新内容~";
            LOGGER.error("saveDetail - BookInfo didn't save to database");
        }
        final DdbLearnWordStructureDetail learnWordStructureDetail = new DdbLearnWordStructureDetail(learnWordPage);
        final DdbLearnWordStructureDetail localLearnWordStructureDetail = learnWordStructureDetailMapper.get();
        if (checkDetail(LearnWordStructureInfo.class, learnWordPage, localLearnWordStructureDetail)) {
            learnWordStructureDetailMapper.save(learnWordStructureDetail);
            LOGGER.error("saveDetail - LearnWordStructureInfo save to database");
        } else {
            LOGGER.error("saveDetail - LearnWordStructureInfo didn't save to database");
        }
        emailUtil.sendHtmlEmail(subject, emailResult.emailContent);
        // 缓存仅在将bookList设置为正式版本时进行更新
        // cacheBookInfo();
    }
    
    /**
     * 校验bookList是否有更新以及是否符合更新规则
     * @param bookCoreDetail
     * @param localBookCoreDetail
     * @return
     * @throws Exception
     */
    private EmailResult checkAndCompareBookList(DdbBookCoreDetail bookCoreDetail,
            DdbBookCoreDetail localBookCoreDetail) throws Exception {
        if (localBookCoreDetail == null) {
            return new EmailResult(true, String.format("新增书,版本号=%s", bookCoreDetail.getVersion()));
        }
        final List<Map<String, Object>> newBookIdList = new ArrayList<>();
        final List<Map<String, Object>> delBookIdList = new ArrayList<>();
        // 查询增加的书
        checkBookInfoList(localBookCoreDetail == null ? null : localBookCoreDetail.formDetail(), bookCoreDetail, true,
                newBookIdList, delBookIdList);
        // 查询减少的书,如果超出限定的值,返回false
        if (!checkBookInfoList(bookCoreDetail == null ? null : bookCoreDetail.formDetail(), localBookCoreDetail, false,
                newBookIdList, delBookIdList)) {
            return new EmailResult(false, "");
        }
        // 更新成功,发送邮件:新增或减少的书-->发送bookId.更新的书-->发送bookId及更新的字段内容
        final List<BookInfo> newBookInfos = bookCoreDetail.formDetail().getItems();
        final List<BookInfo> localBookInfos = localBookCoreDetail.formDetail().getItems();
        final List<Map<String, Map<String, Object>>> resList = new ArrayList<>();
        // 循环遍历,比较相同书id的bean,得到之间的差异数据
        for (int i = 0; i < newBookInfos.size(); i++) {
            for (int j = 0; j < localBookInfos.size(); j++) {
                if (newBookInfos.get(i).id.equals(localBookInfos.get(j).id)) {
                    final Map<String, Map<String, Object>> list = compare(newBookInfos.get(i), localBookInfos.get(j),
                            false);
                    if (list != null && list.size() > 0) {
                        resList.add(list);
                    }
                }
            }
        }
        // 得到每本书的bookId,根据bookId查询出code,name,bookLink等信息
        final List<Map<String, Object>> bookDiffList = new ArrayList<>();
        for (Map<String, Map<String, Object>> map : resList) {
            for (String bookId : map.keySet()) {
                final DdbResourceBook ddbResourceBook = resourceBookMapper.getId(bookId);
                if (null != ddbResourceBook) {
                    final Map<String, Object> bookDiffMap = new HashMap<>();
                    bookDiffMap.put("id", ddbResourceBook.getId());
                    bookDiffMap.put("name", ddbResourceBook.getName());
                    bookDiffMap.put("code", ddbResourceBook.getCode());
                    bookDiffMap.put("path", ddbResourceBook.getBookLink());
                    bookDiffList.add(bookDiffMap);
                }
            }
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final boolean couldCreate = (resList != null && resList.size() > 0) || delBookIdList.size() > 0
                || newBookIdList.size() > 0;
        return new EmailResult(couldCreate, String.format("新增的书=%s<br>减少的书=%s<br>更新的书=%s<br>版本号=%s", gson.toJson(newBookIdList),
                gson.toJson(delBookIdList), gson.toJson(bookDiffList), bookCoreDetail.getVersion()));
    }
    
    /**
     * 检查bookList中书的增减并记录
     * @param bookPage
     * @param localBookCoreDetail
     * @param addRecord: true:记录增加的书id. false:记录减少的书id
     * @return
     */
    private boolean checkBookInfoList(Page<BookInfo> bookPage, DdbBookCoreDetail localBookCoreDetail, boolean addRecord,
            List<Map<String, Object>> newBookIdList, List<Map<String, Object>> delBookIdList) {
        if (localBookCoreDetail == null) {
            return true;
        }
        final ArrayList<BookInfo> bookInfos = (ArrayList<BookInfo>) bookPage.getItems();
        final List<BookInfo> localBookInfos = localBookCoreDetail.formDetail().getItems();
        int number = 0;
        for (BookInfo bookInfo : localBookInfos) {
            final int index = queryBookInfoFromBookInfos(bookInfos, bookInfo);
            if (index < 0) {
                number++;
                final Map<String, Object> bookMap = new HashMap<>();
                final ShCode[] coverCodes = bookInfo.coverCodes;
                bookMap.put("id", bookInfo.id);
                bookMap.put("name", bookInfo.fullName);
                bookMap.put("code", coverCodes == null ? "" : coverCodes[0].code);
                if (addRecord) {
                    // 记录增加的书id,增加的书和更新的书返回邮件内容一致,需要查询出bookLink
                    final DdbResourceBook ddbResourceBook = resourceBookMapper.getId(bookInfo.id);
                    bookMap.put("path", ddbResourceBook == null ? "" : ddbResourceBook.getBookLink());
                    newBookIdList.add(bookMap);
                } else {
                    // 记录减少的书id
                    delBookIdList.add(bookMap);
                }
            }
        }
        // 当减少的书籍大于十本时，认为BookList有误，不允许更新
        if (number > 10) {
            return false;
        }
        return true;
    }
    
    /**
     * 检查pageInfo中书的增减并记录
     * @param bookPage
     * @param localBookCoreDetail
     * @param addRecord: true:记录增加的书id. false:记录减少的书id
     * @return
     */
    private void checkPageInfoList(Page<PageInfo> bookPage, DdbPageDetail localPageCoreDetail, boolean addRecord,
            List<Map<String, Object>> newPageIdList, List<Map<String, Object>> delPageIdList) {
        if (localPageCoreDetail == null || bookPage == null) {
            return;
        }
        final ArrayList<PageInfo> pageInfos = (ArrayList<PageInfo>) bookPage.getItems();
        final List<PageInfo> localPageInfos = localPageCoreDetail.formDetail().getItems();
        for (PageInfo pageInfo : localPageInfos) {
            final int index = queryIndexFromPageInfos(pageInfos, pageInfo);
            if (index < 0) {
                final Map<String, Object> pageMap = new HashMap<>();
                pageMap.put("pageId", pageInfo.id);
                pageMap.put("bookId", pageInfo.bookId);
                pageMap.put("pageNum", pageInfo.pageNum);
                if (addRecord) {
                    // 记录增加的书id,pageId,pageNum,查询出pageName
                    final DdbResourcePageCode ddbResourcePageCode = pageCodeMapper.getPageCodeByPageId(pageInfo.id);
                    pageMap.put("pageName", ddbResourcePageCode == null ? "" : ddbResourcePageCode.getName());
                    newPageIdList.add(pageMap);
                } else {
                    // 记录减少的书id
                    delPageIdList.add(pageMap);
                }
            }
        }
    }

    @Override
    public List<FileModule> downloadBook(String id) throws Exception {
        final DdbResourceBook book = getById(id);
        if (book == null) {
            throw new SdkException(Constants.NO_MACHING_BOOK);
        }
        return fileService.fileDownload(book.getBookLink());
    }

    private <T> boolean checkDetail(Class<T> c, Page<T> page, CacheInfos<T> localDetail) {
        if (localDetail == null) {
            return true;
        } else {
            final Page<T> localPage = localDetail.formDetail();
            if ((page.getItems().size() - localPage.getItems().size()) > -5
                && !Constants.GSON.toJson(localPage.getItems()).equals(Constants.GSON.toJson(page.getItems()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public OralTestInfo getAllOralTestInfo(String bookId, String loginld, int assessmentType) throws Exception {
        OralTestInfo oralTestInfo = new OralTestInfo();
        // 根据bookid查找书籍名字以及卷子的数量
        DdbOraltestBook ddbOraltestBook = ddbOralTestBookMapper.getDdbOraltestBook(bookId);
        oralTestInfo.name = ddbOraltestBook.getName();
        oralTestInfo.id = ddbOraltestBook.getBookId();
        // 得到卷子数量
        int num = ddbOraltestBook.getNum();
        // 得到该用户评测过的该本书卷子的数量
        int count = oralTestDetailMapper.getExamCountByLoginIdAndBookId(loginld, bookId, assessmentType);
        // 根据loginld bookid得到一本书全部试卷最新平均得分
        if (count != 0) {
            // 全部卷子总分/卷子数量
            oralTestInfo.averageScore = oralTestDetailMapper.getExamSumByLoginIdAndBookId(loginld, bookId,
                    assessmentType) / count;
        }
        // 卷子列表
        ArrayList<OralTestPaper> oralTestPapers = new ArrayList<>();
        // 去MP包中解析卷子名字
        for (int idx = 1; idx <= num; idx++) {
            CodeInfo codeInfo = null;
            try {
                codeInfo = decodeService.getOralTestCodeInfo(bookId, idx);
                if (codeInfo == null) {
                    continue;
                }
                final String json = FUtils
                        .fileToString(FileUtils.getFileSaveRealPath(codeInfo.languageInfos[0].getVoice(), false));
                final Exam exam = Constants.GSON.fromJson(json, Exam.class);
                OralTestPaper oralTestPaper = new OralTestPaper();
                // 得到卷子名字
                oralTestPaper.name = exam.name;
                // 卷子编号
                oralTestPaper.num = idx;
                // 判断本套卷子是否评测过
                int oralTestPaperCount = oralTestDetailMapper.getOralTestPaperCount(loginld, bookId, idx,
                        assessmentType);
                if (oralTestPaperCount != 0) {
                    // 得到该套卷子的总分
                    oralTestPaper.score = oralTestDetailMapper.getOralTestPaperSum(loginld, bookId, idx,
                            assessmentType);
                }
                oralTestPapers.add(oralTestPaper);
                oralTestInfo.papers = oralTestPapers;
                return oralTestInfo;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
        //屏蔽此处代码，APP端需求改变
        /*final CodeInfo codeInfo = decodeService
            .getCodeInfo(QuickCodeInfo.getSimpleSHQuickCodeInfo(bookId, 0, MpResourceDecoder.ORAL_TEST_CODE));
        if (codeInfo == null) {
            return null;
        }
        final String json = FUtils
            .fileToString(FileUtils.getFileSaveRealPath(codeInfo.languageInfos[0].getVoice(), false));
        return Constants.GSON.fromJson(json, Exam[].class);*/
    }
   
    @Override
    public ExamResult getOralTestInfo(String bookId, int num, DdbPeCustom peCustom ,int assessmentType,List<String> loginds,int type) throws Exception {
        //屏蔽此处代码，利用书籍ID、num以及热区来获取exam.json文件
        /*final CodeInfo codeInfo = decodeService
            .getCodeInfo(QuickCodeInfo.getSimpleSHQuickCodeInfo(bookId, num, MpResourceDecoder.ORAL_TEST_CODE));*/
        final CodeInfo codeInfo=decodeService.getOralTestCodeInfo(bookId, num);
        if (codeInfo == null) {
            return null;
        }
        final String json = FUtils
            .fileToString(FileUtils.getFileSaveRealPath(codeInfo.languageInfos[0].getVoice(), false));
        final Exam exam = Constants.GSON.fromJson(json, Exam.class);
        final ExamResult result = new ExamResult();
        result.userName = StringUtils.isBlank(peCustom.getNickName()) ? peCustom.getTrueName() : peCustom.getNickName();
        result.examName = exam.name;
        result.topic = new ArrayList<>(exam.topic.size());
        for (Topic topic : exam.topic) {
            final TopicResult topicResult = new TopicResult();
            topicResult.title = topic.title;
            topicResult.subTopic = new ArrayList<>(topic.subTopic.size());
            dealTopic(topic.subTopic, bookId, peCustom.getLoginId(), topicResult, result,assessmentType,loginds,type);
            result.topic.add(topicResult);
        }
        return result;
    }

    private void dealTopic(ArrayList<Exam.SubTopic> subTopics, String bookId, String loginId, TopicResult topicResult,
        ExamResult exam,int assessmentType,List<String> loginIds,int type) {
        for (Exam.SubTopic topic : subTopics) {
            final SubTopicResult subTopicResult = new SubTopicResult();
            subTopicResult.question = topic.question;
            subTopicResult.type = topic.type;
            subTopicResult.imageUrl=topic.imageUrl;
            subTopicResult.accentKey=topic.accentKey;
            topicResult.subTopic.add(subTopicResult);
            subTopicResult.refAnswer = topic.refAnswer;
            //TODO 此处代码返回听录音题的mp3的url,app端需求暂时不需要,且此处利用ShCode值来解析MP包，不适用
            /*if (topic.type == 0) {
                CodeInfo codeInfo = null;
                try {
                    codeInfo = decodeService.getCodeInfo(
                        QuickCodeInfo.getSimpleSHQuickCodeInfo(bookId, 1, new ShCode(topic.num, ShCode.OID3_BASE)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (codeInfo == null) {
                    continue;
                }
                subTopicResult.refAnswer.add(codeInfo.languageInfos[0].getVoice());
            }*/
            //TODO 现根据logId,bookId以及小题题号来定位一个评测记录，如果将来确定MP包中增加卷子数量，应添卷子编号来定位评测数据
            OralTestDetail  detail=null;
            switch (type) {
            case Constants.PERSONAL:
                detail = oralTestDetailMapper.get(loginId, bookId, topic.num,assessmentType);
                if (detail == null) {
                    continue;
                }
                subTopicResult.userRecognizeTxt = detail.getRecognizeTxt();
                subTopicResult.userPoints = detail.getScore();
                subTopicResult.fluency = detail.getFluency();
                subTopicResult.integrity = detail.getIntegrity();
                subTopicResult.pronunciation = detail.getPronunciation();
                exam.userTotalPoints += subTopicResult.userPoints;
                break;
            case Constants.COLLECTIVE:
                if (loginIds.size() == 0) {
                    continue;
                }
                detail = oralTestDetailMapper.getDetail(loginIds, bookId, topic.num, assessmentType);
                if (detail == null) {
                    continue;
                }
                // 最高分
                subTopicResult.maxScore = detail.getMaxScore();
                // 最低分
                subTopicResult.minScore = detail.getMinScore();
                // 平均分
                subTopicResult.avgScore = detail.getAvgScore();
                // 平均流利度
                subTopicResult.avgFluency = detail.getAvgFluency();
                // 平均准确度
                subTopicResult.avgIntegrity = detail.getAvgIntegrity();
                // 平均标准度
                subTopicResult.avgPronunciation = detail.getAvgPronunciation();
                // 平均分
                exam.avgTotalPoints += subTopicResult.avgScore;
                break;
            default:
                break;
            }
            subTopicResult.userVoice = FileUtils.getFullRequestPath(detail.getRecordingUrl());
            if (exam.userDate == 0 || detail.getUploadTime().toEpochMilli() > exam.userDate) {
                exam.userDate = detail.getUploadTime().toEpochMilli();
            }
        }
    }

    /**
     * 根据bookId获取口语考试卷子
     */
    @Override
    public List<OralTestPaper> getOralBookContent(String bookId) {
        final DdbOraltestBook ddbOraltestBook = ddbOralTestBookMapper.getDdbOraltestBook(bookId);
        if (ddbOraltestBook == null) {
            return null;
        }
        // 得到卷子数量
        int num = ddbOraltestBook.getNum();
        // 卷子列表
        final ArrayList<OralTestPaper> oralTestPapers = new ArrayList<>();
        for (int idx = 1; idx <= num; idx++) {
            CodeInfo codeInfo = null;
            try {
                codeInfo = decodeService.getOralTestCodeInfo(bookId, idx);
                if (codeInfo == null) {
                    continue;
                }
                final String json = FUtils
                        .fileToString(FileUtils.getFileSaveRealPath(codeInfo.languageInfos[0].getVoice(), false));
                final Exam exam = Constants.GSON.fromJson(json, Exam.class);
                final OralTestPaper oralTestPaper = new OralTestPaper();
                // 得到卷子名字
                oralTestPaper.name = exam.name;
                // 卷子编号
                oralTestPaper.num = idx;
                oralTestPapers.add(oralTestPaper);
                return oralTestPapers;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<DdbResourceBook> getBooksTeachLink() {
        List<DdbResourceBook> books = resourceBookMapper.getBooksTeachLink();
        return books;
    }

    @Override
    public List<FileModule> downloadBookZipEx(String id, boolean needTeachLink, boolean needMpLink, boolean needMppLink)
            throws Exception {
        final DdbResourceBook book = getById(id);
        if (book == null) {
            throw new SdkException(Constants.NO_MACHING_BOOK);
        }
        List<String> list = new ArrayList<>();
        final boolean[] needLink = { needTeachLink, needMpLink, needMppLink, needMppLink /* mpv：需要mpp的时候也需要mpv */ };
        final String[] links = { book.getTeachLink(), book.getBookLink(), book.getMppLink(), book.getMpvLink() };
        final boolean[] failIfNotExist = { true, true, true, false /* mpv 可选 */ };
        boolean isMpvExist = false;
        for (int idx = 0; idx < needLink.length; idx++) {
            if (needLink[idx]) {
                final File file = checkAndCreate(links[idx]);
                if (file != null) {
                    if (idx == (needLink.length - 1)) {
                        isMpvExist = true;
                    }
                    list.add(file.getPath());
                } else if (failIfNotExist[idx]) {
                    return null;
                }
            }
        }
        // 根据下载的包的不同种类,生成对应的包名
        final String realPath = FileUtils.getFileSaveRealPath(FileUtils.BOOK_ZIP_FOLDER);
        final String zipName = book.getName() + "_" + book.getVersion() + (needMppLink ? "_mpp" : "")
                + (isMpvExist ? "_mpv" : "") + (needMpLink ? "_mp" : "") + (needTeachLink ? "_teach" : "") + ".zip";
        if (!new File(realPath + zipName).exists()) {
            FileUtils.zipFile(list, realPath + zipName);
        }
        return fileService.fileDownload(FileUtils.BOOK_ZIP_FOLDER + zipName);
    }

    @Override
    public void updatePageInfoDetail(String bookId, boolean isActive, boolean isSendEmail) throws Exception {
        final ArrayList<PageInfo> pageInfos = updatePageInfos(bookId);
        if (pageInfos.size() < 1) {
            LOGGER.error("saveDetail - PageInfo didn't save to database");
            return;
        }
        Collections.sort(pageInfos);
        final Page<PageInfo> page = new Page<>(pageInfos, pageInfos.size());
        page.setVersion(new ResourceVersion(System.currentTimeMillis(), 0));
        final DdbPageDetail pageDetail = new DdbPageDetail(page);
        pageDetail.setBookId(bookId);
        pageDetail.setIsActive(isActive);
        final DdbPageDetail localPageDetail = pageDetailMapper.get(bookId, true);
        final String subject = "pageInfo更新结果";
        final List<Map<String, Object>> newPageIdList = new ArrayList<>();
        final List<Map<String, Object>> delPageIdList = new ArrayList<>();
        // checkAndComparePageInfo返回结果为Map:key-->couldCreate(是否允许更新). value-->emailContent(邮件内容)
        final EmailResult emailResult = checkAndComparePageInfo(pageDetail, localPageDetail, newPageIdList,
                delPageIdList);
        if (checkDetail(PageInfo.class, page, localPageDetail) && emailResult.couldCreate) {
            pageDetailMapper.save(pageDetail);
            LOGGER.error("saveDetail - PageInfo save to database");
        } else {
            emailResult.emailContent = "没有更新内容~";
            LOGGER.error("saveDetail - PageInfo didn't save to database");
        }
        if (isSendEmail) {
            emailUtil.sendHtmlEmail(subject, emailResult.emailContent);
        }
    }
    
    /**
     * 校验pageInfo是否有更新以及是否符合更新规则
     * @param pageDetail
     * @param localPageDetail
     * @return
     * @throws Exception
     */
    private EmailResult checkAndComparePageInfo(DdbPageDetail pageDetail, DdbPageDetail localPageDetail,
            List<Map<String, Object>> newPageIdList, List<Map<String, Object>> delPageIdList) throws Exception {
        if (localPageDetail == null) {
            return new EmailResult(true, String.format("新增页,版本号=%s<br>书ID=%s", pageDetail.getVersion(), pageDetail.getBookId()));
        }
        // 查询增加的page
        checkPageInfoList(localPageDetail == null ? null : localPageDetail.formDetail(), pageDetail, true,
                newPageIdList, delPageIdList);
        // 查询减少的page
        checkPageInfoList(pageDetail.formDetail(), localPageDetail, false, newPageIdList, delPageIdList);
        // 更新成功,发送邮件:新增或减少的书,发送bookId.更新的书,发送bookId及更新的字段内容
        final List<PageInfo> newPageInfos = pageDetail.formDetail().getItems();
        final List<PageInfo> localPageInfos = localPageDetail.formDetail().getItems();
        final List<Map<String, Map<String, Object>>> resList = new ArrayList<>();
        // 循环遍历,比较相同pageId的bean,得到之间的差异数据
        for (int i = 0; i < newPageInfos.size(); i++) {
            for (int j = 0; j < localPageInfos.size(); j++) {
                if (newPageInfos.get(i).id.equals(localPageInfos.get(j).id)) {
                    final Map<String, Map<String, Object>> list = compare(newPageInfos.get(i), localPageInfos.get(j),
                            true);
                    if (list != null && list.size() > 0) {
                        resList.add(list);
                    }
                }
            }
        }
        // 得到pageId,根据pageId查出bookId,bookName,pageNum
        final List<Map<String, Object>> pageDiffList = new ArrayList<>();
        for (Map<String, Map<String, Object>> map : resList) {
            for (String pageId : map.keySet()) {
                final DdbResourcePageCode ddbResourcePageCode = pageCodeMapper.getPageCodeByPageId(pageId);
                if (null == ddbResourcePageCode) {
                    continue;
                }
                final Map<String, Object> pageDiffMap = new HashMap<>();
                pageDiffMap.put("bookId", ddbResourcePageCode.getFkBookId());
                pageDiffMap.put("pageName", ddbResourcePageCode.getName());
                pageDiffMap.put("different", map.get(pageId));
                pageDiffList.add(pageDiffMap);
            }
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final boolean couldCreate = ((resList != null && resList.size() > 0) || delPageIdList.size() > 0
                || newPageIdList.size() > 0);
        return new EmailResult(couldCreate, String.format("新增的页=%s<br>减少的页=%s<br>更新的页=%s<br>版本号=%s", gson.toJson(newPageIdList),
                gson.toJson(delPageIdList), gson.toJson(pageDiffList), pageDetail.getVersion()));
    }
    
    /**
     * 比较两个bean之间的差异
     * @param obj1 
     * @param Obj2
     * @param skipVersion 是否跳过校验version
     * @return
     * @throws Exception
     */
    private static <T> Map<String, Map<String, Object>> compare(T newObj, T oldObj, boolean skipVersion)
            throws Exception {
        final Field[] fs = newObj.getClass().getDeclaredFields();// 获取所有属性
        final Map<String, Object> diffMap = new HashMap<>();
        final Map<String, Map<String, Object>> resList = new HashMap<>();
        String id = null; // 书id或pageId
        for (Field f : fs) {
            f.setAccessible(true);// 设置访问性，反射类的方法，设置为true就可以访问private修饰的东西，否则无法访问
            final Object v1 = f.get(newObj);
            final Object v2 = f.get(oldObj);
            if (f.getName().equals("id")) { // bookList获取的是bookId,pageInfo获取的是pageId
                id = v1 == null ? "" : v1.toString();
            }
            outer: if (!equals(JSONObject.toJSONString(v1), JSONObject.toJSONString(v2))) {
                if (skipVersion && f.getName().equals("version")) { // pageInfo的version不做比较
                    break outer;
                }
                diffMap.put(f.getName(),
                        "属性值(新,旧):[" + JSONObject.toJSONString(v1) + "," + JSONObject.toJSONString(v2) + "]");
                resList.put(id, diffMap);
            }
        }
        return resList;
    }

    private static boolean equals(Object obj1, Object obj2) {

        if (obj1 == obj2) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        return obj1.equals(obj2);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setIsActive(String version, ManualOp op, String bookId) throws SdkException, CacheException {
        if (null == version || op == null) {
            throw new SdkException(Constants.INVALID_PARAMETER_ERROR);
        }
        if (op == ManualOp.activePageInfo) {
            pageDetailMapper.activate(version, true, bookId);
            pageDetailMapper.inActivateOtherThan(version, bookId);
            // 生成GlobalPageInfo及版本号,存储到数据表
            final List<DdbPageDetail> pageDetailList = pageDetailMapper.getDdbPageDetailList();
            final ArrayList<PageInfo> pArrayList = new ArrayList<>();
            if (null != pageDetailList) {
                for (DdbPageDetail detail : pageDetailList) {
                    final Page<PageInfo> globalPage = detail.formDetail();
                    final ArrayList<PageInfo> globalPageList = (ArrayList<PageInfo>) globalPage.getItems();
                    pArrayList.addAll(globalPageList);
                }
            }
            Collections.sort(pArrayList);
            final Page<PageInfo> globalPage = new Page<>(pArrayList, pArrayList.size());
            globalPage.setVersion(new ResourceVersion(System.currentTimeMillis(), 0));
            final DdbPageDetail pageDetail = new DdbPageDetail(globalPage);
            pageDetail.setBookId(Constants.GLOBAL_BOOKID_KEY);
            pageDetail.setIsActive(true);
            pageDetailMapper.save(pageDetail);
            pageDetailMapper.inActivateOtherThan(pageDetail.getVersion(), Constants.GLOBAL_BOOKID_KEY);
            // 设置缓存版本
            final String key = CommUtil.getCacheKey(Constants.CACHE_PAGEINFO_VERSION_KEY + bookId);
            memCacheService.set(key, version, Constants.DEFAULT_CACHE_EXPIRATION);
            final String globalPageKey = CommUtil.getCacheKey(Constants.CACHE_PAGEINFO_VERSION_KEY);
            memCacheService.set(globalPageKey, globalPage.getVersion(), Constants.DEFAULT_CACHE_EXPIRATION);
        } else if (op == ManualOp.activeBookList) {
            Constants.CACHE_THREAD_POOL.execute(() -> {
                // 设置缓存版本
                try {
                    String cacheKey = CommUtil.getCacheKey(Constants.CACHE_FULLBOOKINFO_VERSION_KEY);
                    memCacheService.set(cacheKey, version, Constants.DEFAULT_CACHE_EXPIRATION);
                    cacheKey = CommUtil.getCacheKey(Constants.CACHE_BOOKINFO_VERSION_KEY);
                    memCacheService.set(cacheKey, version, Constants.DEFAULT_CACHE_EXPIRATION);
                    cacheKey = CommUtil.getCacheKey(Constants.CACHE_STRUCTUREINFO_VERSION_KEY);
                    memCacheService.set(cacheKey, version, Constants.DEFAULT_CACHE_EXPIRATION);
                } catch (CacheException e1) {
                    e1.printStackTrace();
                }
                bookCoreDetailMapper.activate(version, true);
                bookCoreDetailMapper.inActivateOtherThan(version);
                final List<DdbResourceBook> validBooks = resourceBookMapper.getValidBooks();
                validBooks.forEach((book) -> {
                    try {
                        String key = "";
                        key = CommUtil.getCacheKey(Constants.CACHE_BOOKINFO_KEY_PRIFIX + book.getId());
                        memCacheService.delete(key);
                        key = CommUtil.getCacheKey(Constants.CACHE_POINT_NUM_PRIFIX + book.getId());
                        memCacheService.delete(key);
                        key = CommUtil.getCacheKey(Constants.CACHE_SPOKEN_PREFIX + book.getId());
                        memCacheService.delete(key);
                        key = CommUtil.getCacheKey(Constants.CACHE_STUDY_PREFIX + book.getId());
                        resourceBookService.getBookContent(book.getId(), Constants.CACHE_STUDY_PREFIX);
                        resourceBookService.getBookContent(book.getId(), Constants.CACHE_SPOKEN_PREFIX);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
        }
    }
    
    private static class EmailResult {
        public boolean couldCreate; // 是否允许创建
        public String emailContent; // 邮件内容

        public EmailResult(Boolean couldCreate, String emailContent) {
            this.couldCreate = couldCreate;
            this.emailContent = emailContent;
        }
    }

}
