package com.mpen.api.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.mpen.api.bean.HomeWorks;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbUserHomework;
import com.mpen.api.domain.DdbUserHomeworkState;
import com.mpen.api.domain.DdbUserIntegralRecord;
import com.mpen.api.mapper.DdbUserHomeworkMapper;
import com.mpen.api.mapper.DdbUserHomeworkStateMapper;
import com.mpen.api.mapper.DdbUserIntegralRecordMapper;
import com.mpen.api.service.DdbUserDynamicRecordService;
import com.mpen.api.service.DdbUserHomeworkStateService;
import com.mpen.api.util.CommUtil;

/**
 * <p>
 * 学生班级作业表 服务实现类
 * </p>
 *
 * @author hzy
 * @since 2018-07-05
 */
@Component
public class DdbUserHomeworkStateServiceImpl implements DdbUserHomeworkStateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DdbUserHomeworkStateServiceImpl.class);
    @Autowired
    private DdbUserHomeworkStateMapper homeworkStateMapper;
    @Autowired
    private DdbUserHomeworkMapper homeworkMapper;
    @Autowired
    private DdbUserIntegralRecordMapper integralRecordMapper;
    @Autowired
    private DdbUserDynamicRecordService ddbUserDynamicRecordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(HomeWorks homeWorks, UserSession user) {
        final Date curDate = new Date();
        final DdbUserHomeworkState homeworkState = new DdbUserHomeworkState();
        homeworkState.setId(CommUtil.genRecordKey());
        homeworkState.setFkClassId(homeWorks.getFkClassId());
        homeworkState.setFkLoginId(user.getLoginId());
        homeworkState.setFkHomeworkId(homeWorks.getId());
        homeworkState.setIsCommit("1");
        homeworkState.setIsMarking("0");
        homeworkState.setUploadTime(curDate);
        final String json = Constants.GSON.toJson(homeWorks.getResourceUrl());
        homeworkState.setResourceUrl(json);
        homeworkState.setCreateTime(curDate);
        homeworkState.setUpdateTime(curDate);

        homeworkStateMapper.save(homeworkState);

        Constants.CACHE_THREAD_POOL.execute(() -> {
            // 动态保存和积分保存
            saveIntegarlAndDynamic(homeWorks, user, homeworkState);
        });
        return true;
    }

    /**
     * 保存学生完成作业动态和积分
     */
    private void saveIntegarlAndDynamic(HomeWorks homeWorks, UserSession user, DdbUserHomeworkState homeworkState) {
        final DdbUserHomework homework = homeworkMapper.getById(homeWorks.getId());
        final Date startDate = homework.getCreateDate();
        final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        final String dateString = formatter.format(startDate);
        ddbUserDynamicRecordService.saveDynamic(user.getLoginId(), "完成作业",
                "刚刚完成了作业，<font color='#adec00'[" + dateString + "日英语作业练习]</font>  你完成了吗？");
        final String uploadTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        // 判断是否今天第一次完成作业(课前,课后)
        final DdbUserHomeworkState workState = homeworkStateMapper.getByLoginIdDay(user.getLoginId(), homework.getType(),
                uploadTime, homeworkState.getId());
        if (workState == null) {
            final Date curDate = new Date();
            final DdbUserIntegralRecord integralRecord = new DdbUserIntegralRecord();
            integralRecord.setId(CommUtil.genRecordKey());
            integralRecord.setFkLoginId(user.getLoginId());
            integralRecord.setCreateTime(curDate);
            integralRecord.setIntegral(500);
            final String type = homework.getType() == Constants.INT_ONE.intValue() ? "作业练习" : "课前预习";
            integralRecord.setIntegralType(type);
            integralRecord.setUpdateTime(curDate);
            integralRecordMapper.save(integralRecord);
        }
    }
}
