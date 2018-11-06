/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;

/**
 * TODO 绑定关系返回bean.
 * 
 * @author zyt
 *
 */
public final class PenCustom implements Serializable {
    private static final long serialVersionUID = 5539191502344102412L;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
