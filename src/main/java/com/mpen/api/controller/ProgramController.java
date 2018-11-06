/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
import com.mpen.api.bean.FileParam;
import com.mpen.api.bean.Program;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.common.Constants.UploadType;
import com.mpen.api.service.FileService;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.service.ProgramService;
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
@RequestMapping(Uris.V1_PROGRAM)
public class ProgramController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProgramController.class);
    @Autowired
    private FileService fileService;
    @Autowired
    private MemCacheService memCacheService;
    @Autowired
    private ProgramService programService;

    /**
     * 语音文件上传接口(语音拜年-->录音结束-->上传)
     * @param fileParam
     * @param request
     * @param response
     * @return
     */
    @PostMapping(Uris.UPLOAD)
    public @ResponseBody Callable<NetworkResult<Object>> upload(final FileParam fileParam,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {  
            @Override
            public NetworkResult<Object> call() throws Exception {
                final String uuid = CommUtil.genRecordKey();
                fileService.uploadFile(fileParam, null, UploadType.BAINIAN_2018);
                return RsHelper.success(uuid);
            }
        };
    }

    /**
     * 获取小程序的二维码
     * @param fileParam
     * @param request
     * @param response
     * @return
     */
    @GetMapping(Uris.CODE)
    public @ResponseBody Callable<NetworkResult<Object>> getCode(final FileParam fileParam,final String reqUrl,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (fileParam.getAction()) {
                case Constants.GET_QR_CODE:
                    return RsHelper.success(programService.getWechatQRcodeEx(request,reqUrl));
                default:
                    final String path = programService.getWeChatQRcode(fileParam);
                    return RsHelper.success(path);
                }   
            }
        };
    }

    /**
     * TODO：不能一次返回全部信息；要按书本，分页，等等细分控制
     * 获取小程序所有的图片,音频,名字等信息
     * @param request
     * @param response
     * @return
     */
    @GetMapping(Uris.MODEL)
    public @ResponseBody Callable<NetworkResult<Object>> getModel(final HttpServletRequest request,
        final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                return RsHelper.success(Constants.programs);
            }
        };
    }

    /**
     * 根据id获取小程序所有的图片,音频,名字等信息(拜年-->试听)
     * @param id
     * @param request
     * @param response
     * @return
     */
    @GetMapping(Uris.LISTEN)
    public @ResponseBody Callable<NetworkResult<Object>> listen(final String id, final HttpServletRequest request,
        final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                Program program = memCacheService.get(id);
                return RsHelper.success(program);
            }
        };
    }

}
