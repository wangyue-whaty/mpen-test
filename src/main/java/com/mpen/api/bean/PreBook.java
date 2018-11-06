/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;

/**
 * TODO 书资源返回bean.
 * 
 * @author zyt
 *
 */
public final class PreBook implements Serializable {
    private static final long serialVersionUID = -429683323163948901L;
    private String id;
    private long resSize;
    private String name;
    private String downloadUrl;
    private String photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getResSize() {
        return resSize;
    }

    public void setResSize(long resSize) {
        this.resSize = resSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
