package com.mpen.api.bean;

import java.util.List;
/**
 * 教师端以及app2.0所涉及:班级作业详情 bean
 *
 */
public class HomeWorkDetail {
    // 作业id
    private String id;
    // 班级id
    private String fkClassId;
    // 课前预习内容
    private PreviewContent content;
    // 课后作业标题
    private String title;
    // 作业状态
    private String status;
    // 教师评论
    private String comment;
    // 完成时间
    private Long uploadTime;
    // 批阅状态
    private String isMarking;
    // 资源路径
    private List<HomeworkResourceUrl> resourceUrl;
    // 奖励红花个数
    private String rewardNum;
    // 课后作业课本学习详情
    private List<ClassAfterLearnDetail> learnDetails;
    // 课后作业听说训练详情
    private List<ClassAfterExamDetail> examResult;
    // 课本学习内容
    List<TextBookLearning> textBookLeanings;
    // 班级课后作业详情
    private ClassLearnContent classTextBookLeanings;
    // 音频url
    private List<HomeworkResourceUrl> audioUrls;

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

    public PreviewContent getContent() {
        return content;
    }

    public void setContent(PreviewContent content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getIsMarking() {
        return isMarking;
    }

    public void setIsMarking(String isMarking) {
        this.isMarking = isMarking;
    }

    public void setClassTextBookLeanings(ClassLearnContent classTextBookLeanings) {
        this.classTextBookLeanings = classTextBookLeanings;
    }

    public void setAudioUrls(List<HomeworkResourceUrl> audioUrls) {
        this.audioUrls = audioUrls;
    }

    public String getRewardNum() {
        return rewardNum;
    }

    public void setRewardNum(String rewardNum) {
        this.rewardNum = rewardNum;
    }

    public List<ClassAfterLearnDetail> getLearnDetails() {
        return learnDetails;
    }

    public void setLearnDetails(List<ClassAfterLearnDetail> learnDetails) {
        this.learnDetails = learnDetails;
    }

    public List<ClassAfterExamDetail> getExamResult() {
        return examResult;
    }

    public void setExamResult(List<ClassAfterExamDetail> examResult) {
        this.examResult = examResult;
    }

    public List<TextBookLearning> getTextBookLeanings() {
        return textBookLeanings;
    }

    public void setTextBookLeanings(List<TextBookLearning> textBookLeanings) {
        this.textBookLeanings = textBookLeanings;
    }

    public ClassLearnContent getClassTextBookLeanings() {
        return classTextBookLeanings;
    }

    public List<HomeworkResourceUrl> getAudioUrls() {
        return audioUrls;
    }

    public List<HomeworkResourceUrl> getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(List<HomeworkResourceUrl> resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
}
