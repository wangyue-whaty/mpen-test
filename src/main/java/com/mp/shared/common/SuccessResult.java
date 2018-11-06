/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mp.shared.common;

import java.io.Serializable;

/**
 * TODO 公共成功返回bean.
 * 
 * @author zyt
 *
 */
public class SuccessResult implements Serializable {
    private static final long serialVersionUID = -4663798377450389899L;
    private Boolean success;
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public SuccessResult() {
        super();
    }

    public SuccessResult(Boolean success) {
        super();
        this.success = success;
    }

}
