/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import com.mpen.api.bean.RomUpdate;
import com.mpen.api.exception.SdkException;
import java.util.List;

/**
 * rom升级接口.
 *
 * @author zyt
 *
 */
public interface RomUpdateService {
    /**
     * 获取Rom升级信息.
     * 
     */
    List<RomUpdate> getUpdateMessage(String penId, String version) throws SdkException;
}
