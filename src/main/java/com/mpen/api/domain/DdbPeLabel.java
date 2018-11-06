/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.domain;

import java.io.Serializable;

public final class DdbPeLabel implements Serializable {
    private static final long serialVersionUID = 6630666838941542382L;
    private String id;
    private String name;
    private String code;
    private String introduce;
    private String flagLabelType;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getFlagLabelType() {
        return flagLabelType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setFlagLabelType(String flagLabelType) {
        this.flagLabelType = flagLabelType;
    }
}
