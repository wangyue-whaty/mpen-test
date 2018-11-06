/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mpen.api.bean.User;
import com.mpen.api.bean.UserInfo;
import com.mpen.api.bean.UserSession;
import com.mpen.api.domain.SsoUser;
import com.mpen.api.exception.SdkException;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SsoUserService接口.
 *
 * @author kai
 *
 */
public interface SsoUserService {
    /**
     * 获取用户信息.
     * 
     */
    UserSession getUserSessionByLoginId(String loginId,HttpServletRequest request) throws SdkException;

    /**
     * 登陆.
     * 
     */
    UserInfo login(User user, HttpServletRequest request, HttpServletResponse response) throws SdkException,
        JsonParseException, JsonMappingException, IOException;
    
    /**
     * 获取用户头像
     * 
     */
    SsoUser getUserPhoto(String loginId,HttpServletRequest request) throws SdkException ;
}
