/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mp.shared.common.NetworkResult;
import com.mpen.api.bean.Cache;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Constants.CacheType;
import com.mpen.api.common.Constants.ManualOp;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbResourceBook;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.BookCoreDetailMapper;
import com.mpen.api.mapper.PageDetailMapper;
import com.mpen.api.mapper.PeCustomMapper;
import com.mpen.api.mapper.ResourceBookMapper;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.service.RecordUserBookService;
import com.mpen.api.service.ResourceBookService;
import com.mpen.api.service.impl.ResourceBookServiceImpl;
import com.mpen.api.util.CommUtil;

/**
 * TODO 缓存API.
 *
 * @author zyt
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_MEMECHACHE)
public class MemberCacheController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberCacheController.class);

    @Autowired
    private MemCacheService memCacheService;
    @Autowired
    private ResourceBookMapper resourceBookMapper;
    @Autowired
    private PeCustomMapper peCustomMapper;
    @Autowired
    private ResourceBookService resourceBookService;
    @Autowired
    private RecordUserBookService recordUserBookService;

    /**
     * TODO 临时清除缓存接口.之后改为只能从 管理中心发送这个请求
     */
    @GetMapping("/")
    public @ResponseBody Callable<NetworkResult<Object>> sendMessage(final Cache cache, final String memKey,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                if (!Constants.UPDATE_CACHE_SECURE_KEY.equals(memKey) || cache.getKey() == null) {
                    return RsHelper.error(Constants.ACCESS_FORBIDDEN_ERROR_MSG);
                }
                final ManualOp op = ManualOp.valueOf(cache.getKey());
                if (op == null) {
                    return RsHelper.error(Constants.INVALID_PARAMRTER_MESSAGE);
                }
                String key = "";
                switch (op) {
                case book:
                    Constants.CACHE_THREAD_POOL.execute(() -> {
                        try {
                            resourceBookService.updateBookDetail();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    break;
                case allUser:
                    final List<DdbPeCustom> list = peCustomMapper.get();
                    for (DdbPeCustom ddbPeCustom : list) {
                        key = CommUtil.getCacheKey(Constants.CACHE_USERSESSION_KEY_PREFIX + ddbPeCustom.getLoginId());
                        memCacheService.delete(key);
                        key = CommUtil.getCacheKey(Constants.CACHE_USER_STUDY_PREFIX + ddbPeCustom.getLoginId());
                        memCacheService.delete(key);
                    }
                    Constants.CACHE_THREAD_POOL.execute(() -> {
                        list.forEach((custom) -> {
                            try {
                                recordUserBookService.getUserStydyMap(custom.getLoginId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    });
                    break;
                case User:
                    key = CommUtil.getCacheKey(Constants.CACHE_USERSESSION_KEY_PREFIX + cache.getLoginId());
                    memCacheService.delete(key);
                    key = CommUtil.getCacheKey(Constants.CACHE_USER_STUDY_PREFIX + cache.getLoginId());
                    memCacheService.delete(key);
                    break;
                case pen:
                    key = CommUtil.getCacheKey(Constants.CACHE_PENINFO_KEY_PREFIX + cache.getId());
                    memCacheService.delete(key);
                    break;
                case sms:
                    key = CommUtil.getCacheKey(Constants.CACHE_SEND_SMS_KEY);
                    memCacheService.delete(key);
                    break;
                case pageInfo:
                    if (null == cache.getBookId()) {
                        return RsHelper.error(Constants.NO_MACHING_ERROR_MSG);
                    }
                    Constants.CACHE_THREAD_POOL.execute(() -> {
                        try {
                            // 默认不是正式版本,发送邮件
                            resourceBookService.updatePageInfoDetail(cache.getBookId(), false, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    break;
                case activeBookList:
                    resourceBookService.setIsActive(cache.getVersion(), ManualOp.activeBookList, null);
                    break;
                case activePageInfo:
                    resourceBookService.setIsActive(cache.getVersion(), ManualOp.activePageInfo, cache.getBookId());
                    break;
                case initPageInfo:
                    Constants.CACHE_THREAD_POOL.execute(() -> {
                        final List<DdbResourceBook> validBooks = resourceBookMapper.getValidBooks();
                        validBooks.forEach((book) -> {
                            try {
                                // 改版后,初始化pageInfo,默认是正式版本 不发送邮件
                                resourceBookService.updatePageInfoDetail(book.getId(), true, false);
                                resourceBookService.updateLocalCache(CacheType.CACHE_PAGE, book.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        // 设置数据库和redis版本
                        try {
                            resourceBookService.setIsActive("", ManualOp.activePageInfo, null);
                            // 单本bookList加载完,加载全部的pageInfoLit
                            resourceBookService.updateLocalCache(CacheType.CACHE_GLOBAL_PAGE, null);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    });
                    break;
                default:
                    break;
                }
                return RsHelper.success(true);
            }
        };
    }

}
