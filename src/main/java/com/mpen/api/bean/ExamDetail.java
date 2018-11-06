package com.mpen.api.bean;

import java.io.Serializable;

/**
 * 阅读分级考试详情请求对象实体类
 * @author wangyue
 *
 */
public class ExamDetail implements Serializable{
    private static final long serialVersionUID = 1L;
    private String action;
    // 考试Id
    private String examId;
    // 开始时间
    private String createTime;
    // 时长
    private String duration;
    // 等级
    private Integer level;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }
}
