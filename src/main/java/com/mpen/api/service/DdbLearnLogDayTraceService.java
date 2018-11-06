package com.mpen.api.service;

import java.util.List;

import com.mpen.api.domain.DdbLearnLogDayTrace;

/**
 * <p>
 * 学情日志每日汇总数据  接口
 * </p>
 *
 * @author hzy
 * @since 2018-10-11
 */
public interface DdbLearnLogDayTraceService {

    /**
     * 批量保存用户每日学习分布
     * @param logDayTraces
     */
    boolean batchUpdate(List<DdbLearnLogDayTrace> logDayTraces);
	
}