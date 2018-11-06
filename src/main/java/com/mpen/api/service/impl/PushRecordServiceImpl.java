/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mp.shared.common.SuccessResult;
import com.mpen.api.bean.Message;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbPushRecord;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.PushRecordMapper;
import com.mpen.api.service.PushRecordService;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.JPushUtil;

/**
 * PushRecordService服务.
 *
 * @author zyt
 *
 */
@Component
public class PushRecordServiceImpl implements PushRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushRecordServiceImpl.class);

    @Autowired
    private PushRecordMapper pushRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SuccessResult save(Message message, UserSession userSession) throws Exception {
        String content = "";
        final Map<String, String> msg = new HashMap<>();
        if (StringUtils.isBlank(message.getType()) || message.getType().equals(Constants.ZERO)) {
            message.setType(Constants.ZERO);
            // type类型为0或为空时，为电量推送.
            if (message.getBattery() == null || message.getBattery().length() == 0) {
                message.setBattery(Constants.DEFAULT_BATTERY);
            }
            content = Constants.PRE_BATTERY_PUSH + message.getBattery() + Constants.SUF_BATTERY_PUSH;
            msg.put(Constants.BATTERY, Constants.BATTERY);
            final JPushUtil.JpushParam param = new JPushUtil.JpushParam(userSession.getLoginId(), content, msg, null,
                JPushUtil.PlatformType.ALL);
            JPushUtil.sendPushToUser(param);
        } else if (Constants.ONE.equals(message.getType())) {
            // type类型为1时，为视频推送.
            content = Constants.VEDIO_PUSH;
            if (StringUtils.isBlank(message.getPath())
                && (message.getVideos() == null || message.getVideos().size() <= 0)) {
                throw new SdkException(Constants.PUSH_ERROR);
            }
            if (StringUtils.isNotBlank(message.getPath())) {
                msg.put(Constants.PATH, message.getPath());
            }
            if (message.getVideos() != null && message.getVideos().size() > 0) {
                msg.put(Constants.VIDEOS, Constants.GSON.toJson(message.getVideos()));
            }
            final JPushUtil.JpushParam param = new JPushUtil.JpushParam(userSession.getLoginId(), content, msg, null,
                JPushUtil.PlatformType.ALL);
            JPushUtil.sendPushToUser(param);
        } else {
            throw new SdkException(Constants.PUSH_ERROR);
        }
        final DdbPushRecord pushRecord = new DdbPushRecord();
        pushRecord.setId(CommUtil.genRecordKey());
        pushRecord.setLoginId(userSession.getLoginId());
        pushRecord.setType(message.getType());
        pushRecord.setContent(content);
        pushRecord.setTime(new Date());
        pushRecordMapper.create(pushRecord);
        SuccessResult result = new SuccessResult();
        result.setSuccess(true);
        return result;
    }
}
