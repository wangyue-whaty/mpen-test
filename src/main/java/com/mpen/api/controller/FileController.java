/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

import java.io.File;
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
import org.springframework.web.servlet.ModelAndView;

import com.mp.shared.common.NetworkResult;
import com.mp.shared.common.SuccessResult;
import com.mpen.api.bean.FileParam;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.service.FileService;
import com.mpen.api.service.PePenService;
import com.mpen.api.service.RecordUserBookService;
import com.mpen.api.service.ResourceBookService;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.FileUtils;

/**
 * TODO 文件资源相关API.
 *
 * @author zyt
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_FILES)
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;
    @Autowired
    private PePenService pePenService;
    @Autowired
    private ResourceBookService resourceBookService;
    @Autowired
    private RecordUserBookService recordUserBookService;

    /**
     * 上传文件接口.
     * 
     */
    @PostMapping(Uris.UPLOAD)
    public @ResponseBody Callable<NetworkResult<Object>> uploadFile(final FileParam fileParam,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final SuccessResult result = new SuccessResult();
                String fileSavePath = null;
                switch (fileParam.getAction()) {
                case Constants.UPLOAD_CMD_FILE:
                    fileSavePath = fileService.saveFile(fileParam.getFile(), FileUtils.CMD_FILE, null);
                    result.setSuccess(
                        pePenService.updateCmdUrl(fileParam.getUuid(), fileSavePath, fileParam.getDescription()));
                    break;
                case Constants.UPLOAD_ORALTEST_RECORDING:
                    fileSavePath = fileService.saveFile(fileParam.getFile(), FileUtils.RECORDING, null);
                    result.setSuccess(recordUserBookService.saveOralTest(request, fileSavePath, fileParam));
                    break;
                case Constants.UPLOAD_PERSISTENCE_FILE:
                    final String address = CommUtil.getIpAddr(request);
                    result.setSuccess(fileService.insertFile(fileParam, address));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
                return RsHelper.success(result);
            }
        };
    }
    // TODO 要删除 替换为downloadFilesEx
    @GetMapping(Uris.DOWNLOAD_FILES)
    public @ResponseBody Callable<NetworkResult<Object>> downloadFiles(final FileParam fileParam,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                // TODO 兼容老的文件切分下载，避免action为null抛出异常
                if (Constants.DOWNLOAD_BOOK.equals(fileParam.getAction())) {
                    return RsHelper.success(resourceBookService.downloadBook(fileParam.getUuid()));
                } else if (Constants.DOWNLOAD_BOOK_ZIP.equals(fileParam.getAction())) {
                    return RsHelper.success(resourceBookService.downloadBookZipEx(fileParam.getUuid(), false, true, true));
                }
                return RsHelper.success(fileService.fileDownload(fileParam.getFilePath()));
            }
        };
    }
    
    @GetMapping(Uris.DOWNLOAD_FILES_EX)
    public @ResponseBody Callable<NetworkResult<Object>> downloadFilesEx(final FileParam fileParam,
        final HttpServletRequest request, final HttpServletResponse response, final boolean needTeachLink,
        final boolean needMpLink, final boolean needMppLink) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                // TODO 兼容老的文件切分下载，避免action为null抛出异常
                if (Constants.DOWNLOAD_BOOK_ZIP.equals(fileParam.getAction())) {
                    return RsHelper.success(resourceBookService.downloadBookZipEx(
                    		fileParam.getUuid(), needTeachLink, needMpLink, needMppLink));
                } 
                return RsHelper.success(fileService.fileDownload(fileParam.getFilePath()));
            }
        };
    }
    

    @GetMapping(Uris.DOWNLOAD)
    public @ResponseBody Callable<ModelAndView> downloadFile(final FileParam fileParam,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<ModelAndView>() {
            @Override
            public ModelAndView call() throws Exception {
                final ModelAndView mv = new ModelAndView("byteRangeViewRender");
                mv.addObject("file", new File(FileUtils.getFileSaveRealPath(fileParam.getFilePath())));
                mv.addObject("contentType", "application/octet-stream");
                return mv;
            }
        };
    }
}
