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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mp.shared.common.CodeInfo;
import com.mp.shared.common.HotArea;
import com.mp.shared.common.NetworkResult;
import com.mp.shared.common.QuickCodeInfo;
import com.mpen.api.bean.Book;
import com.mpen.api.bean.FileParam;
import com.mpen.api.bean.Hot;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.common.Constants.UploadType;
import com.mpen.api.service.DecodeService;
import com.mpen.api.service.FileService;
import com.mpen.api.service.PePenService;
import com.mpen.api.service.UserSessionService;
import com.mpen.api.util.FileUtils;
import com.mpen.api.util.LogUtil;

/**
 * TODO 语音资源相关API.
 *
 * @author zyt
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_AUDIOS)
public class AudiosController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AudiosController.class);

    @Autowired
    private DecodeService decodeService;
    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private PePenService pePenService;
    @Autowired
    private FileService fileService;

    /**
     * 点读获取语音信息接口(DIY有声书)
     * 
     */
    @PostMapping("/")
    public @ResponseBody Callable<NetworkResult<Object>> getCodeInfo(@RequestBody final QuickCodeInfo quickCodeInfo,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                NetworkResult<Object> result;
                // 校验点读笔版本信息 版本过低 直接返回错误结果
                if (!pePenService.checkAppVersion(request)) {
                    final CodeInfo codeInfoResult = new CodeInfo();
                    codeInfoResult.languageInfos = new HotArea.LanguageInfo[1];
                    codeInfoResult.languageInfos[0] = new HotArea.LanguageInfo();
                    codeInfoResult.languageInfos[0].soundFile = FileUtils
                        .getFullRequestPath(Constants.UPDATE_PROMPT_VOICE);
                    result = RsHelper.success(codeInfoResult);
                    return result;
                }
                final UserSession user = userSessionService.getUser(request, response);
                final CodeInfo codeInfo = decodeService.getCodeInfo(quickCodeInfo);
                result = RsHelper.success(codeInfo);
                LogUtil.printLog(request, null, user, result);
                return result;
            }
        };
    }

    /**
     * 点读获取视频信息接口.
     * 
     */
    @PostMapping(Uris.VIDEO)
    public @ResponseBody Callable<NetworkResult<Object>> getVideoCodeInfo(@RequestBody final CodeInfo codeInfo,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession user = userSessionService.getUser(request, response);
                NetworkResult<Object> result = null;
                if (!decodeService.getVideo(codeInfo) || codeInfo.isVideo()) {
                    result = RsHelper.success(decodeService.getCodeInfo(codeInfo.quickCodeInfo));
                } else {
                    result = RsHelper.success(codeInfo);
                }
                LogUtil.printLog(request, null, user, result);
                return result;
            }
        };
    }
    
    /**
     * 语音上传(DIY有声书-->发现-->录音-->结束录音)
     * @param fileParam
     * @param request
     * @param response
     * @return
     */
    @PostMapping(Uris.FILE)
    public @ResponseBody Callable<NetworkResult<Object>> uploadFile(final FileParam fileParam,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession user = userSessionService.getUser(request, response);
                return RsHelper.success(fileService.uploadFile(fileParam, user,UploadType.DAILY_PROGRAM));
            }
        };
    }

    /**
     * 小程序请求接口(DIY有声书-->编辑有声书-->恢复默认).
     * 
     */
    @PostMapping(Uris.DEFAULT)
    public @ResponseBody Callable<NetworkResult<Object>> defaultMethod(@RequestBody final Book book,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession user = userSessionService.getUser(request, response);
                int num = book.getPageNum();
                String uuid = book.getBookId();
                final List<Hot> list = Constants.hotAreas.get(book.getId()).get(num - 1);
                for (Hot hotArea : list) {
                    if (hotArea.num == Integer.valueOf(uuid.trim())) {
                        hotArea.map.remove(user.getLoginId());
                    }
                }
                return RsHelper.success(true);
            }
        };
    }

    /**
     * 小程序请求接口(监听页面初次渲染完成,根据id得到一页的所有信息)
     * 
     */
    @GetMapping(Uris.AREA)
    public @ResponseBody Callable<NetworkResult<Object>> getArea(final String id, final HttpServletRequest request,
        final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                return RsHelper.success(Constants.hotAreas.get(id));
            }
        };
    }
}
