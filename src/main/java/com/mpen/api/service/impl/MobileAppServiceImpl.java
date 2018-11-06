package com.mpen.api.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.mpen.api.domain.MobileApp;
import com.mpen.api.mapper.MobileAppMapper;
import com.mpen.api.service.MobileAppService;
import com.mpen.api.util.FileUtils;

/**
 * 手机app升级服务.
 *
 * @author zyt
 *
 */
@Component
public class MobileAppServiceImpl implements MobileAppService {
    @Resource
    private MobileAppMapper mobileAppMapper;

    @Override
    public MobileApp get(MobileApp.Type type) throws Exception {
        final MobileApp app = mobileAppMapper.get(type);
        if (app != null) {
            app.setFileUrl(FileUtils.getFullRequestPath(app.getFileUrl()));
        }
        return app;
    }

}
