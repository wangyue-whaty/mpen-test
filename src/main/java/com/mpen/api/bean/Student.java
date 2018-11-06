package com.mpen.api.bean;

import java.util.List;
/**
 * 学生个人信息，作业bean 
 * 涉及：教师端获取学生作业详情，个人信息
 */
public class Student {

    private String id;
    // 姓名
    private String name;
    // 班级ID
    private String classId;
    private String loginId;
    // 作业Id
    private String homeworkId;
    // 作业类型 1 课后作业 2 课前导学
    private int type;
    // 头像url
    private String photoUrl;
    // 评语音频url
    private List<HomeworkResourceUrl> audioUrls;
    // 评语
    private String remark;
    // 小红花数量
    private String flowerNum;
    // 提交时间
    private String submitDate;
    // 学生作业资源路径
    private List<HomeworkResourceUrl> resourceUrls;
    // 学生作业更新时间
    private String updateTime;
    private String action;
    // 阅读评级
    private Integer readRating;
    // 积分
    private Integer integral;
    // 头像
    private String photo;
    private int pariseNum;


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFlowerNum() {
        return flowerNum;
    }

    public void setFlowerNum(String flowerNum) {
        this.flowerNum = flowerNum;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public List<HomeworkResourceUrl> getAudioUrls() {
        return audioUrls;
    }

    public void setAudioUrls(List<HomeworkResourceUrl> audioUrls) {
        this.audioUrls = audioUrls;
    }

    public List<HomeworkResourceUrl> getResourceUrls() {
        return resourceUrls;
    }

    public void setResourceUrls(List<HomeworkResourceUrl> resourceUrls) {
        this.resourceUrls = resourceUrls;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getReadRating() {
        return readRating;
    }

    public void setReadRating(Integer readRating) {
        this.readRating = readRating;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public int getPariseNum() {
        return pariseNum;
    }

    public void setPariseNum(int pariseNum) {
        this.pariseNum = pariseNum;
    }
}
