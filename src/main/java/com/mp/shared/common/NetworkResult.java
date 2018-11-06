/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mp.shared.common;

/**
 * 统一返回结果封装: 1.code: 业务层面的返回码 2.msg: 业务层面的返回消息 3.data: 业务数据字段
 * 
 * @author kai
 * @param <T>  返回数据类型
 *
 *
 */
public final class NetworkResult<T> {
    public static final String MSG_SUCCESS = "Success";
    public static final String MSG_ERROR = "Error";

    public static final String MSG_SUCCESS_CODE = "200";
    public static final String BAD_REQUEST_ERROR_CODE = "400";
    public static final String BAD_REQUEST_ERROR_MSG = "Bad request!";
    public static final String ACCESS_DENIED_ERROR_CODE = "401";
    public static final String ACCESS_DENIED_ERROR_MSG = "Access denied!";
    public static final String ACCESS_FORBIDDEN_ERROR_CODE = "403";
    public static final String ACCESS_FORBIDDEN_ERROR_MSG = "Access forbidden!";
    public static final String NO_MACHING_ERROR_CODE = "404";
    public static final String NO_MACHING_ERROR_MSG = "No maching resource!";

    private String errorCode;
    private String errorMsg;
    private Long timeStamp;
    private T data;

    public NetworkResult() {
        this.timeStamp = System.currentTimeMillis(); //new Date().getTime();
    }

    /**
     * @return 结果是不是成功
     */
    public boolean isGood() {
        return MSG_SUCCESS_CODE.equals(errorCode);
    }

    /**
     * constructor for a GOOD result
     * @param data
     */
    public NetworkResult(T data) {
    	errorCode = MSG_SUCCESS_CODE;
    	errorMsg = MSG_SUCCESS;
    	this.data = data;
    }
    
    /**
     * constructor for an ERROR result
     * @param errorCode the errorCode, usually one of pre-defined ..._CODE
     * @param errorMsg the error message, usually one of pre-defined ..._MSG, or an exception message
     */
    public NetworkResult(String errorCode, String errorMsg) {
    	this.errorCode = errorCode;
    	this.errorMsg = errorMsg;
    	this.data = null;
    }
    
    /**
     * below are getters and setters
     */
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
