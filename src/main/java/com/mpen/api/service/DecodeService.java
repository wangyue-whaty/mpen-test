/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import com.mp.shared.common.CodeInfo;
import com.mp.shared.common.QuickCodeInfo;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;

public interface DecodeService {
    /**
     * 解码获取点读资源.
     * 
     * @throws Exception
     * 
     */
    CodeInfo getCodeInfo(QuickCodeInfo quickCodeInfo) throws SdkException, CacheException, Exception;

    /**
     * 查询视频资源.
     * 
     * @throws Exception
     * 
     */
    boolean getVideo(CodeInfo codeInfo);

    CodeInfo getOralTestCodeInfo(String bookId, int idx) throws Exception;
}
