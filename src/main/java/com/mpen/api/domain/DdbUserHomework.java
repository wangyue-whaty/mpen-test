package com.mpen.api.domain;

import java.util.Date;
import java.util.List;

import com.mpen.api.bean.HomeworkResourceUrl;

/*
 * 布置作业实体类
 * 涉及：教师端布置作业
 */
public class DdbUserHomework {

    private String id;
    // 作业类型(0 课前导学,1 课后作业)
    private int type;
    // 作业内容(json)
    private String content;
    // 班级表的主键
    private String fkClassId;
    // 截止日期
    private Date endDate;
    // 创建日期
    private Date createDate;
    // 教师留言
    private String remark;
    // 教师用户ID
    private String fkLoginId;

    // ---------学生作业-----
    // 是否提交 0 未提交 1 已提交
    private Integer isCommit;
    // 作业上传时间
    private Date uploadTime;
    // 评论内容
    private String comment;
    // 是否批阅 0 未批阅 1 已批阅
    private String isMarking;
    // 上传资源路径
    private String resourceUrl;
    // 奖励数量
    private String rewardNum;
    // 音频路径
    private List<HomeworkResourceUrl> audioUrls;
    // --------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFkClassId() {
        return fkClassId;
    }

    public void setFkClassId(String fkClassId) {
        this.fkClassId = fkClassId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFkLoginId() {
        return fkLoginId;
    }

    public void setFkLoginId(String fkLoginId) {
        this.fkLoginId = fkLoginId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getIsCommit() {
        return isCommit;
    }

    public void setIsCommit(Integer isCommit) {
        this.isCommit = isCommit;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIsMarking() {
        return isMarking;
    }

    public void setIsMarking(String isMarking) {
        this.isMarking = isMarking;
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

    public List<HomeworkResourceUrl> getAudioUrls() {
        return audioUrls;
    }

    public void setAudioUrls(List<HomeworkResourceUrl> audioUrls) {
        this.audioUrls = audioUrls;
    }

}
