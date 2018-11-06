/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.exception;

/**
 * cache 异常类. 
 *
 * @author kai
 *
 */
public class CacheException extends Exception {

    private static final long serialVersionUID = -5053846456349091769L;

    /**
     * 构造一个cache异常. 
     *
     * @param message 错误消息
     */
    public CacheException(String message) {
        super(message);
    }

    /**
     * 构造一个cache异常. 
     *
     * @param cause Throwable对象
     */
    public CacheException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个cache异常. 
     *
     * @param message 错误消息
     * @param cause Throwable对象
     */
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

}
