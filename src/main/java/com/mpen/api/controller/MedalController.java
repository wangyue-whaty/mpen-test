package com.mpen.api.controller;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mp.shared.common.NetworkResult;
import com.mpen.api.bean.MyMedal;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.service.MedalService;
import com.mpen.api.service.UserSessionService;

/**
 * 勋章相关API类
 * 涉及：APP2.0勋章模块
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_MEDAL)
public class MedalController {
    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private MedalService medalService;

    /*
     * 用户勋章
     */
    @GetMapping(Uris.USERRECORD)
    public @ResponseBody Callable<NetworkResult<Object>> getComments(MyMedal myMedal, final HttpServletRequest request,
            final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession userSession = userSessionService.getUser(request, response);
                switch (myMedal.getAction()) {
                // 获取用户佩戴的勋章信息
                case Constants.INTEGRALLIST:
                    return RsHelper.success(medalService.getMyMedal(userSession));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }

    @PostMapping(Uris.USERRECORD)
    public @ResponseBody Callable<NetworkResult<Object>> savePraiseNum(@RequestBody final MyMedal myMedal,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {

            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession userSession = userSessionService.getUser(request, response);
                switch (myMedal.getAction()) {
                // 勋 佩戴
                case Constants.MEDALWEAR:
                    return RsHelper.success(medalService.updateMedalWear(userSession, myMedal));
                // 勋章 拆下
                case Constants.MEDALOFF:
                    return RsHelper.success(medalService.updateMedalWear(userSession, myMedal));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
}