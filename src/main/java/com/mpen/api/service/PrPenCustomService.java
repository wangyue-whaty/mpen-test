/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import com.mp.shared.common.SuccessResult;
import com.mpen.api.bean.Pen;
import com.mpen.api.bean.PenCustom;
import com.mpen.api.bean.UserSession;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;

/**
 * PrPenCustomService.
 * 
 * @author zyt
 *
 */
public interface PrPenCustomService {
    /**
     * 保存绑定关系.
     * @throws CacheException 
     * 
     */
    PenCustom saveBindRelationship(Pen pen, UserSession user) throws SdkException, CacheException;

    /**
     * 校验绑定关系.
     * 
     */
    SuccessResult checkBindRelationship(String identifiaction, UserSession user) throws SdkException;

    /**
     * 解除绑定关系.
     * @throws CacheException 
     * 
     */
    Boolean unBindRelationship(Pen pen, UserSession user, String agentOperateKey) throws SdkException, CacheException;
}
