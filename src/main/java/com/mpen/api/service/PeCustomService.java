/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import com.mp.shared.common.SuccessResult;
import com.mpen.api.bean.Label;
import com.mpen.api.bean.User;
import com.mpen.api.bean.UserSession;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;
import java.util.List;

/**
 * PeCustomService.
 *
 * @author kai
 *
 */
public interface PeCustomService {
    /**
     * 获取用户信息.
     * 
     */
    DdbPeCustom getById(String id);

    /**
     * 获取用户信息.
     * 
     */
    DdbPeCustom getByLoginId(String loginId);

    /**
     * 保存用户信息.
     * 
     */
    String create(DdbPeCustom peCustom);

    /**
     * 删除用户信息.
     * 
     */
    void delete(String id);

    /**
     * 更改用户信息.
     * @throws CacheException 
     * 
     */
    SuccessResult update(UserSession userSession, User user) throws SdkException, CacheException;

    /**
     * 获取用户标签.
     * 
     */
    List<Label> getCustomLabels(UserSession user);
    
    SuccessResult saveAddress(User user, UserSession userSession) throws CacheException;

    String updateCover(String cover, UserSession user);
}
