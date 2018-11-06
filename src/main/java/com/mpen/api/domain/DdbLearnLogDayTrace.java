package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 学情日志每日汇总数据  domain
 * 涉及:pipeline统计用户日学习时长信息
 * @author hzy
 * @since 2018-09-12
 */
public class DdbLearnLogDayTrace implements Serializable {
    private static final long serialVersionUID = -2933920381453568220L;

    private String id;
    /**
     * loginId
     */
    private String fkLoginId;
    /**
     * 学习日期
     */
    private Date studyDate;
    /**
     * 总时间
     */
    private Float countTime;
    /**
     * 课本学习时间
     */
    private Float bookStudyTime;
    /**
     * 口语评测时间
     */
    private Float spokenTestTime;
    /**
     * 课后练习时间
     */
    private Float exercisesTime;
    /**
     * 课外阅读时间
     */
    private Float readTime;
    /**
     * 其他时间
     */
    private Float otherTime;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 辅助字段,学习日期long值
     */
    private long studyDateInMS;
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

    public Date getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(Date studyDate) {
        this.studyDate = studyDate;
    }

    public Float getCountTime() {
        return countTime;
    }

    public void setCountTime(Float countTime) {
        this.countTime = countTime;
    }

    public Float getBookStudyTime() {
        return bookStudyTime;
    }

    public void setBookStudyTime(Float bookStudyTime) {
        this.bookStudyTime = bookStudyTime;
    }

    public Float getSpokenTestTime() {
        return spokenTestTime;
    }

    public void setSpokenTestTime(Float spokenTestTime) {
        this.spokenTestTime = spokenTestTime;
    }

    public Float getExercisesTime() {
        return exercisesTime;
    }

    public void setExercisesTime(Float exercisesTime) {
        this.exercisesTime = exercisesTime;
    }

    public Float getReadTime() {
        return readTime;
    }

    public void setReadTime(Float readTime) {
        this.readTime = readTime;
    }

    public Float getOtherTime() {
        return otherTime;
    }

    public void setOtherTime(Float otherTime) {
        this.otherTime = otherTime;
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