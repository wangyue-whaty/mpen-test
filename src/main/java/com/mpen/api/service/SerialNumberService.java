/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import com.mpen.api.bean.Pen;
import com.mpen.api.exception.SdkException;

public interface SerialNumberService {
    /**
     * 保存序列号.
     * 
     */
    Boolean saveRelationship(Pen pen) throws SdkException;

}
