/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

/** 用户和笔的绑定关系. */
public final class DdbPrPenCustom implements Serializable {
    private static final long serialVersionUID = -2068600139225298367L;
    private String id;
    // 绑定时间
    private Date createDatetime;
    private String fkCustomId;
    private String fkPenId;
    // 解除绑定时间
    private Date deleteTime;
    // 是否有效（0：无效；1：有效）
    private int isValid;
    
    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getFkCustomId() {
        return fkCustomId;
    }

    public void setFkCustomId(String fkCustomId) {
        this.fkCustomId = fkCustomId;
    }

    public String getFkPenId() {
        return fkPenId;
    }

    public void setFkPenId(String fkPenId) {
        this.fkPenId = fkPenId;
    }

}