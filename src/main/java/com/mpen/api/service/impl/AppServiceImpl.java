/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mpen.api.common.Constants;
import com.mpen.api.common.Constants.PenType;
import com.mpen.api.domain.DdbApp;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.AppMapper;
import com.mpen.api.mapper.PePenMapper;
import com.mpen.api.service.AppService;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.service.PePenService;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.FileUtils;

/**
 * App升级服务.
 *
 * @author zyt
 *
 */
@Component
public class AppServiceImpl implements AppService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppServiceImpl.class);
    @Autowired
    private AppMapper appMapper;
    @Autowired
    private PePenMapper pePenMapper;
    @Autowired
    private PePenService pePenService;
    @Autowired
    private MemCacheService memCacheService;

    @Override
    public DdbApp getAppMessageByPenId(String penId, String version) throws Exception {
        // 校验笔信息是否存在.
        final DdbPePen pen = pePenService.getPenByIdentifiaction(penId);
        if (pen == null || StringUtils.isBlank(pen.getId()) || StringUtils.isBlank(version)) {
            throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
        }
        DdbApp ddbApp = null;
        final PenType penType = pen.getType() == null ? PenType.ANDROID : pen.getType();
        final List<DdbApp> importantVersion = appMapper.getImportantVersion(penType);
        final DdbApp appVersion = appMapper.getByVersionName(version, penType);
        if (appVersion == null) {
            throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
        }
        // 收集版本信息
        memCacheService.delete(CommUtil.getCacheKey(Constants.CACHE_PENINFO_KEY_PREFIX + penId));
        pePenMapper.updateAppVersionByIdentifiaction(penId, version);
        final int versionCode = appVersion.getVersionCode();
        if (importantVersion != null && importantVersion.size() > 0) {
            for (DdbApp app : importantVersion) {
                if (app.getVersionCode() > versionCode) {
                    ddbApp = app;
                    break;
                }
            }
        }
        if (ddbApp == null) {
            // 笔无灰度标记时获取笔升级信息.
            if (StringUtils.isBlank(pen.getLabel())) {
                ddbApp = appMapper.get(versionCode, penType);
            } else {
                // 笔有灰度标记时获取笔升级信息.
                ddbApp = appMapper.getByLabel(pen.getLabel(), versionCode, penType);
            }
        }
        if (ddbApp != null) {
            ddbApp.setFileUrl(FileUtils.getFullRequestPath(ddbApp.getFileUrl()));
        }
        return ddbApp;
    }

}
