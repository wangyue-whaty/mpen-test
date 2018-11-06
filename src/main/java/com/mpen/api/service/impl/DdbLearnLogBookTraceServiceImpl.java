package com.mpen.api.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mpen.api.bean.StudyCountAssist;
import com.mpen.api.domain.DdbLearnLogBookTrace;
import com.mpen.api.mapper.DdbLearnLogBookTraceMapper;
import com.mpen.api.service.DdbLearnLogBookTraceService;

/**
 * <p>
 * 每日书籍学习时间轨迹表  服务实现类
 * </p>
 *
 * @author hzy
 * @since 2018-10-11
 */
@Component
public class DdbLearnLogBookTraceServiceImpl implements DdbLearnLogBookTraceService {
    @Autowired
    private DdbLearnLogBookTraceMapper logBookTraceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdate(List<DdbLearnLogBookTrace> logBookTraces) {
        try {
            for (DdbLearnLogBookTrace logBookTrace : logBookTraces) {
                logBookTrace.setStudyDate(new Date(logBookTrace.getStudyDateInMS()));
                logBookTrace.setCreateDate(new Date(logBookTrace.getCreateDateInMS()));
                logBookTrace.setUpdateDate(new Date(logBookTrace.getUpdateDateInMS()));
            }
            logBookTraceMapper.batchUpdate(logBookTraces);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public DdbLearnLogBookTrace getByLoginIdBookIdStudyDate(StudyCountAssist studyCountAssist) {
        return logBookTraceMapper.getByLoginIdBookIdStudyDate(studyCountAssist);
    }
	
}