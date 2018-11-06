package com.mpen.api.controller;

import java.util.concurrent.Callable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mp.shared.common.NetworkResult;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.domain.PCVersionUpdate;
import com.mpen.api.service.PCVersionUpdateService;

/*
 * PC系统相关 API
 * 
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_PC)
public class PCVersionUpdateController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PCVersionUpdateController.class);
    @Resource
    private PCVersionUpdateService pcService;

    /*
     * PC端园丁系统升级
     */
    @GetMapping(Uris.GARDENER)
    public @ResponseBody Callable<NetworkResult<Object>> getAppInfo(@PathVariable final PCVersionUpdate.Type type,
            final String action, final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (action) {
                case Constants.UPGRADE_PC:
                    return RsHelper.success(pcService.get(type));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }

}
