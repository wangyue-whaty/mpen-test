package com.mpen.api.controller;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mp.shared.common.NetworkResult;
import com.mpen.api.bean.UserMessage;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.service.DdbUserMessageService;
import com.mpen.api.service.UserSessionService;

/**
 * 用户消息表 前端控制器 涉及：APp2.0消息模块
 * 
 * @since 2018-08-21
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_USERMESSAGE)
public class DdbUserMessageController {
    @Autowired
    private DdbUserMessageService ddbUserMessageService;
    @Autowired
    private UserSessionService userSessionService;

    @PostMapping(Uris.PUSH_MESSAGE)
    public @ResponseBody Callable<NetworkResult<Object>> uploadFile(@RequestBody final UserMessage userMessage,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (userMessage.getAction()) {
                // 推送系统消息(全部用户)
                case Constants.PUSH_SYSTEM_MESSAGE:
                    return RsHelper.success(ddbUserMessageService.pushSystemMessage(userMessage.getType(),
                            userMessage.getContent(), userMessage.getMsg()));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }

    @PostMapping(Uris.LIST_USER_MESSAGE)
    public @ResponseBody Callable<NetworkResult<Object>> getUserMessage(@RequestBody UserMessage userMessage,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {

            @Override
            public NetworkResult<Object> call() throws Exception {
                // 获取登录用户信息
                final UserSession user = userSessionService.getUser(request, response);
                NetworkResult<Object> networkResult = null;
                switch (userMessage.getAction()) {
                // 消息列表
                case Constants.LIST_USER_MESSAGE:
                    userMessage.setLoginId(user.getLoginId());
                    networkResult = RsHelper.success(ddbUserMessageService.UserMessagesOfPage(userMessage));
                    break;
                // 获取一条消息
                case Constants.ONE_USER_MESSAGE:
                    userMessage.setLoginId(user.getLoginId());
                    networkResult = RsHelper.success(ddbUserMessageService.OneOfUserMessages(userMessage));
                    break;
                // 删除一条消息
                case Constants.DELETE_USER_MESSAGE:
                    userMessage.setLoginId(user.getLoginId());
                    networkResult = RsHelper.success(ddbUserMessageService.deleteUserMessage(userMessage));
                    break;
                default:
                    networkResult = RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                    break;
                }

                return networkResult;
            }
        };

    }
}