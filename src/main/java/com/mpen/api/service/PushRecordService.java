/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import com.mp.shared.common.SuccessResult;
import com.mpen.api.bean.Message;
import com.mpen.api.bean.UserSession;

public interface PushRecordService {
    /**
     * 推送消息.
     * 
     */
    SuccessResult save(Message message, UserSession userSession) throws Exception;
}
