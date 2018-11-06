/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mp.shared.common;

import java.io.Serializable;

import java.util.Date;

public class DdbApp implements Serializable {
    private static final long serialVersionUID = 4853505875680954732L;
    private String id;
    // 标题：作为更新的标识
    private String title;
    // 摘要：应用的更新说明，其内容将会在应用检查新版本的时候展现给用户，所以要认真填写
    private String item;
    // 数据1：应用版本号，用来app判断是否为最新版本，请填写正确的版本号
    private String versionName;
    // 应用序列号
    private Integer versionCode;
    // 数据2：是否为强制更新：0不强制/1强制。如果为强制更新，则用户不更新就无法继续使用当前版本app。若非紧急BUG，请尽量不要强制用户更新
    private String isForce;
    // 数据3：是否为重要更新：0不重要/1重要。若为重要更新，在WIFI下进入app后会自动弹出提示，如果为不重要更新，则只会在相应的位置上有红点提示。大版本更新时，建议设置成重要更新。
    private String isImportant;
    // 应用下载路径
    private String fileUrl;
    // 应用大小：非WIFI时候更新，会提示占用用户多少流量，请填写
    private String fileSize;
    // 是否有效: 1有效/0无效
    private String isValid;
    // 灰度升级标记
    private String label;
    // 创建时间
    private Date time;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getIsForce() {
        return isForce;
    }

    public void setIsForce(String isForce) {
        this.isForce = isForce;
    }

    public String getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(String isImportant) {
        this.isImportant = isImportant;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
}
