package com.mpen.api.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mpen.api.domain.DdbLearnLogBookDetailTrace;
import com.mpen.api.mapper.DdbLearnLogBookDetailTraceMapper;
import com.mpen.api.service.DdbLearnLogBookDetailTraceService;

/**
 * <p>
 * 学情日志书籍内容统计  服务实现类
 * </p>
 *
 * @author hzy
 * @since 2018-10-11
 */
@Component
public class DdbLearnLogBookDetailTraceServiceImpl implements DdbLearnLogBookDetailTraceService {
    
    @Autowired
    private DdbLearnLogBookDetailTraceMapper logBookDetailTraceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdate(List<DdbLearnLogBookDetailTrace> logBookDetailTraces) {
        try {
            for (DdbLearnLogBookDetailTrace bookDetailTrace : logBookDetailTraces) {
                final String recognizeBytesStr = bookDetailTrace.getUserRecognizeBytesStr();
                if (StringUtils.isNotBlank(recognizeBytesStr)) {
                    final byte[] bytes = recognizeBytesStr.getBytes();
                    bookDetailTrace.setUserRecognizeBytes(bytes);
                }
                bookDetailTrace.setLatestDate(new Date(bookDetailTrace.getLatestDateInMS()));
                bookDetailTrace.setCreateDate(new Date(bookDetailTrace.getCreateDateInMS()));
                bookDetailTrace.setUpdateDate(new Date(bookDetailTrace.getUpdateDateInMS()));
            }
            logBookDetailTraceMapper.batchUpdate(logBookDetailTraces);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}