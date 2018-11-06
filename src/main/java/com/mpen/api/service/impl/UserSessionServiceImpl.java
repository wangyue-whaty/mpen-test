/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.service.SsoUserService;
import com.mpen.api.service.UserSessionService;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * UserSessionService服务.
 *
 * @author zyt
 *
 */
@Component
public class UserSessionServiceImpl implements UserSessionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserSessionServiceImpl.class);
    @Autowired
    private SsoUserService ssoUserService;

    /**
     * 获取UserSession.
     * 
     */
    public UserSession getUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Object loginIdObj = request.getAttribute(Constants.LOGINIDKEY);
        if (loginIdObj == null) {
            return null;
        }

        final String loginId = loginIdObj.toString();
        final UserSession user = ssoUserService.getUserSessionByLoginId(loginId,request);
        return user;
    }

}
