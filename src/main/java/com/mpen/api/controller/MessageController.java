/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

import com.mp.shared.common.NetworkResult;
import com.mpen.api.bean.Message;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.service.PushRecordService;
import com.mpen.api.service.UserSessionService;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO 消息相关API.
 *
 * @author zyt
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_MESSAGES)
public class MessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private PushRecordService pushRecordService;
    @Autowired
    private UserSessionService userSessionService;

    /**
     * 笔触发事件向手机推送消息接口.
     * 
     */
    @PostMapping("/")
    public @ResponseBody Callable<NetworkResult<Object>> sendMessage(@RequestBody final Message message,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession user = userSessionService.getUser(request, response);
                if (Constants.PUSH_APP.equals(message.getAction())) {
                    return RsHelper.success(pushRecordService.save(message, user));
                }
                return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
            }
        };

    }
}
