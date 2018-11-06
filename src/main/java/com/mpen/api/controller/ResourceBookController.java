/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

import com.mp.shared.common.BookInfo;
import com.mp.shared.common.LearnWordStructureInfo;
import com.mp.shared.common.NetworkResult;
import com.mp.shared.common.PageInfo;
import com.mp.shared.common.ResourceVersion;
import com.mpen.api.bean.Book;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Constants.CacheType;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.service.PePenService;
import com.mpen.api.service.ResourceBookService;
import com.mpen.api.service.UserSessionService;
import com.mpen.api.util.LogUtil;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO 课程相关API.
 *
 * @author kai
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_BOOKS)
public class ResourceBookController {

    @Autowired
    private ResourceBookService resourceBookService;
    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private PePenService pePenService;

    /**
     * 获取可用课程.
     *
     * @return Result对象
     */
    @GetMapping("/")
    public @ResponseBody Callable<NetworkResult<Object>> getBookInfo(final Book book, final HttpServletRequest request,
        final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                UserSession user = userSessionService.getUser(request, response);
                if (!pePenService.checkAppVersion(request)) {
                    return RsHelper.success(null);
                }
                NetworkResult<Object> result = null;
                switch (book.getAction()) {
                case Constants.GET_VALID_BOOKS:
                    result = RsHelper.success(resourceBookService.getCacheInfos(BookInfo.class, null,
                            CacheType.CACHE_BOOK, ResourceVersion.fromString(book.getVersion())));
                    break;
                case Constants.GET_BOOK_PAGES:
                    result = RsHelper.success(resourceBookService.getCacheInfos(PageInfo.class, book.getBookId(),
                            CacheType.CACHE_PAGE, ResourceVersion.fromString(book.getVersion())));
                    break;
                case Constants.GET_LEARNWORD_STRUCTUREINFO:
                    result = RsHelper.success(resourceBookService.getCacheInfos(LearnWordStructureInfo.class, null,
                            CacheType.CACHE_LEARN_WORD_STRUCTURE_INFOS, ResourceVersion.fromString(book.getVersion())));
                    break;
                default:
                    result = RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                    break;
                }
                LogUtil.printLog(request, book.getAction(), user, result);
                return result;
            }
        };
    }

    /**
     * 获取可用课程.
     *
     * @return Result对象
     */
    @GetMapping(Uris.ORAL_TEST)
    public @ResponseBody Callable<NetworkResult<Object>> getBookOralTestInfo(final Book book,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (book.getAction()) {
                case Constants.GET_ALL_ORALTEST_INFO:
                    final UserSession user01 = userSessionService.getUser(request, response);
                    return RsHelper.success(resourceBookService.getAllOralTestInfo(book.getBookId(),user01.getLoginId(),book.getAssessmentType()));
                case Constants.GET_ORALTEST_INFO:
                    final UserSession user = userSessionService.getUser(request, response);
                    return RsHelper.success(
                        resourceBookService.getOralTestInfo(book.getBookId(), book.getPageNum(), user.getPeCustom(),book.getAssessmentType(),null,Constants.PERSONAL));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
}
