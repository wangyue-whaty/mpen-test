/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mpen.api.common.RsHelper;
import com.mpen.api.exception.SdkException;
import com.sun.el.parser.ParseException;

/**
 * TODO 全局异常处理类，用于统一处理服务的异常HTTP响应.
 *
 * @author kai
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private final static String EXCEPTION_MESSAGE = "please reconnect to produce more backends connections";

    @ResponseBody
    public ResponseEntity<?> handleUnauthenticationException(Exception ex) {
        return RsHelper.error(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class })
    @ResponseBody
    public ResponseEntity<?> handleConflictException(Exception ex) {
        return RsHelper.error(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ SQLException.class, DataAccessException.class, RuntimeException.class })
    @ResponseBody
    public ResponseEntity<?> handleSqlException(Exception ex) {
        return RsHelper.error(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ IOException.class, ParseException.class, JsonParseException.class, JsonMappingException.class, HttpMessageNotReadableException.class })
    @ResponseBody
    public ResponseEntity<?> handleParseException(Exception ex) {
        return RsHelper.error(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ InvalidKeyException.class, NoSuchAlgorithmException.class })
    @ResponseBody
    public ResponseEntity<?> handleHashException(Exception ex) {
        return RsHelper.error(new Exception("Encrypt/Decrypt key is requested"), HttpStatus.LOCKED);
    }

    @ExceptionHandler({ SdkException.class, AsyncRequestTimeoutException.class })
    @ResponseBody
    public ResponseEntity<?> sdkException(Exception ex) {
        return RsHelper.error(ex, HttpStatus.OK);
    }

    @ExceptionHandler({ CannotCreateTransactionException.class })
    @ResponseBody
    public ResponseEntity<?> sqlException(Exception ex) {
        if (ex.getMessage().contains(EXCEPTION_MESSAGE)) {
            // 数据库与mysql-proxy的连接不够，mysql-proxy在重新创建连接，此异常状态设为200
            return RsHelper.error(ex, HttpStatus.OK);
        }
        return RsHelper.error(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public ResponseEntity<?> handleAnyException(Exception ex) {
        return RsHelper.error(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
