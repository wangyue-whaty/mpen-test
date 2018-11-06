package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 阅读分级考试详情返回对象实体类
 * @author wangyue
 *
 */
public class DdbRecordExamDetail implements Serializable{
    private static final long serialVersionUID = 1L;
    // 考试Id
    private String examId;
	// 用户Id
    private String loginId;
    // 开始时间
    private String createTime;
    // 时长
    private String duration;
    // 等级
    private Integer level;
    
    public String getExamId() {
        return examId;
    }
	
    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

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
    
}
