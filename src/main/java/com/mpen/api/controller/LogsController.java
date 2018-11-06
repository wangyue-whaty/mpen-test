/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

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

import com.mp.shared.common.NetworkResult;
import com.mp.shared.common.SuccessResult;
import com.mp.shared.record.ActionRecords;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.service.ActionRecordService;
import com.mpen.api.service.DataAnalysisService;
import com.mpen.api.service.SsoUserService;
import com.mpen.api.service.UserSessionService;
import com.mpen.api.util.CommUtil;

/**
 * TODO 日志收集相关API.
 *
 * @author zyt
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_LOGS)
public class LogsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogsController.class);
    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private ActionRecordService actionRecordService;
    @Autowired
    private DataAnalysisService dataAnalysisService;
    @Autowired
    private SsoUserService ssoUserService;

    /**
     * 日志收集接口.
     * 
     */
    @PostMapping("/")
    public @ResponseBody Callable<NetworkResult<Object>> saveRecords(@RequestBody final ActionRecords actionRecords,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession user = ssoUserService.getUserSessionByLoginId(CommUtil.getLoginId(request),request);
                final boolean temp = actionRecordService.save(actionRecords, user, request) >= 0;
                final SuccessResult result = new SuccessResult();
                result.setSuccess(temp);
                result.setUuid(actionRecords.getUploadUuid());
                return RsHelper.success(result);
            }
        };
    }

    @GetMapping(Uris.DATA_ANALYSIS)
    public @ResponseBody Callable<NetworkResult<Object>> getDataAnalysisResult(final HttpServletRequest request,
        final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                return RsHelper.success(dataAnalysisService.getDataAnalysisResult());
            }
        };
    }
}
