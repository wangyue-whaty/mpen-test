package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 统计用户书籍学习总时间 domain
 * 涉及：pipeline统计用户学情某本书的课本学习，口语评测时长信息
 * 
 */
public class DdbLearnLogBookSumTrace implements Serializable {

    private static final long serialVersionUID = -9220433467832447650L;
    private String id;
    private String fkLoginId;
    private String fkBookId;
    // 课本学习总时长
    private float sumTime;
    // 口语评测总时间
    private float spokenTestTime;
    // 最新学习时间
    private Date latestDate;
    // 创建时间
    private Date createDate;
    // 更新时间
    private Date updateDate;
    // 辅助字段,用于接收最新学习时间long值
    private long latestDateInMS;
    // 辅助字段,用于接收创建时间long值
    private long createDateInMS;
    // 辅助字段,用于接收更新时间long值
    private long updateDateInMS;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFkLoginId() {
        return fkLoginId;
    }

    public void setFkLoginId(String fkLoginId) {
        this.fkLoginId = fkLoginId;
    }

    public String getFkBookId() {
        return fkBookId;
    }

    public void setFkBookId(String fkBookId) {
        this.fkBookId = fkBookId;
    }

    public float getSumTime() {
        return sumTime;
    }

    public void setSumTime(float sumTime) {
        this.sumTime = sumTime;
    }

    public float getSpokenTestTime() {
        return spokenTestTime;
    }

    public void setSpokenTestTime(float spokenTestTime) {
        this.spokenTestTime = spokenTestTime;
    }

    public Date getLatestDate() {
        return latestDate;
    }

    public void setLatestDate(Date latestDate) {
        this.latestDate = latestDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public long getLatestDateInMS() {
        return latestDateInMS;
    }

    public void setLatestDateInMS(long latestDateInMS) {
        this.latestDateInMS = latestDateInMS;
    }

    public long getCreateDateInMS() {
        return createDateInMS;
    }

    public void setCreateDateInMS(long createDateInMS) {
        this.createDateInMS = createDateInMS;
    }

    public long getUpdateDateInMS() {
        return updateDateInMS;
    }

    public void setUpdateDateInMS(long updateDateInMS) {
        this.updateDateInMS = updateDateInMS;
    }
}
