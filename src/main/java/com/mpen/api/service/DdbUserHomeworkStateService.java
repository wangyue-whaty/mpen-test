package com.mpen.api.service;

import com.mpen.api.bean.HomeWorks;
import com.mpen.api.bean.UserSession;

/**
 * <p>
 * 学生班级作业表  接口
 * </p>
 *
 * @author hzy
 * @since 2018-07-05
 */
public interface DdbUserHomeworkStateService {

    boolean save(HomeWorks homeWorks, UserSession user);
	
}