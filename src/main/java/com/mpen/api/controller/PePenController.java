/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

import com.mp.shared.common.NetworkResult;
import com.mp.shared.common.SuccessResult;
import com.mpen.api.bean.Pen;
import com.mpen.api.bean.PenInfo;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.service.AppService;
import com.mpen.api.service.PePenService;
import com.mpen.api.service.PrPenCustomService;
import com.mpen.api.service.RomUpdateService;
import com.mpen.api.service.UserSessionService;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO 笔资源相关API.
 *
 * @author zyt
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_PENS)
public class PePenController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private PrPenCustomService prPenCustomService;
    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private AppService appService;
    @Autowired
    private RomUpdateService romUpdateService;
    @Autowired
    private PePenService pePenService;

    /**
     * pens相关,校验笔绑定信息，获取笔信息和升级信息接口.
     * 
     */
    @GetMapping(Uris.PENIDS)
    public @ResponseBody Callable<NetworkResult<Object>> getPenInfo(@PathVariable final String penId,
        final PenInfo penInfo, final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (penInfo.getAction()) {
                case Constants.CHECK_BIND:
                    if (StringUtils.isBlank(penId)) {
                        LOGGER.error(Constants.INVALID_PARAMETER_ERROR, request.getRequestURL());
                        return RsHelper.error(Constants.BAD_REQUEST_ERROR_CODE, Constants.INVALID_PARAMRTER_MESSAGE);
                    }
                    final UserSession user = userSessionService.getUser(request, response);
                    final SuccessResult result = prPenCustomService.checkBindRelationship(penId, user);
                    return RsHelper.success(result);
                case Constants.UPGRADE_APP:
                    return RsHelper.success(appService.getAppMessageByPenId(penId, penInfo.getVersion()));
                case Constants.UPGRADE_ROM:
                    return RsHelper.success(romUpdateService.getUpdateMessage(penId, penInfo.getVersion()));
                case Constants.ADB_ADMIT:
                    final SuccessResult res = new SuccessResult();
                    res.setSuccess(pePenService.adbAdmit(penId));
                    return RsHelper.success(res);
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }

    /**
     * 根据Mac地址，获取笔信息.
     * 
     */
    @GetMapping("/")
    public @ResponseBody Callable<NetworkResult<Object>> getPenMac(final PenInfo penInfo,
        final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (penInfo.getAction()) {
                case Constants.UN_BIND_PEN:
                    return RsHelper.success(pePenService.getUnBindPen(penInfo.getMacAddress()));
                case Constants.COMPLETE_MAC:
                    return RsHelper.success(pePenService.getCompleteMac(penInfo.getMacAddress()));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
    
    /**
     * 根据序列号获取笔id，以及mac地址.
     * liangixong   2018/4/19
     * 
     */
    @GetMapping(Uris.V1_PENS_SERIALNUM)
    public @ResponseBody Callable<NetworkResult<Object>> getPenIdAndMac(final Pen pen, final HttpServletRequest request, final HttpServletResponse response) {
    	return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
            	switch (pen.getAction()) {
            	case Constants.GET_PENIDANDMAC:
                    return RsHelper.success(pePenService.getPenIdAndMac(pen.getSerialNumber()));
            	case Constants.GET_PENINFOBYSERIALNUMORMAC:
            	    return RsHelper.success(pePenService.getPenInfoBySerialNumOrMac(pen.getSerialNumber(),pen.getMacAddress()));
            	case Constants.GET_PENINFOBYMOBILE:
            	    return RsHelper.success(pePenService.getPenInfoByBindMobile(pen.getBindMobile()));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
            	}
            }
        };
    }
    
   /**
    * 生成与获取教师笔的版本信息,并保存
    * @param request
    * @param response
    * @return
    */
    @GetMapping(Uris.PENINFO)
    public @ResponseBody Callable<NetworkResult<Object>> getPenVersionForTeacher(final String key, final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (key) {
                case Constants.TEACHER_PEN:
                    return RsHelper.success(pePenService.createTeacherPenVersionInfo());
                case Constants.PEN_VERSION_INFO:
                    return RsHelper.success(pePenService.getTeacherPenVersionInfo());   
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
}
