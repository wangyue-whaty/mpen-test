package com.mpen.api.service;

import com.mp.shared.common.Page;
import com.mpen.api.bean.Dynamic;
import com.mpen.api.bean.PersonDynamicInfo;
import com.mpen.api.bean.UserCoverInfo;
import com.mpen.api.bean.UserSession;
import com.mpen.api.exception.SdkException;

/**
 * 用户动态记录服务类
 * @author hzy
 * @since 2018-08-09
 */
public interface DdbUserDynamicRecordService {


    void saveDynamic(String loginId, String type, String content);

    Page<PersonDynamicInfo> pageDynamic(Dynamic dynamic, UserSession user);

    Page<PersonDynamicInfo> pagePersonalDynamic(Dynamic dynamic, UserSession user);

    UserCoverInfo getUserCover(UserSession user) throws SdkException;
}