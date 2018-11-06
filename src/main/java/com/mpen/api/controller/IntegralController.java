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
import com.mpen.api.bean.Book;
import com.mpen.api.bean.ClassAssignments;
import com.mpen.api.bean.IntegralRecord;
import com.mpen.api.bean.UserIntegralSet;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.service.IntegralService;
import com.mpen.api.service.UserSessionService;

import ch.qos.logback.core.joran.action.Action;

/**
 * 用户积分相关API
 * 涉及：App2.0 积分相关
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_INTEGRAL)
public class IntegralController {

    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private IntegralService integralService;

    /*
     * 用户积分记录
     */
    @GetMapping(Uris.USERRECORD)
    public @ResponseBody Callable<NetworkResult<Object>> getComments(IntegralRecord integralRecord,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession userSession = userSessionService.getUser(request, response);
                switch (integralRecord.getAction()) {
                // 获取用户积分记录列表
                case Constants.INTEGRALLIST:
                    return RsHelper.success(integralService.getIntegralRecord(userSession, integralRecord));
                // 获取总排行榜
                case Constants.GET_RANKING_LIST:
                    return RsHelper.success(integralService.getRankingList(userSession, integralRecord));
                // 获取好友排行榜
                case Constants.GET_FRIENDS_LIST:
                    return RsHelper.success(integralService.getRankingList(userSession, integralRecord));
                // 获取总排行榜个人排行情况
                case Constants.GET_PERSONAL_RANKING:
                    return RsHelper.success(integralService.getUserRanking(userSession, integralRecord.getType()));
                // 获取个人近期7天的积分
                case Constants.GET_RECENT_INTEGRAL:
                    return RsHelper.success(integralService.getRecentIntegral(userSession));
                // 如何获取积分的说明
                case Constants.GET_INTEGRAL_INSTRUCTION:
                    return RsHelper.success(integralService.getIntegralInstruction(userSession));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }

    /**
     * 积分榜点赞
     */
    @PostMapping(Uris.USERRECORD)
    public @ResponseBody Callable<NetworkResult<Object>> savePraiseNum(
            @RequestBody final UserIntegralSet userIntegralSet, final HttpServletRequest request,
            final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (userIntegralSet.getAction()) {
                // 点赞
                case Constants.UPDATEPRAISENUM:
                    return RsHelper.success(integralService.updatePraiseNum(userIntegralSet));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
}
