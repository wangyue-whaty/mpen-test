/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.domain;

import java.io.Serializable;

import com.mp.shared.common.HotArea;

/**
 * ORM对象：DdbResourceVideo.
 *
 * @author zyt
 *
 */
public final class DdbResourceVideo implements Serializable {
    private static final long serialVersionUID = 3071625161064353889L;
    private String id;
    private Integer code;
    private String name;
    private String url;
    private String panLink;
    private String fkBookId;
    private HotArea.VideoType type;

    private String activityId;
    private String item;
    private String unitName;
    
    public HotArea.VideoType getType() {
        return type;
    }

    public void setType(HotArea.VideoType type) {
        this.type = type;
    }

    public String getFkBookId() {
        return fkBookId;
    }

    public void setFkBookId(String fkBookId) {
        this.fkBookId = fkBookId;
    }

    public String getPanLink() {
        return panLink;
    }

    public void setPanLink(String panLink) {
        this.panLink = panLink;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

}
