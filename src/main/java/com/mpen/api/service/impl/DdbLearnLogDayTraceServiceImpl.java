package com.mpen.api.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mpen.api.domain.DdbLearnLogDayTrace;
import com.mpen.api.mapper.DdbLearnLogDayTraceMapper;
import com.mpen.api.service.DdbLearnLogDayTraceService;

/**
 * <p>
 * 学情日志每日汇总数据  服务实现类
 * </p>
 *
 * @author hzy
 * @since 2018-10-11
 */
@Component
public class DdbLearnLogDayTraceServiceImpl implements DdbLearnLogDayTraceService {
    @Autowired
    private DdbLearnLogDayTraceMapper logDayTraceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdate(List<DdbLearnLogDayTrace> logDayTraces) {
        try {
            for (DdbLearnLogDayTrace logDayTrace : logDayTraces) {
                logDayTrace.setStudyDate(new Date(logDayTrace.getStudyDateInMS()));
                logDayTrace.setCreateDate(new Date(logDayTrace.getCreateDateInMS()));
                logDayTrace.setUpdateDate(new Date(logDayTrace.getUpdateDateInMS()));
            }
            logDayTraceMapper.batchUpdate(logDayTraces);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
	
}