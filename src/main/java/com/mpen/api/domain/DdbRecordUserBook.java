/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

import com.mp.shared.common.Code;

public final class DdbRecordUserBook implements Serializable {
    private static final long serialVersionUID = -582695990611437515L;
    private String id;
    private String loginId;
    private String fkBookId;
    private String code;
    private Code.Type codeType;
    private Date clickTime;
    private Date endTime;
    // 上传时间
    private long time;
    // 资源类型（0，语音；1视频）
    private String type;
    // 语音类型（0，英文；1中文）
    private String voiceType;
    // 功能类型（0，普通点读；1，跟读对比；2，口语评测）
    private String function;
    private float x;
    private float y;

    // 所属活动Id
    private String fkActivityId;
    // 所属页码
    private String page;
    // 口语评测文本
    private String text;
    // 口语评测分数
    private float score;
    private String isRead;
    //云知声口语评测原始数据
    private byte[] userRecognizeBytes;
    

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public Code.Type getCodeType() {
        return codeType;
    }

    public void setCodeType(Code.Type codeType) {
        this.codeType = codeType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getVoiceType() {
        return voiceType;
    }

    public void setVoiceType(String voiceType) {
        this.voiceType = voiceType;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Date getClickTime() {
        return clickTime;
    }

    public void setClickTime(Date clickTime) {
        this.clickTime = clickTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getFkBookId() {
        return fkBookId;
    }

    public void setFkBookId(String fkBookId) {
        this.fkBookId = fkBookId;
    }

    public String getFkActivityId() {
        return fkActivityId;
    }

    public void setFkActivityId(String fkActivityId) {
        this.fkActivityId = fkActivityId;
    }

    public byte[] getUserRecognizeBytes() {
        return userRecognizeBytes;
    }

    public void setUserRecognizeBytes(byte[] userRecognizeBytes) {
        this.userRecognizeBytes = userRecognizeBytes;
    }

}
