package com.mpen.api.service;

import com.mpen.api.bean.MyMedal;
import com.mpen.api.bean.UserMedals;
import com.mpen.api.bean.UserSession;

/**
 * 勋章服务类
 *
 */
public interface MedalService {

    void saveMedal(String loginId, String type, int number);

    boolean updateMedalWear(UserSession userSession, MyMedal myMedal);

    UserMedals getMyMedal(UserSession userSession);
    

}
