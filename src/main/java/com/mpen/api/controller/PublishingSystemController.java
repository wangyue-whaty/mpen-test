package com.mpen.api.controller;

import java.io.File;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mp.shared.common.NetworkResult;
import com.mp.shared.common.PageInfo;
import com.mp.shared.utils.FUtils;
import com.mpen.api.bean.Book;
import com.mpen.api.bean.FileParam;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Constants.CacheType;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.service.FileService;
import com.mpen.api.service.ResourceBookService;
import com.mpen.api.util.FileUtils;

/**
 * TODO 出版系统相关API.
 *
 * @author zyt
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_PUBLISHING)
public class PublishingSystemController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublishingSystemController.class);

    @Autowired
    private ResourceBookService resourceBookService;
    @Autowired
    private FileService fileService;

    /**
     * TODO 创建资源接口（action:createBook），生成点读码接口（action:createCode），查看生成进度几口（action:getProgress）
     *
     * @param book
     *        书籍资源参数bean
     * @param request
     *        HttpServletRequest
     * @param response
     *        HttpServletResponse
     * @return
     *        NetworkResult
     */
    @PostMapping(Uris.BOOK)
    public @ResponseBody Callable<NetworkResult<Object>> createTif(@RequestBody final Book book,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (book.getAction()) {
                case Constants.CREATE_BOOK:
                    return RsHelper.success(resourceBookService.addBook(book));
                case Constants.CREATE_CODE:
                    return RsHelper.success(resourceBookService.createCode(book));
                case Constants.GET_PROGRESS:
                    return RsHelper.success(resourceBookService.getProgress(book));
                case Constants.GET_PAGEINFO:
                    return RsHelper.success(
                            resourceBookService.getCacheInfos(PageInfo.class, book.getBookId(), CacheType.CACHE_PAGE, null));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }

    /**
     * TODO 书籍MP包上传接口（action:uploadBook）
     * 
     * @param fileParam
     *        文件参数bean
     * @param request
     *        HttpServletRequest
     * @param response
     *        HttpServletResponse
     * @return
     *        NetworkResult
     */
    @PostMapping(Uris.FILE)
    public Callable<NetworkResult<Object>> uploadFile(final FileParam fileParam, final String publisher,
            final String loginId, final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                if (StringUtils.isBlank(loginId) || StringUtils.isBlank(publisher) || null == fileParam) {
                    return RsHelper.error(Constants.BAD_REQUEST_ERROR_CODE, Constants.INVALID_PARAMRTER_MESSAGE);
                }
                // 校验文件的md5值
                final String reqfileMD5 = FUtils.getMd5(fileParam.getFile());
                if (!reqfileMD5.equals(fileParam.getMd5())) {
                    return RsHelper.error(Constants.BAD_REQUEST_ERROR_CODE, Constants.INVALID_PARAMRTER_MESSAGE);
                }
                switch (fileParam.getAction()) {
                case Constants.UPLOAD_BOOK:
                    // 路径:/imcoming/course/出版社/用户ID/bookID/资源种类(MP|MPP|MPV)
                    final StringBuilder sBuilder = new StringBuilder();
                    sBuilder.append(FileUtils.SOURCE_PATH); // 根路径
                    sBuilder.append(publisher); // 出版社
                    sBuilder.append("/");
                    sBuilder.append(loginId); // 用户ID
                    sBuilder.append("/");
                    sBuilder.append(fileParam.getUuid()); // bookID
                    sBuilder.append("/");
                    final String filePath = fileService.saveFile(fileParam.getFile(), null, sBuilder.toString());
                    return RsHelper.success(resourceBookService.updateBookLink(fileParam, filePath, reqfileMD5));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
}
