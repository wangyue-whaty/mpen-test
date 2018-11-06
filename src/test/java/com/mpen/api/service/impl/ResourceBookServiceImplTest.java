/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mp.shared.common.BookInfo;
import com.mp.shared.common.Page;
import com.mp.shared.common.PageInfo;
import com.mp.shared.common.ResourceVersion;
import com.mpen.TestBase;

import com.mpen.api.bean.Unit;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Constants.CacheType;
import com.mpen.api.common.Constants.ManualOp;
import com.mpen.api.domain.DdbBookCoreDetail;
import com.mpen.api.domain.DdbPageDetail;
import com.mpen.api.domain.DdbResourceBook;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.BookCoreDetailMapper;
import com.mpen.api.mapper.PageDetailMapper;
import com.mpen.api.mapper.ResourceBookMapper;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.service.ResourceBookService;
import com.mpen.api.util.CommUtil;

/**
 * ResourceBookServiceImplTest.
 * 
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ResourceBookServiceImplTest extends TestBase{

    @InjectMocks
    @Autowired
    ResourceBookService resourceBookService;
    @Autowired
    ResourceBookMapper resourceBookMapper;
    @Autowired
    BookCoreDetailMapper bookCoreDetailMapper;
    @Autowired
    MemCacheService memCacheService;
    @Autowired
    PageDetailMapper pageDetailMapper;
    
    private static final String version = "ResVer_1537864925602_0";
    private static final String bookId ="ff8080815dc4de93015dcb3ea0ed0055";

    @BeforeClass
    public static void setUp() throws Exception {
        MockitoAnnotations.initMocks(ResourceBookServiceImplTest.class);
    }

    @Test
    public void testGetBookContentSuccess() throws SdkException, CacheException {
        final List<Unit> bookContent = resourceBookService.getBookContent("ff808081581deb4101581e74ac7d0088",
            "13661309890");
        Assert.assertEquals(bookContent.size() > 0, true);
    }

    @Test
    public void testGetBookInfoSuccess() throws Exception {
        final BookInfo bookInfo = resourceBookService.getBookInfo("ff808081581deb4101581e74ac7d0088");
        Assert.assertEquals(bookInfo != null, true);
    }

    /**
     * 测试booklist更新,步骤:
     * 1.查询缓存中booklist版本信息
     * 2.给Constants.bookInfos赋值
     * 3.获取到bookList信息
     * 4.执行更新bookList操作
     * 5.再次获取到bookList信息,没有实际内容更新，校验各个版本号和内容都不变
     * 6.删除一本书,使bookList能更新,之后再恢复
     * 7.再次执行更新bookList操作(应该看邮件内容查看差异对比,查看邮箱中版本号是否与当前version不同)
     * 8.假设更新成功,设置为正式版本
     * 9.查看booklist内容是否改变
     * 10.查看缓存是否改变
     * 11.将删除的书再创建
     * 
     * @throws Exception
     */
    @Test
    public void testUpdateBookList() throws Exception {
        // 查询缓存中booklist版本信息
        final String key = CommUtil.getCacheKey(Constants.CACHE_BOOKINFO_VERSION_KEY);
        ResourceVersion currentVersion = null;
        try {
            currentVersion = memCacheService.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 存储缓存信息到本地
        DdbBookCoreDetail ddbBookCoreDetail = bookCoreDetailMapper.get(true);
        Constants.bookInfos = ddbBookCoreDetail.formDetail();
        // 获取到bookList信息
        Page<BookInfo> page = resourceBookService.getCacheInfos(BookInfo.class, null, CacheType.CACHE_BOOK, null);
        // 执行更新bookList操作
        resourceBookService.updateBookDetail();
        // 再次获取到bookList信息,没有实际内容更新，校验各个版本号和内容都不变
        Page<BookInfo> page1 = resourceBookService.getCacheInfos(BookInfo.class, null, CacheType.CACHE_BOOK, null);
        Assert.assertEquals(Constants.GSON.toJson(page).equals(Constants.GSON.toJson(page1)), true);
        // 删除一本书,使bookList能更新,之后再恢复
        DdbResourceBook book = resourceBookMapper.getByName("丽声拼读故事会教学版学生包第一级-小虫鲍勃");
        resourceBookMapper.delete(book.getId());
        // 再次执行更新bookList操作(应该看邮件内容查看差异对比,查看邮箱中版本号是否与当前version不同)
        resourceBookService.updateBookDetail();
        // 假设更新成功,从数据库中查询出最新version
        DdbBookCoreDetail ddbBookCoreDetail1 = bookCoreDetailMapper.get(false);
        resourceBookService.setIsActive(ddbBookCoreDetail1.getVersion(), ManualOp.activeBookList, null);
        // 再次获取到bookList信息,有实际内容更新，校验各个版本号和内容
        Page<BookInfo> page2 = resourceBookService.getCacheInfos(BookInfo.class, null, CacheType.CACHE_BOOK, null);
        Assert.assertNotEquals(Constants.GSON.toJson(page2).equals(Constants.GSON.toJson(page)), true);
        // 查看缓存是否改变
        ResourceVersion newVersion = null;
        try {
            newVersion = memCacheService.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(Constants.GSON.toJson(newVersion).equals(Constants.GSON.toJson(currentVersion)), false);
        // 将删除的书再创建
        resourceBookMapper.create(book);
        // 再次执行更新bookList操作
        resourceBookService.updateBookDetail();
        // 假设更新成功,从数据库中查询出最新version
        DdbBookCoreDetail ddbBookCoreDetail2 = bookCoreDetailMapper.get(false);
        resourceBookService.setIsActive(ddbBookCoreDetail2.getVersion(), ManualOp.activeBookList, null);
    }
    
    /**
     * 更新pageInfo步骤
     * 1.得到这本书的缓存信息
     * 2.先获取到这本书的pageInfo
     * 3.执行更新pageInfo操作
     * 4.再获取到这本书的pageInfo,没有改动,校验各个版本号和内容都不变
     * 5.查询出最新的version信息(刚刚生成的)
     * 6.标记为正式版本
     * 7.再获取到这本书的pageInfo,有改动(数据库之前不存在这本书的pageInfo信息),校验版本号和内容
     * 8. 查看缓存是否改变
     * 9.校验Constants.pageInfos中内容是否发生改变
     * @throws Exception
     */
    @Test
    public void testUpdatePageInfo() throws Exception {
        Page<PageInfo> localPage = pageDetailMapper.get(bookId, false).formDetail();
        Constants.pageInfoList.put(bookId, localPage);
        // 得到这本书的缓存信息
        final String key = CommUtil.getCacheKey(Constants.CACHE_PAGEINFO_VERSION_KEY + bookId);
        ResourceVersion currentVersion = null;
        try {
            currentVersion = memCacheService.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 先获取到这本书的pageInfo
        Page<PageInfo> page = resourceBookService.getCacheInfos(PageInfo.class, bookId, CacheType.CACHE_PAGE, null);
        // 执行更新pageInfo操作
        resourceBookService.updatePageInfoDetail(bookId, false, true);
        // 再获取到这本书的pageInfo,没有改动,校验各个版本号和内容都不变
        Page<PageInfo> page1 = resourceBookService.getCacheInfos(PageInfo.class, bookId, CacheType.CACHE_PAGE, null);
        Assert.assertEquals(Constants.GSON.toJson(page.getItems()).equals(Constants.GSON.toJson(page1.getItems())), true);
        // 查询出最新的version信息(刚刚生成的)
        DdbPageDetail detail = pageDetailMapper.get(bookId, false);
        // 标记为正式版本
        resourceBookService.setIsActive(detail.getVersion(), ManualOp.activePageInfo, bookId);
        // 再获取到这本书的pageInfo,有改动(数据库之前不存在这本书的pageInfo信息),校验版本号和内容
        Page<PageInfo> page2 = resourceBookService.getCacheInfos(PageInfo.class, bookId, CacheType.CACHE_PAGE, null);
        Assert.assertEquals(Constants.GSON.toJson(page).equals(Constants.GSON.toJson(page2)), false);
        // 查看缓存是否改变
        ResourceVersion newVersion = null;
        try {
            newVersion = memCacheService.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(Constants.GSON.toJson(newVersion).equals(Constants.GSON.toJson(currentVersion)), false);
        // 校验Constants.pageInfos中是否存在刚生成的pageInfo
        Page<PageInfo> pageInfos = Constants.pageInfos;
        Assert.assertEquals(Constants.GSON.toJson(pageInfos).equals(Constants.GSON.toJson(localPage)), false);
    }
    
}
