package com.mpen.api.service;

import com.mpen.api.bean.PersonDynamicInfo;
import com.mpen.api.bean.UserSession;

/**
 * <p>
 * 动态点赞表  接口
 * </p>
 *
 * @author hzy
 * @since 2018-08-09
 */
public interface DdbUserDynamicPraiseService {

    PersonDynamicInfo savePraise(UserSession user, String id);
	
}