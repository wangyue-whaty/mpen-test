/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpen.api.bean.Token;
import com.mpen.api.bean.User;
import com.mpen.api.bean.UserInfo;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Constants.PenType;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbPeLabel;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.domain.DdbPrPenCustom;
import com.mpen.api.domain.SsoUser;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.PeCustomMapper;
import com.mpen.api.mapper.PeLabelMapper;
import com.mpen.api.mapper.PePenMapper;
import com.mpen.api.mapper.PrPenCustomMapper;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.service.PeCustomService;
import com.mpen.api.service.SsoUserService;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.FileUtils;

/**
 * SsoUserService服务.
 *
 * @author kai
 *
 */
@Component
public class SsoUserServiceImpl implements SsoUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SsoUserServiceImpl.class);

    @Autowired
    private PeCustomMapper peCustomMapper;
    @Autowired
    private PrPenCustomMapper prPenCustomMapper;
    @Autowired
    private PePenMapper pePenMapper;
    @Autowired
    private MemCacheService memCacheService;
    @Autowired
    private PeCustomService peCustomService;
    @Autowired
    private PeLabelMapper peLabelMapper;

    /**
     * 获取usersession.
     * 
     * @return UserSession
     * @throws SdkException
     *             SDK异常
     */
    @Override
    public UserSession getUserSessionByLoginId(String loginId,HttpServletRequest request) throws SdkException {

        if (StringUtils.isEmpty(loginId)) {
            return null;
        }

        final String key = CommUtil.getCacheKey(Constants.CACHE_USERSESSION_KEY_PREFIX + loginId);
        UserSession us = null;
        try {
            us = (UserSession) this.memCacheService.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (us == null) {
                final String loginIdAfterDecode = URLDecoder.decode(loginId, "UTF-8");
                final DdbPeCustom peCustom = this.peCustomService.getByLoginId(loginIdAfterDecode);
                //请求用户中心获取用户信息
                SsoUser ssoUser = new SsoUser();
                String authorization = request.getHeader("Authorization");
                if (authorization == null) {
                    ssoUser = CommUtil.getUserInfo(request);
                }else {
                    String url = Constants.NEW_USERCENTER_USER+this.memCacheService.get(authorization);
                    ssoUser = CommUtil.getUserInfoV2(url, authorization);
                }
                
                if(ssoUser == null) {
                    throw new SdkException("获取用户信息失败!");
                }
                
                us = new UserSession();
                us.setSsoUser(ssoUser);
                us.setPeCustom(peCustom);
                us.setId(ssoUser.getId());
                us.setLoginId(ssoUser.getLoginId());
                try {
                    this.memCacheService.set(key, us, Constants.DEFAULT_CACHE_EXPIRATION);
                } catch (CacheException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取用户信息错误！", e);
            throw new SdkException("用户名不存在！");
        }

        return us;
    }
    
    /**
     * 获取usersession.
     * 
     * @return UserSession
     * @throws SdkException
     *             SDK异常
     */
    private UserSession getUserSessionByLoginIdExt(SsoUser ssoUser) throws SdkException {
    	String loginId = ssoUser.getLoginId();
        if (StringUtils.isEmpty(loginId)) {
            return null;
        }
        final String key = CommUtil.getCacheKey(Constants.CACHE_USERSESSION_KEY_PREFIX + loginId);
        UserSession us = null;
        try {
            if (us == null) {
                final String loginIdAfterDecode = URLDecoder.decode(loginId, "UTF-8");
                final DdbPeCustom peCustom = this.peCustomService.getByLoginId(loginIdAfterDecode);
                us = new UserSession();
                us.setSsoUser(ssoUser);
                us.setPeCustom(peCustom);
                us.setId(ssoUser.getId());
                us.setLoginId(ssoUser.getLoginId());
                try {
                    this.memCacheService.set(key, us, Constants.DEFAULT_CACHE_EXPIRATION);
                } catch (CacheException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取用户信息错误！", e);
            throw new SdkException("获取用户信息错误！");
        }
        return us;
    }

    /**
     * 登陆.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfo login(User user, HttpServletRequest request, HttpServletResponse response)
        throws SdkException, JsonParseException, JsonMappingException, IOException {
        return ucenterLogin(user, request, response, true);
    }

    private UserInfo ucenterLogin(User user, HttpServletRequest request, HttpServletResponse response,
        boolean checkAdd) throws SdkException, JsonParseException, JsonMappingException, IOException {
        //根据type是否有值判断v2版本还是v1版本,如果有则是V2请求,否则是V1请求
        UserSession userSession = null;
        UserInfo userInfo = new UserInfo();
        if(StringUtils.isNotBlank(user.getType())) {
            final Map<String, Object> map = loginV2(user, checkAdd);
            userSession = (UserSession) map.get("userSession");
            userInfo = (UserInfo) map.get("userInfo");
        }else {
            userSession = loginV1(user, checkAdd, request, response);
        }
        
        userInfo.setLoginId(userSession.getPeCustom().getLoginId());
        userInfo.setSex(userSession.getPeCustom().getFlagGender());
        userInfo.setTrueName(StringUtils.isBlank(userSession.getPeCustom().getNickName())
            ? userSession.getPeCustom().getTrueName() : userSession.getPeCustom().getNickName());
        final DdbPeLabel ddbPeLabel = peLabelMapper.getById(userSession.getPeCustom().getFkLabelId());
        if (ddbPeLabel != null) {
            userInfo.setGrade(ddbPeLabel.getName());
        }
        userInfo.setAge(userSession.getPeCustom().getAge());
        userInfo.setPhoto(FileUtils.getFullRequestPath(userSession.getSsoUser().getPhoto()));
        final DdbPrPenCustom penCustom = prPenCustomMapper.getByUserId(userSession.getPeCustom().getId());
        if (penCustom != null) {
            final DdbPePen pen = pePenMapper.getById(penCustom.getFkPenId());
            if (pen != null) {
                userInfo.setBindDevice(pen.getIdentifiaction());
                userInfo.setMacAddress(pen.getMacAddress());
                userInfo.setType(pen.getType());
            }
        }
        // 查询历史绑定笔类型列表,返回是否绑定过android笔
        final List<PenType> typeList = prPenCustomMapper.getHasBoundPenType(userSession.getPeCustom().getId());
        if (null == typeList) {
            return userInfo;
        }
        for (PenType type : typeList) {
            if (PenType.ANDROID == type) {
                userInfo.setHasBoundAndroidPen(true);
                break;
            }
        }
        return userInfo;
    }

    private DdbPeCustom createPeCustom(SsoUser ssoUser,String ticket) throws SdkException{
        final String mobile = ssoUser.getBindmobile();
        // 设置为学生角色
        ssoUser.setFkRoleId(Constants.ZERO);
        ssoUser.setTrueName(mobile);
        // 变更用户中心生成的loginId
        ssoUser.setLoginId(mobile);
        //更新用户中心数据
        Boolean bl = CommUtil.updateUserInfo(ticket, ssoUser);
        //处理结果
        if(!bl) {
        	throw new SdkException(Constants.UCENTER_ERROR);
        }
        
        final DdbPeCustom peCustom = new DdbPeCustom();
        peCustom.setId(CommUtil.genRecordKey());
        peCustom.setFkUserId(ssoUser.getId());
        peCustom.setTrueName(mobile);
        peCustom.setMobilephone(mobile);
        peCustom.setLoginId(mobile);
        peCustomMapper.create(peCustom);
        try {
            memCacheService
                .delete(CommUtil.getCacheKey(Constants.CACHE_USERSESSION_KEY_PREFIX + peCustom.getLoginId()));
        } catch (CacheException e) {
            e.printStackTrace();
        }
        return peCustom;
    }
    
    private Map<String,Object> loginV2(User user,boolean checkAdd) throws SdkException, JsonParseException, JsonMappingException, IOException {
        UserSession userSession = null;
        final UserInfo userInfo = new UserInfo();
        final Map<String, String> params = new HashMap<String, String>();
        final String type = user.getType();
        //请求V2版本接口登录
        params.put("appId", "apiServer01");
        params.put("appKey", "5pyd6L6e55m95bid5b2p5LqR6Ze0");
        params.put("type", type);
        if("mobile".equals(type)) {
            params.put("mobile", user.getUserName());
            params.put(Constants.PASSWORD, user.getPassword());
        }else if("mobile_fast".equals(type)) {
            params.put("mobile", user.getUserName());
            params.put("code", user.getCode());
        }else if("weixin".equals(type)) {
            params.put("unionId", user.getUnionId());
            params.put("access_token", user.getAccessToken());
        }else if("qq".equals(type)) {
            params.put("qq_app_id", user.getQqAppId());
            params.put("access_token", user.getAccessToken());
            params.put("openId", user.getOpenId());
        }else if("refresh".equals(type)) {
            params.put("refresh_token", user.getRefreshToken());
        }
        // 向用户中心发送请求
        final String result = CommUtil.postHttps(Constants.NEW_USERCENTER_LOGIN_ADDS, params);
        if (StringUtils.isBlank(result)) {
            throw new SdkException(Constants.UCENTER_ERROR);
        }
        final ObjectMapper mapper = new ObjectMapper();
        final HashMap<String, Object> jsonObject = mapper.readValue(result, HashMap.class);
        if(jsonObject.containsKey("errorCode")) {
            if("Success".equals(jsonObject.get("errorMsg"))) {
                String jsonString = JSON.toJSONString(jsonObject.get("data"));
                Token token = JSONObject.parseObject(jsonString,Token.class);
                String tokenType = token.getToken_type();
                String accesstoken  = token.getAccess_token();
                String userId = token.getUserId();
                
                String authorization = tokenType+" "+accesstoken;
                
                //请求用户中心获取用户信息
                String url = Constants.NEW_USERCENTER_USER+userId;
                SsoUser ssoUser = CommUtil.getUserInfoV2(url, authorization);
                //初始化userSession
                if(ssoUser != null) {
                    userSession = getUserSessionByLoginIdExt(ssoUser);
                }else {
                    throw new SdkException("获取用户信息错误！");
                }
                try {
                    this.memCacheService.set(authorization, userId, 2*60*60*1000);
                    this.memCacheService.set(authorization+"#loginId",ssoUser.getLoginId(), 2*60*60*1000);
                } catch (CacheException e) {
                    e.printStackTrace();
                }
                // 只有普通登陆的时候才检查一下
                if (checkAdd) {
                    // 登陆的时候 如果DdbPeCustom为空的话就重建一个
                    if (userSession.getPeCustom() == null) {
                        //传递authorization,根据authorization判断V1版本和V2版本
                        userSession.setPeCustom(createPeCustom(userSession.getSsoUser(),authorization));
                    }
                }
                userInfo.setToken(token);
            }else {
                String errorMsg = (String) jsonObject.get("errorMsg");
                String errorCode = (String) jsonObject.get("errorCode");
                throw new SdkException(errorMsg,errorCode,null,null);
            }
        }else {
            throw new SdkException(Constants.UCENTER_ERROR);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("userInfo", userInfo);
        map.put("userSession", userSession);
        return map;
    }
    
    private UserSession loginV1(User user,boolean checkAdd,HttpServletRequest request,HttpServletResponse response) throws SdkException, JsonParseException, JsonMappingException, IOException {
        UserSession userSession = null;
        //请求V1版本接口登录
        final Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.LOGINIDKEY, user.getUserName());
        params.put(Constants.PASSWORD, user.getPassword());
        params.put(Constants.SITECODE, Constants.SCHOOL_NO);
        params.put(Constants.JSON, Constants.JSON);
        params.put(Constants.IP_PARAM, CommUtil.getIpAddr(request));
        // 向用户中心发送请求
        final String result = CommUtil.postHttps(Constants.USERCENTER_LOGIN_ADDS, params);
        if (StringUtils.isBlank(result)) {
            throw new SdkException(Constants.UCENTER_ERROR);
        }
        final ObjectMapper mapper = new ObjectMapper();
        final HashMap<String, Object> jsonObject = mapper.readValue(result, HashMap.class);
        if (jsonObject.containsKey(Constants.RESULT)) {
            if (Constants.SUCCESS.equals(jsonObject.get(Constants.RESULT))) {
                final Map<String, Object> tip = (Map<String, Object>) jsonObject.get(Constants.TIP);
                String  ticket = "";
                if (tip != null) {
                    response.setCharacterEncoding(Constants.UTF8_ENCODING);
                    response.setContentType(Constants.CONTENT_TYPE);
                    Cookie cookie = new Cookie(Constants.LOGINIDKEY, user.getUserName());
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    ticket = (String) tip.get(Constants.TICKET);
                    cookie = new Cookie(Constants.UCENTERKEY,  ticket);
                    response.addCookie(cookie);
                    String jsonString = JSON.toJSONString(tip.get("user"));
                    SsoUser ssoUser = JSONObject.parseObject(jsonString, SsoUser.class);
                    if(ssoUser != null) {
                        userSession = getUserSessionByLoginIdExt(ssoUser);
                    }else {
                        throw new SdkException("获取用户信息错误！");
                    }
                }
                // 只有普通登陆的时候才检查一下
                if (checkAdd) {
                    // 登陆的时候 如果DdbPeCustom为空的话就重建一个
                    if (userSession.getPeCustom() == null) {
                        userSession.setPeCustom(createPeCustom(userSession.getSsoUser(),ticket));
                    }
                }
            }else {
                Object object = jsonObject.get(Constants.TIP);
                if(object != null) {
                    throw new SdkException(object.toString());
                }else {
                    throw new SdkException(Constants.UCENTER_ERROR);
                }
            }
        } else {
            throw new SdkException(Constants.UCENTER_ERROR);
        }
        return userSession;
    }

    @Override
    public SsoUser getUserPhoto(String loginId,HttpServletRequest request) throws SdkException {
        if (StringUtils.isEmpty(loginId)) {
            return null;
        }
        //请求用户中心获取用户信息
        SsoUser ssoUser = CommUtil.getUserInfo(request);
        
        if(ssoUser == null) {
            throw new SdkException("获取用户信息失败!");
        }
        
        String photo = ssoUser.getPhoto();
        if (StringUtils.isEmpty(photo)) {
            ssoUser.setPhoto("");
        } else {
            ssoUser.setPhoto(photo);
        }
        return ssoUser;
    }
    
}