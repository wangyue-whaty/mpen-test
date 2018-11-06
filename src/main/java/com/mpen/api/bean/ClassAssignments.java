package com.mpen.api.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

/**
 * 教师端：班级作业 bean
 * 
 */

public class ClassAssignments {
    private String id;
    // 作业类型
    private int type;
    // 单元
    private String title;
    // 单元名称
    private String content;
    // 班级
    private String className;
    // 截止日期
    private String endDate;
    // 创建时间
    private String createDate;
    // 审核类型 0 未批阅 1 已批阅 2 未提交 3 已提交
    private String reviewType;
    // 已经批阅人数
    private int review;
    // 未批阅人数
    private int noReview;
    // 未提交人数
    private int notSubmit;
    // 已提交人数
    private int submit;
    // 班级Id
    private String fkClassId;
    // 班级学生姓名
    private String StudentName;
    // 班级学生头像url
    private String imageUrl;
    // 班级学生登录ID
    List<String> loginIds;
    private String action;
    // 创建时间 精确到天
    private String createDayDate;
    // 需要学生回答
    private ArrayList<String> submits;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public int getNoReview() {
        return noReview;
    }

    public void setNoReview(int noReview) {
        this.noReview = noReview;
    }

    public int getNotSubmit() {
        return notSubmit;
    }

    public void setNotSubmit(int notSubmit) {
        this.notSubmit = notSubmit;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFkClassId() {
        return fkClassId;
    }

    public void setFkClassId(String fkClassId) {
        this.fkClassId = fkClassId;
    }

    public int getSubmit() {
        return submit;
    }

    public void setSubmit(int submit) {
        this.submit = submit;
    }

    public List<String> getLoginIds() {
        return loginIds;
    }

    public void setLoginIds(List<String> loginIds) {
        this.loginIds = loginIds;
    }

    public String getCreateDayDate() {
        return createDayDate;
    }

    public void setCreateDayDate(String createDayDate) {
        this.createDayDate = createDayDate;
    }

    public ArrayList<String> getSubmits() {
        return submits;
    }

    public void setSubmits(ArrayList<String> submits) {
        this.submits = submits;
    }

}
