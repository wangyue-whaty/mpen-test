package com.mpen.api.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.mp.shared.common.Page;
import com.mpen.api.bean.IntegralRecord;
import com.mpen.api.bean.UserIntegralSet;
import com.mpen.api.bean.UserSession;
import com.mpen.api.domain.DdbPeCustom;

public interface IntegralService {



    boolean updatePraiseNum(UserIntegralSet userIntegralSet);

    Page<UserIntegralSet> getRankingList(UserSession userSession, IntegralRecord integralRecord);


    UserIntegralSet getRecentIntegral(UserSession userSession);

    List<UserIntegralSet> getUserRanking(UserSession userSession, String type);

    Page<UserIntegralSet> getIntegralRecord(UserSession userSession, IntegralRecord integralRecord);

    JSONArray getIntegralInstruction(UserSession userSession);
}
