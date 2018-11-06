/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mp.shared.common.NetworkResult;
import com.mpen.api.bean.Audio;
import com.mpen.api.bean.Pen;
import com.mpen.api.bean.PenInfo;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.domain.DdbQuestionItem;
import com.mpen.api.mapper.QuestionItemMapper;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.service.PePenService;
import com.mpen.api.service.ResourceBookService;
import com.mpen.api.service.SerialNumberService;
import com.mpen.api.util.FileUtils;
import com.mpen.api.util.RSAUtils;

/**
 * TODO 出厂测试相关API.
 *
 * @author zyt
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_QA)
public class QualityController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QualityController.class);
    @Autowired
    private PePenService pePenSrvice;
    @Autowired
    private ResourceBookService ddbResourceBookService;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private QuestionItemMapper questionItemMapper;
    @Autowired
    private MemCacheService memCacheService;
    @Autowired
    private PePenService pePenService;

    /**
     * pensPenId相关，笔出厂保存笔信息接口.
     * 
     */
    @PostMapping(Uris.PENS_PENID)
    public @ResponseBody Callable<NetworkResult<Object>> savePen(@PathVariable final String penId, final String key,
        @RequestBody final PenInfo penInfo, final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (penInfo.getAction()) {
                case Constants.PASSQA:
                    // 笔出厂保存笔信息.
                    if (StringUtils.isBlank(penId) || StringUtils.isBlank(penInfo.getMacAddress())
                        || StringUtils.isBlank(penInfo.getCode()) || StringUtils.isBlank(penInfo.getPublicKey())) {
                        LOGGER.error(Constants.INVALID_PARAMETER_ERROR, request.getRequestURL());
                        return RsHelper.error(Constants.BAD_REQUEST_ERROR_CODE, Constants.INVALID_PARAMRTER_MESSAGE);
                    }
                    pePenSrvice.savePen(penId, penInfo);
                    return RsHelper.success(RSAUtils.encrypt(penInfo.getPublicKey(), Constants.BOOK_KEY));
                //TODO: 更好的验证和加强对密钥等信息访问的安全性
                case Constants.FILE_RSA_KEY:
                    return RsHelper.success(pePenSrvice.getFilesRSAKey(penId, penInfo, key));
                case Constants.GET_CMD:
                    return RsHelper.success(pePenService.getPenCmd(penId, penInfo.getId(), penInfo.getResult()));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }

    /**
     * 保存笔序列号接口.
     * 
     */
    @PostMapping(Uris.PENS_SERIAL_NUM)
    public @ResponseBody Callable<NetworkResult<Object>> saveSerialNumber(@RequestBody final Pen pen,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (pen.getAction()) {
                case Constants.UPLOAD_SERIAL_NUMBER:
                    if (StringUtils.isBlank(pen.getIdentifiaction()) || StringUtils.isBlank(pen.getSerialNumber())) {
                        LOGGER.error(Constants.INVALID_PARAMETER_ERROR, request.getRequestURL());
                        return RsHelper.error(Constants.BAD_REQUEST_ERROR_CODE, Constants.INVALID_PARAMRTER_MESSAGE);
                    }
                    return RsHelper.success(serialNumberService.saveRelationship(pen));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }

    /**
     * books相关,获取笔出厂预下载书籍信息接口.
     * 
     */
    @GetMapping(Uris.BOOKS)
    public @ResponseBody Callable<NetworkResult<Object>> getBooksInfo(final String action,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (action) {
                case Constants.PREDOWNLOAD:
                    // 获取笔出厂预下载书籍信息.
                    return RsHelper.success(ddbResourceBookService.getPrepownloadBooks());
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }

    /**
     * audios相关，出厂测试声音接口.
     * 
     */
    @GetMapping(Uris.AUDIOS)
    public @ResponseBody Callable<NetworkResult<Object>> getAudios(final String action,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (action) {
                case Constants.AUDIOS_TEST:
                    final List<Audio> list = new ArrayList<Audio>();
                    Audio audio = new Audio();
                    audio.setName(Constants.AUDIOS1_NAME);
                    audio.setPath(Constants.AUDIOS1_PATH);
                    list.add(audio);
                    audio = new Audio();
                    audio.setName(Constants.AUDIOS2_NAME);
                    audio.setPath(Constants.AUDIOS2_PATH);
                    list.add(audio);
                    audio = new Audio();
                    audio.setName(Constants.AUDIOS3_NAME);
                    audio.setPath(Constants.AUDIOS3_PATH);
                    list.add(audio);
                    audio = new Audio();
                    audio.setName(Constants.AUDIOS4_NAME);
                    audio.setPath(Constants.AUDIOS4_PATH);
                    list.add(audio);
                    audio = new Audio();
                    audio.setName(Constants.AUDIOS5_NAME);
                    audio.setPath(Constants.AUDIOS5_PATH);
                    list.add(audio);
                    audio = new Audio();
                    audio.setName(Constants.AUDIOS6_NAME);
                    audio.setPath(Constants.AUDIOS6_PATH);
                    list.add(audio);
                    audio = new Audio();
                    audio.setName(Constants.AUDIOS7_NAME);
                    audio.setPath(Constants.AUDIOS7_PATH);
                    list.add(audio);
                    audio = new Audio();
                    audio.setName(Constants.AUDIOS8_NAME);
                    audio.setPath(Constants.AUDIOS8_PATH);
                    list.add(audio);
                    audio = new Audio();
                    audio.setName(Constants.AUDIOS9_NAME);
                    audio.setPath(Constants.AUDIOS9_PATH);
                    list.add(audio);
                    return RsHelper.success(list);
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }

    /**
     * 服务器监控接口.
     * 
     */
    @GetMapping(Uris.CHECK_SERVER)
    public @ResponseBody Callable<NetworkResult<Object>> checkServer(final HttpServletRequest request,
        final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final List<DdbQuestionItem> items = questionItemMapper.get();
                if (items == null || items.size() <= 0) {
                    throw new Exception("JDBC is bad! ");
                }
                final String key = "checkCache";
                final long value = Instant.now().toEpochMilli();
                memCacheService.set(key, value, Constants.DEFAULT_CACHE_EXPIRATION);
                if (value != (Long) memCacheService.get(key)) {
                    throw new Exception("Cache is bad! ");
                }
                final File file = new File(
                    FileUtils.getFileSaveRealPath("/incoming/course/video/instructions.mp4", false));
                if (!file.exists()) {
                    throw new Exception("File is bad! ");
                }
                return RsHelper.success(true);
            }
        };
    }
}
