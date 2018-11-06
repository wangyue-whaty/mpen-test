package com.mpen.api.bean;

import java.util.List;

/**
 * 教师端以及APP2.0所涉及: 班级信息 bean
 */
public class ClassInfo {
    private String id;
    // 学校
    private String school;
    // 年级班级名字
    private String className;
    // 老师名字
    private String englishTeacher;
    // 邀请码
    private String invitationCode;
    // 老师用户loginId
    private String fkLoginId;
    private String action;
    // 真实姓名
    private String trueName;
    // 教材版本
    private String textbookEdition;
    // 学生数量
    private Integer studentNums;
    // 作业ID
    private String homeWorkId;
    // 状态 0 空 1 已截止 2 未截止
    private Integer state;
    // 阅读等级 0 积分排行 1
    private int type;
    private String classNumber;
    // 作业
    private List<ClassAssignments> homeworks;
    // 布置作业的时间
    private String date;
    // 未提交人数
    private int notSubmit;
    // 已提交人数
    private int submit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEnglishTeacher() {
        return englishTeacher;
    }

    public void setEnglishTeacher(String englishTeacher) {
        this.englishTeacher = englishTeacher;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFkLoginId() {
        return fkLoginId;
    }

    public void setFkLoginId(String fkLoginId) {
        this.fkLoginId = fkLoginId;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getTextbookEdition() {
        return textbookEdition;
    }

    public void setTextbookEdition(String textbookEdition) {
        this.textbookEdition = textbookEdition;
    }

    public int getStudentNums() {
        return studentNums;
    }

    public void setStudentNums(int studentNums) {
        this.studentNums = studentNums;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getHomeWorkId() {
        return homeWorkId;
    }

    public void setHomeWorkId(String homeWorkId) {
        this.homeWorkId = homeWorkId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public void setStudentNums(Integer studentNums) {
        this.studentNums = studentNums;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<ClassAssignments> getHomeworks() {
        return homeworks;
    }

    public void setHomeworks(List<ClassAssignments> homeworks) {
        this.homeworks = homeworks;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNotSubmit() {
        return notSubmit;
    }

    public void setNotSubmit(int notSubmit) {
        this.notSubmit = notSubmit;
    }

    public int getSubmit() {
        return submit;
    }

    public void setSubmit(int submit) {
        this.submit = submit;
    }

}
