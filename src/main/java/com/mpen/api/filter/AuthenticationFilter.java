/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.filter;

import com.fasterxml.classmate.util.ResolvedTypeCache.Key;
import com.mp.shared.common.NetworkResult;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Uris;
import com.mpen.api.domain.SsoUser;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.service.PePenService;
import com.mpen.api.service.SsoUserService;
import com.mpen.api.util.CommUtil;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitterReturnValueHandler;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final String[] URLS = { "http://www.mpen.com.cn", "https://www.mpen.com.cn", "http://ddb.webtrn.cn",
        "https://ddb.webtrn.cn", "http://code.mpen.com.cn", "https://code.mpen.com.cn", "http://api.mpen.com.cn",
        "https://api.mpen.com.cn" };
    // TODO: 出版系统请求IP,暂时写到代码中,以后改到配置文件中,.*的ip地址需要加到最后
    private static final String[] PUBLISH_IPS = {"139.210.167.214","114.247.222.226", "47.92.157.127", "47.92.159.20", 
            "221.122.122.0-221.122.122.31","222.168.57.*"};
    
    @Value("${web.allow-urls}")
    private String allowUrls;
    @Value("${web.disallow-urls}")
    private String disallowUrls;
    @Autowired
    private SsoUserService ssoUserService;
    @Autowired
    private PePenService pePenService;
    @Autowired
    private MemCacheService memCacheService;

    /**
     * TODO 增加安全验证，使得外部不能访问内部接口.类似的接口还有,如下:
     *      普通个人用户，园丁系统老师用户，内部服务器访问都应该有不同的访问权限的。我们需要能够区分这些访问的:
     *      后续需要理顺接口，看看哪些是给普通用户，哪些给园丁用户，（有一部分是重复的）然后要能够判定调用用户身份，决定是否提供服务(要和sso一起考虑)
     * 服务器: 
     *   清除缓存接口: /v1/memeCache/?key=book; 
     *   刷新bookList接口: /v1/books/?action=getValidBooks;
     *   服务器检查接口: /v1/qa/checkServer;
     * 出版系统:
     *   获取教师笔版本信息接口: /v1/pens/penInfo?key=penVersionInfo;
     *   生成教师笔版本信息接口: /v1/pens/penInfo?key=teacherPen;
     *   查看生成点读码进度接口: /v1/publishing/book?action=getProgress;
     *   创建图书接口: /v1/publishing/book?action=createBook;
     *   生成点读码接口: /v1/publishing/book?action=createCode;
     *   书资源上传到服务器接口: /v1/publishing/file?action=uploadBook;
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        // 设置禁止缓存
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setCharacterEncoding("utf8");
        NetworkResult<Object> result = null;
        String msg = checkRequestAccess(httpRequest);
        if (!Constants.SUCCESS.equals(msg)) {
            result = new NetworkResult<>(NetworkResult.BAD_REQUEST_ERROR_CODE, msg);
            logger.error(result.getErrorMsg(), httpRequest.getRequestURL());
            ((HttpServletResponse) response).setStatus(200);
            response.getWriter().write(Constants.GSON.toJson(result));
            return; // 直接返回
        }
        if (!checkUriAccess(httpRequest)) {
            logger.error(Constants.NO_MACHING_ERROR_MSG, httpRequest.getRequestURL());
            ((HttpServletResponse) response).setStatus(404);
            result = new NetworkResult<>(NetworkResult.NO_MACHING_ERROR_CODE, NetworkResult.NO_MACHING_ERROR_MSG);
            response.getWriter().write(Constants.GSON.toJson(result));
            return; // 直接返回
        }
        if (checkLoginAccess(httpRequest)) {
            chain.doFilter(httpRequest, response);
        } else {
            logger.error(Constants.INVALID_LOGINID_ERROR, httpRequest.getRequestURL());
            ((HttpServletResponse) response).setStatus(403);
            result = new NetworkResult<>(NetworkResult.ACCESS_FORBIDDEN_ERROR_CODE, Constants.NO_MATCHING_USER);
            response.getWriter().write(Constants.GSON.toJson(result));
            return; // 直接返回
        }
    }

    private boolean checkLoginAccess(HttpServletRequest httpRequest) {
        String loginId = null;
        //获取认证请求头,判断是否为空,如果为空则为V1否则为V2
        final String authorization = httpRequest.getHeader("Authorization");
        // 售后管理系统请求需要去用户中心验证
        final String key = httpRequest.getParameter("key");
        final String userId = httpRequest.getHeader("userId");
        if (Constants.KEY_FOR_WYS_INVENTORY_MANAGER.equals(key)) {
            //请求用户中心，获取用户信息
            final String url = Constants.NEW_USERCENTER_USER + userId;
            try {
                SsoUser userInfoV2 = CommUtil.getUserInfoV2(url, authorization);
                if(userInfoV2 != null) {
                    return true;
                }
            } catch (SdkException e) {
                return false;
            }
            return true;
        }
        if(StringUtils.isNotBlank(authorization)) {
            try {
                loginId = memCacheService.get(authorization+"#loginId");
            } catch (CacheException e) {
                e.printStackTrace();
            }
        }else {
            loginId = CommUtil.getLoginId(httpRequest);
        }
        
        if (isInNoNeedLoginList(httpRequest)) {
            return true;
        }
        
        UserSession userSession = null;
        try {
            userSession = ssoUserService.getUserSessionByLoginId(loginId,httpRequest);
        } catch (SdkException sdkException) {
            logger.error("", sdkException);
        }
        if (userSession != null && StringUtils.isNotBlank(userSession.getLoginId())) {
            httpRequest.setAttribute(Constants.LOGINIDKEY, loginId);
            return true;
        }
        // 检验出版系ip,如果通过,return true,不通过,继续执行checkReadingAllow();
        if (checkPublishAllow(httpRequest)) {
            return true;
        }
        
        return checkReadingAllow(httpRequest);
    }
    
    /**
     * 检验出版系统访问的ip是否在允许的列表中
     * @param httpRequest
     * @return
     */
    private boolean checkPublishAllow(HttpServletRequest httpRequest) {
        // 出版系统暂时无法校验登陆,改为校验ip
        final String requestIp = CommUtil.getIpAddr(httpRequest);
        final String uri = httpRequest.getRequestURI();
        final String key = httpRequest.getParameter("key");
        // 校验key和访问接口
        if (!Constants.PUBLISH_KEY.equals(key) || !uri.startsWith("/v1/publishing")) {
            return false;
        }
        // 检验是否为出版系统的请求ip
        for (final String publishIp : PUBLISH_IPS) {
            if (requestIp.equals(publishIp)) {
                return true;
            }
            // TODO：改进数据结构和查询效率。
            if (publishIp.contains("*") && requestIp.startsWith(publishIp.replaceAll("\\*", ""))) {
                return true;
            }
            if (publishIp.contains("-")) {
                final String[] strings = publishIp.split("-");
                if (requestIp.startsWith(strings[0].substring(0, strings[0].lastIndexOf(".")))) {
                    if (Integer.parseInt(requestIp.substring(12, requestIp.length())) >= 0
                            && Integer.parseInt(requestIp.substring(12, requestIp.length())) <= 31) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    

    /**
     * 未绑定时，默认允许云点读100次.
     * 
     */
    private boolean checkReadingAllow(HttpServletRequest httpRequest) {
        if (isInMustLoginList(httpRequest)) {
            return false;
        }
        return pePenService.checkReadingAllow(httpRequest);
    }

    /**
     * TODO 校验是否在开放接口白名单中
     * 
     */
    private boolean isInNoNeedLoginList(HttpServletRequest httpRequest) {
        // TODO 临时写死，之后改为配置文件
        final String uri = httpRequest.getRequestURI();
        return  uri.startsWith("/v1/qa") || uri.startsWith("/v1/files")
            || uri.startsWith("/v1/user/login") || uri.startsWith("/v1/mobiles/app") || uri.startsWith("/v1/questions")
            || (uri.startsWith("/v1/pens/") && (Constants.ADB_ADMIT.equals(httpRequest.getParameter("action"))
                || Constants.UPGRADE_APP.equals(httpRequest.getParameter("action"))
                || Constants.UPGRADE_ROM.equals(httpRequest.getParameter("action"))))
            || (uri.startsWith("/v1/logs/dataAnalysis") && "https".equals(CommUtil.getScheme(httpRequest)))
                && Constants.ACCESS_CONTROL_KEY.equals(httpRequest.getParameter("key"))
            || uri.startsWith(Uris.V1_MEMECHACHE) || uri.startsWith(Uris.V1_QUESTION) || uri.startsWith(Uris.V1_SHOPS)
            || uri.equals(Uris.V1_LOGS + "/") || uri.startsWith("/v1/pc/gardener")|| uri.startsWith("/v1/program")
            || uri.equals("/v1/pens/penInfo") || (uri.startsWith("/v1/studyCount") 
                && Constants.SHADOW_KEY_FOR_STUDY_COUNT.equals(httpRequest.getParameter("shadowkey")));
    }

    /**
     * TODO 校验是否在必须绑定接口黑名单中
     * 
     */
    private boolean isInMustLoginList(HttpServletRequest httpRequest) {
        final String uri = httpRequest.getRequestURI();
        return uri.equals("/v1/messages/")
            || (uri.startsWith("/v1/pens/") && (Constants.CHECK_BIND.equals(httpRequest.getParameter("action"))))
            || uri.equals("/v1/pens/") || (uri.startsWith(Uris.V1_USER) && !uri.contains(Uris.LOGIN));

    }

    /**
     * TODO 校验请求是否在允许的配置文件中
     * 
     */
    private boolean checkUriAccess(HttpServletRequest httpRequest) {
        final String uri = httpRequest.getRequestURI();
        if (StringUtils.isNotBlank(disallowUrls) && matchPattern(uri, disallowUrls)) {
            return false;
        }
        if (StringUtils.isNotBlank(allowUrls) && !matchPattern(uri, allowUrls)) {
            return false;
        }
        return true;
    }

    private boolean matchPattern(String uri, String urlPatterns) {
        final String[] urlArray = urlPatterns.split(",");
        for (String url : urlArray) {
            if (uri.startsWith(url)) {
                return true;
            }
        }
        return false;
    }

    private String checkRequestAccess(HttpServletRequest request) {
        // 验证来源
        final String referer = request.getHeader("Referer");
        // TODO 为了兼容之前的，允许referer为空情况，以后去除
        if (StringUtils.isBlank(referer)) {
            return Constants.SUCCESS;
        }
        // TODO 验证来源是否为网页端ajax请求，以后增加user-agent
        for (String url : URLS) {
            if (referer.startsWith(url)) {
                return Constants.SUCCESS;
            }
        }
        if (!"mpenAndroid".equalsIgnoreCase(referer) && !"mpenIOS".equalsIgnoreCase(referer)
            && !"mpenPen".equalsIgnoreCase(referer) && !"mpenPublish".equalsIgnoreCase(referer)) {
            return Constants.WRONG_REFERER;
        }
        final String userAgent = request.getHeader("User-Agent");
        return CommUtil.checkUserAgent(userAgent);
    }

    @Override
    public void destroy() {
    }

}
