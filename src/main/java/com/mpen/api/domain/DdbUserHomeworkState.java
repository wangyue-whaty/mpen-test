package com.mpen.api.domain;

import java.util.Date;

/**
 * 作业状态实体类
 * 涉及：教师端批阅作业，app2.0 学生提交作业
 */
public class DdbUserHomeworkState {

    private String id;
    // 班级表主键
    private String fkClassId;
    // 用户表主键
    private String fkLoginId;
    // 作业表主键
    private String fkHomeworkId;
    // 学生做完的作业内容
    private String content;
    // 是否提交 0 未提交 1 已提交
    private String isCommit;
    // 是否批阅(0:未批阅;1已批阅)
    private String isMarking;
    // 完成时间
    private Date uploadTime;
    // 教师评语(文字或语音的路径)
    private String remark;
    // 上传的资源路径
    private String resourceUrl;
    // 奖励数量标识
    private String rewardNum;
    
    private Date createTime;
    
    private Date updateTime;
    private String  audioRemarkUrls;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFkClassId() {
        return fkClassId;
    }

    public void setFkClassId(String fkClassId) {
        this.fkClassId = fkClassId;
    }

    public String getFkLoginId() {
        return fkLoginId;
    }

    public void setFkLoginId(String fkLoginId) {
        this.fkLoginId = fkLoginId;
    }

    public String getFkHomeworkId() {
        return fkHomeworkId;
    }

    public void setFkHomeworkId(String fkHomeworkId) {
        this.fkHomeworkId = fkHomeworkId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsCommit() {
        return isCommit;
    }

    public void setIsCommit(String isCommit) {
        this.isCommit = isCommit;
    }

    public String getIsMarking() {
        return isMarking;
    }

    public void setIsMarking(String isMarking) {
        this.isMarking = isMarking;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getRewardNum() {
        return rewardNum;
    }

    public void setRewardNum(String rewardNum) {
        this.rewardNum = rewardNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAudioRemarkUrls() {
        return audioRemarkUrls;
    }

    public void setAudioRemarkUrls(String audioRemarkUrls) {
        this.audioRemarkUrls = audioRemarkUrls;
    }
    
}
