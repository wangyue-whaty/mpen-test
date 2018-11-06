package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 学情日志书籍内容统计 domain
 * 涉及：pipeline统计某本书学情的口语评测，课本点读信息
 *
 * @author hzy
 * @since 2018-09-13
 */
public class DdbLearnLogBookDetailTrace implements Serializable {

    private static final long serialVersionUID = 1435309479928500631L;

    private String id;
    /**
     * loginId
     */
    private String fkLoginId;
    /**
     * 书id
     */
    private String fkBookId;
    /**
     * activityId
     */
    private String fkActivityId;
    /**
     * 句子md5值
     */
    private String textMd5;
    /**
     * 句子
     */
    private String text;
    /**
     * 总时长
     */
    private Float time;
    /**
     * 总点读次数
     */
    private Integer number;
    /**
     * 最高得分
     */
    private Float score;
    /**
     * 最新点读时间
     */
    private Date latestDate;
    /**
     * 类型0:课本学习1:口语评测
     */
    private String type;
    /**
     * 评测数据
     */
    private byte[] userRecognizeBytes;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新时间
     */
    private Date updateDate;
    
    /**
     * 评测数据字符串
     * @return
     */
    private String userRecognizeBytesStr;
    /**
     * 辅助字段,用于接收最新点读时间long值
     */
    private long latestDateInMS;
    /**
     * 辅助字段,用于接收创建时间long值
     */
    private long createDateInMS;
    /**
     * 辅助字段,用于接收更新时间long值
     */
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

    public String getFkActivityId() {
        return fkActivityId;
    }

    public void setFkActivityId(String fkActivityId) {
        this.fkActivityId = fkActivityId;
    }

    public String getTextMd5() {
        return textMd5;
    }

    public void setTextMd5(String textMd5) {
        this.textMd5 = textMd5;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Float getTime() {
        return time;
    }

    public void setTime(Float time) {
        this.time = time;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Date getLatestDate() {
        return latestDate;
    }

    public void setLatestDate(Date latestDate) {
        this.latestDate = latestDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getUserRecognizeBytes() {
        return userRecognizeBytes;
    }

    public void setUserRecognizeBytes(byte[] userRecognizeBytes) {
        this.userRecognizeBytes = userRecognizeBytes;
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

    public String getUserRecognizeBytesStr() {
        return userRecognizeBytesStr;
    }

    public void setUserRecognizeBytesStr(String userRecognizeBytesStr) {
        this.userRecognizeBytesStr = userRecognizeBytesStr;
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