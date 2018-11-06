/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import com.mp.shared.common.SuccessResult;
import com.mpen.api.bean.Label;
import com.mpen.api.bean.User;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbPeLabel;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.PeCustomMapper;
import com.mpen.api.mapper.PeLabelMapper;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.service.PeCustomService;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * PeCustomService服务.
 *
 * @author kai
 *
 */
@Component
public class PeCustomServiceImpl implements PeCustomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeCustomServiceImpl.class);

    @Autowired
    private PeCustomMapper peCustomMapper;
    @Autowired
    private PeLabelMapper peLabelMapper;
    @Autowired
    private MemCacheService memCacheService;

    @Override
    public DdbPeCustom getByLoginId(String loginId) {
        return this.peCustomMapper.getByLoginId(loginId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(DdbPeCustom peCustom) {
        if (peCustom == null) {
            return null;
        } else if (StringUtils.isEmpty(peCustom.getId())) {
            peCustom.setId(CommUtil.genRecordKey());
        }
        try {
            this.peCustomMapper.create(peCustom);
            return peCustom.getId();
        } catch (Exception ex) {
            LOGGER.error("create DdbPeCustom error!", ex);
            return null;
        }
    }

    @Override
    public DdbPeCustom getById(String id) {
        return this.peCustomMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        this.peCustomMapper.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SuccessResult update(UserSession userSession, User user) throws SdkException, CacheException {
        if (StringUtils.isNotBlank(user.getGrade())) {
            final DdbPeLabel label = peLabelMapper.getById(user.getGrade());
            if (label == null) {
                throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
            }
        }
        if (user.getAge() == null) {
            user.setAge(Constants.INT_SEVEN);
        }
        peCustomMapper.update(userSession.getPeCustom().getId(), user.getNickName(), user.getSchool(), user.getGrade(),
            user.getAge(), user.getSex());
        String key = CommUtil.getCacheKey(Constants.CACHE_USERSESSION_KEY_PREFIX + userSession.getLoginId());
        memCacheService.delete(key);
        final SuccessResult result = new SuccessResult(true);
        return result;
    }

    @Override
    public List<Label> getCustomLabels(UserSession user) {
        final List<DdbPeLabel> peLabels = peLabelMapper.get();
        final List<Label> labels = new ArrayList<Label>();
        final String currentLabel = user.getPeCustom().getFkLabelId();
        Label label = null;
        for (DdbPeLabel ddbPeLabel : peLabels) {
            label = new Label();
            label.setId(ddbPeLabel.getId());
            label.setCode(ddbPeLabel.getCode());
            label.setName(ddbPeLabel.getName());
            if (ddbPeLabel.getId().equals(currentLabel)) {
                label.setIsCurrent(Constants.INT_ONE);
            } else {
                label.setIsCurrent(Constants.INT_ZERO);
            }
            labels.add(label);
        }
        return labels;
    }

    @Override
    public SuccessResult saveAddress(User user, UserSession userSession) throws CacheException {
        peCustomMapper.saveAddress(user.getAddress(), userSession.getPeCustom().getId());
        String key = CommUtil.getCacheKey(Constants.CACHE_USERSESSION_KEY_PREFIX + userSession.getLoginId());
        memCacheService.delete(key);
        final SuccessResult result = new SuccessResult(true);
        return result;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateCover(String cover, UserSession user) {
        final String fileSaveRealPath = FileUtils.getFullRequestPath(cover);
        peCustomMapper.updateCoverByLoginId(user.getLoginId(),fileSaveRealPath);
        return fileSaveRealPath;
    }
}
