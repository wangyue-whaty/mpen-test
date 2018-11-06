/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;
import java.util.Date;

public final class ActivityStudyDetail implements Serializable {
    private static final long serialVersionUID = -379187199735693204L;
    // 学习总次数.
    private Integer countTimes;
    // 最近学习日期.
    private Date date;
    // 活动id.
    private String fkActivityId;
    // 学习时长.
    private Float time;
    // 最大分数.
    private float score;
    // 口语评测文本.
    private String text;
    // 云知声口语评测原始数据
    private byte[] userRecognizeBytes;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Integer getCountTimes() {
        return countTimes;
    }

    public Date getDate() {
        return date;
    }

    public Float getTime() {
        return time;
    }

    public void setCountTimes(Integer countTimes) {
        this.countTimes = countTimes;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFkActivityId() {
        return fkActivityId;
    }

    public void setFkActivityId(String fkActivityId) {
        this.fkActivityId = fkActivityId;
    }

    public void setTime(Float time) {
        this.time = time;
    }

    public byte[] getUserRecognizeBytes() {
        return userRecognizeBytes;
    }
    
    public void setUserRecognizeBytes(byte[] userRecognizeBytes) {
        this.userRecognizeBytes = userRecognizeBytes;
    }

}
