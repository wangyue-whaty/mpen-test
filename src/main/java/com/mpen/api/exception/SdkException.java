/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.exception;

/**
 * sdk异常基础类.
 * 
 * @author yigui
 *
 */
public class SdkException extends Exception implements SdkExceptionConstants {

    private static final long serialVersionUID = -4948805126720567855L;

    public SdkException(String str) {
        this(str, null, null, null);
    }

    public SdkException(String str, String errorcode, Throwable cause, Object target) {
        super(str, cause);
    }

}
