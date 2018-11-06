/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mp.shared.common.NetworkResult;

/**
 * 统一封装Rest API的返回结果.
 *
 * @author kai
 *
 */
public final class RsHelper<T> {

    private static final Logger logger = LoggerFactory.getLogger(RsHelper.class);

    /**
     * 正常结果的统一封装.
     *
     * @param data
     *            返回的结果数据
     * @return Result对象
     */
    public static <T> NetworkResult<T> success(T data) {
        return new NetworkResult<T>(data);
    }

    /**
     * 异常结果的统一返回.
     *
     * @param throwable
     *            异常的throwable对象
     * @param status
     *            希望返回的HTTP状态码
     * @return ResponseEntity对象
     */
    public static <T> ResponseEntity<NetworkResult<T>> error(Throwable throwable, HttpStatus status) {
        final String errorMsg = throwable == null ? NetworkResult.BAD_REQUEST_ERROR_MSG : throwable.getMessage();
        logger.error("An error occurred.", throwable);
        //默认状态码400
        String code  = NetworkResult.BAD_REQUEST_ERROR_CODE;
        //根据errorMsg匹配对应的状态码
        if (StringUtils.isNotBlank(errorMsg)) {
            switch (errorMsg) {
            case NetworkResult.ACCESS_DENIED_ERROR_MSG:
                code = NetworkResult.ACCESS_DENIED_ERROR_CODE;
                break;
            case NetworkResult.ACCESS_FORBIDDEN_ERROR_MSG:
                code = NetworkResult.ACCESS_FORBIDDEN_ERROR_CODE;
                break;
            case NetworkResult.NO_MACHING_ERROR_MSG:
                code = NetworkResult.NO_MACHING_ERROR_CODE;
                break;
            default:
                break;
            }
        }
        final NetworkResult<T> rs = new NetworkResult<>(code, errorMsg);
        return response(rs, status);
    }

    /**
     * 错误结果的返回.
     * 
     */
    public static <T> NetworkResult<T> error(String errorCode) {
        String msg = "";
        if (NetworkResult.ACCESS_DENIED_ERROR_CODE.equals(errorCode)) {
            msg = NetworkResult.ACCESS_DENIED_ERROR_MSG;
        } else if (NetworkResult.ACCESS_FORBIDDEN_ERROR_CODE.equals(errorCode)) {
            msg = NetworkResult.ACCESS_FORBIDDEN_ERROR_MSG;
        } else if (NetworkResult.NO_MACHING_ERROR_CODE.equals(errorCode)) {
            msg = NetworkResult.NO_MACHING_ERROR_MSG;
        } else {
            errorCode = NetworkResult.BAD_REQUEST_ERROR_CODE;
            msg = NetworkResult.BAD_REQUEST_ERROR_MSG;
        }
        logger.error("An error occurred: " + msg);
        return error(errorCode, msg);
    }

    /**
     * 错误结果的返回.
     * 
     */
    public static <T> NetworkResult<T> error(String errorCode, String msg) {
        logger.error("An error occurred: " + msg);
        return new NetworkResult<T>(errorCode, msg);
    }

    /**
     * 错误结果的返回.
     * 
     */
    public static <T> NetworkResult<T> error() {
        return error(NetworkResult.BAD_REQUEST_ERROR_CODE, NetworkResult.BAD_REQUEST_ERROR_MSG);
    }

    private static <T> ResponseEntity<T> response(T body, HttpStatus status) {
        return new ResponseEntity<T>(body, new HttpHeaders(), status);
    }
}
