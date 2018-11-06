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
import com.mpen.api.bean.Dynamic;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.service.DdbUserDynamicPraiseService;
import com.mpen.api.service.DdbUserDynamicRecordService;
import com.mpen.api.service.FileService;
import com.mpen.api.service.PeCustomService;
import com.mpen.api.service.UserSessionService;
import com.mpen.api.util.FileUtils;

/**
 * 用户动态前端控制器 涉及：app2.0动态模块
 * 
 * @author hzy
 * @since 2018-08-09
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_DYNAMIC)
public class DynamicController {
    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private DdbUserDynamicRecordService dynamicRecordService;
    @Autowired
    private DdbUserDynamicPraiseService dynamicPraiseService;
    @Autowired
    private PeCustomService peCustomService;
    @Autowired
    private FileService fileService;

    @PostMapping(Uris.DYNAMICLIST)
    public @ResponseBody Callable<NetworkResult<Object>> getDynamic(@RequestBody Dynamic dynamic,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession user = userSessionService.getUser(request, response);
                NetworkResult<Object> networkResult;
                switch (dynamic.getAction()) {
                // 获取动态列表
                case Constants.DYNAMIC:
                    dynamic.setLoginId(user.getLoginId());
                    networkResult = RsHelper.success(dynamicRecordService.pageDynamic(dynamic, user));
                    break;
                // 我的动态列表
                case Constants.MYDYNAMIC:
                    dynamic.setLoginId(user.getLoginId());
                    networkResult = RsHelper.success(dynamicRecordService.pagePersonalDynamic(dynamic, user));
                    break;
                // 好友动态列表
                case Constants.FRIENDDYNAMIC:
                    networkResult = RsHelper.success(dynamicRecordService.pagePersonalDynamic(dynamic, user));
                    break;
                // 获取封面信息
                case Constants.COVER:
                    networkResult = RsHelper.success(dynamicRecordService.getUserCover(user));
                    break;
                // 点赞
                case Constants.PRAISE:
                    networkResult = RsHelper.success(dynamicPraiseService.savePraise(user, dynamic.getId()));
                    break;
                default:
                    networkResult = RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                    break;
                }
                return networkResult;
            }
        };
    }

    /**
     * 更新封面
     */
    @PostMapping(Uris.EDIT_COVER)
    public @ResponseBody Callable<NetworkResult<Object>> uploadFile(final Dynamic dynamic,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession user = userSessionService.getUser(request, response);
                final String cover = fileService.saveFile(dynamic.getFile(), FileUtils.COVER_FILES, null);
                return RsHelper.success(peCustomService.updateCover(cover, user));
            }
        };
    }

}
