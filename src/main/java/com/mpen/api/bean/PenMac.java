/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;

import com.mpen.api.common.Constants.PenType;


public final class PenMac implements Serializable {
    private static final long serialVersionUID = 2998484489521683047L;
    private String name;
    private String macAndroid;
    private String macIOS;
    private String serialNumber;
    private PenType penType;
    /** 是否为教师笔  1:是；0:不是*/
    private Integer isTeacher;

    public final PenType getPenType() {
        return penType;
    }

    public final void setPenType(PenType penType) {
        this.penType = penType;
    }

    public final String getSerialNumber() {
        return serialNumber;
    }

    public final void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAndroid() {
        return macAndroid;
    }

    public void setMacAndroid(String macAndroid) {
        this.macAndroid = macAndroid;
    }

    public String getMacIOS() {
        return macIOS;
    }

    public void setMacIOS(String macIOS) {
        this.macIOS = macIOS;
    }

    public Integer getIsTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(Integer isTeacher) {
        this.isTeacher = isTeacher;
    }
}
