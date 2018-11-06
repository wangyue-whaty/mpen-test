/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import com.mpen.api.bean.UserSession;
import com.mpen.api.exception.SdkException;
import java.util.Date;

public interface AnrFileService {
    /**
     * 保存Anr文件.
     * 
     */
    Boolean save(String filePath, String version, Date time, String penId, UserSession user) throws SdkException;
}
