package com.mpen.api.service;

import java.util.List;

import com.mpen.api.bean.StudyCountAssist;
import com.mpen.api.domain.DdbLearnLogBookTrace;

/**
 * <p>
 * 每日书籍学习时间轨迹表  接口
 * </p>
 *
 * @author hzy
 * @since 2018-10-11
 */
public interface DdbLearnLogBookTraceService {

    /**
     * 批量保存用户书籍学习每日轨迹
     * @param logBookTraces
     * @return
     */
    boolean batchUpdate(List<DdbLearnLogBookTrace> logBookTraces);

    /**
     * 获取用户书籍学习轨迹
     * @param studyCountAssist
     * @return
     */
    DdbLearnLogBookTrace getByLoginIdBookIdStudyDate(StudyCountAssist studyCountAssist);
	
}