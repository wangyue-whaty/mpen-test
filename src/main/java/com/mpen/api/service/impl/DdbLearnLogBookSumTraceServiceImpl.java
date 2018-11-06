package com.mpen.api.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mpen.api.domain.DdbLearnLogBookSumTrace;
import com.mpen.api.mapper.DdbLearnLogBookSumTraceMapper;
import com.mpen.api.service.DdbLearnLogBookSumTraceService;

/**
 * <p>
 * 学习书籍列表  服务实现类
 * </p>
 *
 * @author hzy
 * @since 2018-10-11
 */
@Component
public class DdbLearnLogBookSumTraceServiceImpl implements DdbLearnLogBookSumTraceService {
    
    @Autowired
    private DdbLearnLogBookSumTraceMapper logBookSumTraceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdate(List<DdbLearnLogBookSumTrace> logBookSumTraces) {
        try {
            for (DdbLearnLogBookSumTrace bookSumTrace : logBookSumTraces) {
                bookSumTrace.setLatestDate(new Date(bookSumTrace.getLatestDateInMS()));
                bookSumTrace.setCreateDate(new Date(bookSumTrace.getCreateDateInMS()));
                bookSumTrace.setUpdateDate(new Date(bookSumTrace.getUpdateDateInMS()));
            }
            logBookSumTraceMapper.batchUpdate(logBookSumTraces);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
	
}