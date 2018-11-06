/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.domain;

import java.io.Serializable;

import java.util.Date;

public final class DdbRomUpdate implements Serializable {
    private static final long serialVersionUID = 2975348394122256644L;
    private String id;
    // 升级前的版本
    private String fromVersionId;
    // 升级到的版本
    private String toVersionId;
    // 升级信息描述
    private String description;
    // 下载地址
    private String downloadUrl;
    // 文件大小
    private String fileSize;
    // 文件MD5值
    private String fileMd5;
    // 创建时间
    private Date createtime;
    private String item;
    //是否为强制升级（0否，1是）
    private int isForce;

    public int getIsForce() {
        return isForce;
    }

    public void setIsForce(int isForce) {
        this.isForce = isForce;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromVersionId() {
        return fromVersionId;
    }

    public void setFromVersionId(String fromVersionId) {
        this.fromVersionId = fromVersionId;
    }

    public String getToVersionId() {
        return toVersionId;
    }

    public void setToVersionId(String toVersionId) {
        this.toVersionId = toVersionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

}
