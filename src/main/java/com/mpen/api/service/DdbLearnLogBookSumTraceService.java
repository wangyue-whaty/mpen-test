package com.mpen.api.service;

import java.util.List;

import com.mpen.api.domain.DdbLearnLogBookSumTrace;

/**
 * <p>
 * 学习书籍列表  接口
 * </p>
 *
 * @author hzy
 * @since 2018-10-11
 */
public interface DdbLearnLogBookSumTraceService {

    /**
     * 批量保存用户学习书籍列表统计
     * @param logBookSumTraces
     * @return
     */
    boolean batchUpdate(List<DdbLearnLogBookSumTrace> logBookSumTraces);
	
}