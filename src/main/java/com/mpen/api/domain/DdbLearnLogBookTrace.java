package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户学习时间轨迹 domain
 * 涉及：pipeline统计用户学情某本书日课本学习，口语评测，点读轨迹信息
 *
 */
public class DdbLearnLogBookTrace implements Serializable {
  
    private static final long serialVersionUID = -8528534399048970539L;
    private String id;
    private String fkLoginId;
    private String fkBookId;
    // 学习日期
    private Date studyDate;
    // 课本学习时间
    private float bookStudyTime;
    // 口语评测时间
    private float spokenTestTime;
    // 课本点读轨迹页数
    private String learnPage;
    // 口语评测点读页数
    private String speakPage;
    // 创建时间
    private Date createDate;
    // 更新时间
    private Date updateDate;
    // 辅助字段,用于接收学习日期时间long值
    private long studyDateInMS;
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

    public Date getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(Date studyDate) {
        this.studyDate = studyDate;
    }

    public float getBookStudyTime() {
        return bookStudyTime;
    }

    public void setBookStudyTime(float bookStudyTime) {
        this.bookStudyTime = bookStudyTime;
    }

    public float getSpokenTestTime() {
        return spokenTestTime;
    }

    public void setSpokenTestTime(float spokenTestTime) {
        this.spokenTestTime = spokenTestTime;
    }

    public String getLearnPage() {
        return learnPage;
    }

    public void setLearnPage(String learnPage) {
        this.learnPage = learnPage;
    }

    public String getSpeakPage() {
        return speakPage;
    }

    public void setSpeakPage(String speakPage) {
        this.speakPage = speakPage;
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

    public long getStudyDateInMS() {
        return studyDateInMS;
    }

    public void setStudyDateInMS(long studyDateInMS) {
        this.studyDateInMS = studyDateInMS;
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
