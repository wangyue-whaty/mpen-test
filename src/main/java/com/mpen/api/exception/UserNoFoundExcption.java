/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.exception;

/**
 * 没有找到对应用户.
 * 
 * @author kai
 *
 */
public class UserNoFoundExcption extends Exception {

    private static final long serialVersionUID = 3880507764532214689L;

    public UserNoFoundExcption(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public UserNoFoundExcption(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}
