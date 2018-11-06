/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import com.mpen.api.domain.DdbApp;
import com.mpen.api.exception.SdkException;

/**
 * app升级接口.
 *
 * @author zyt
 *
 */
public interface AppService {
    /**
     * 获取App升级信息.
     * 
     */
    DdbApp getAppMessageByPenId(String penId, String version) throws SdkException, Exception;
}
