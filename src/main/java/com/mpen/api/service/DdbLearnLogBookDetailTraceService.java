package com.mpen.api.service;

import java.util.List;

import com.mpen.api.domain.DdbLearnLogBookDetailTrace;

/**
 * <p>
 * 学情日志书籍内容统计  接口
 * </p>
 *
 * @author hzy
 * @since 2018-10-11
 */
public interface DdbLearnLogBookDetailTraceService {

    /**
     * 批量保存用户书籍学习内容详情统计
     * @param logBookDetailTraces
     * @return
     */
    boolean batchUpdate(List<DdbLearnLogBookDetailTrace> logBookDetailTraces);
	
}