/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.exception;

/**
 * 返回码总体说明.
 * 
 * @author yigui
 *
 */
public interface SdkExceptionConstants {

    /**
     * 接口调用成功.
     */
    public static final String OK = "1";

    public static final String ERROR = "0";

    /**
     * 接口调用成功的信息.
     */
    public static final String OK_MESSAGE = "成功";

    /**
     * 输入参数错误.
     */
    public static final String ARGUMENTS_ERR = "w400";

    /**
     * 接口调用超过模板调用上限，部分接口没有被执行.
     */
    public static final String LIMIT_PRIVILEGE_ERR = "w40301";

    /**
     * 无权限访问此接口.
     */
    public static final String NO_PRIVILEGE_ERR = "w40302";

    /**
     * 找不到对应模板.
     */
    public static final String NOT_FOUND_TEMPLATE_ERR = "w40401";

    /**
     * 没有此接口.
     */
    public static final String NOT_FOUND_METHOD_ERR = "w40402";

    /**
     * 接口地址格式错误.
     */
    public static final String URL_FORMAT_ERR = "w40403";

    /**
     * 接口错误.
     */
    public static final String INTERNAL_ERR = "w500";

    /**
     * 未知的接口调用错误.
     */
    public static final String UNKNOW_ERORR = "unknow";

}
