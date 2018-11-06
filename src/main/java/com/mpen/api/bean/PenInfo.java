/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;
import java.util.List;

import com.mpen.api.common.Constants.PenType;

/**
 * TODO 笔信息参数bean.
 * 
 * @author zyt
 *
 */
public final class PenInfo implements Serializable {
    private static final long serialVersionUID = -5793217113515767169L;
    private String action;
    private String version;
    private String code;
    private String macAddress;
    private String publicKey;
    private String activeAdd;
    private String id;
    private String result;
    private PenType type;
    // 存储容量
    private long storageCapacity;
    // 文件名列表
    private List<String> fileNameList;

    public PenType getType() {
        return type;
    }

    public void setType(PenType type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getActiveAdd() {
        return activeAdd;
    }

    public void setActiveAdd(String activeAdd) {
        this.activeAdd = activeAdd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(long storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public List<String> getFileNameList() {
        return fileNameList;
    }

    public void setFileNameList(List<String> fileNameList) {
        this.fileNameList = fileNameList;
    }
}
