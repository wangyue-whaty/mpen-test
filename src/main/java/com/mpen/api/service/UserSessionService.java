/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import com.mpen.api.bean.UserSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserSessionService {
    /**
     * 获取用户信息.
     * 
     */
    UserSession getUser(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
