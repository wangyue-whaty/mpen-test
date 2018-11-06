package com.mpen.api.bean;

import com.mpen.api.common.Constants.Sex;

/**
 * 好友bean
 * 涉及：App2.0好友模块
 */
public class Friend {
    private String id;
    private String loginId;
    private String nickName;
    private String photoUrl;
    // 好友备注
    private String remark;
    private Sex sex;
    private String school;
    private String teacher;
    private String className;
    // 好友动态数量
    private String dynamicNum;
    // 通过 未通过 标识
    private String through;
    // 验证消息
    private String validationMes;
    private String action;
    // false 否 true 是
    private boolean isFriend;
    
    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
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

    public String getDynamicNum() {
        return dynamicNum;
    }

    public void setDynamicNum(String dynamicNum) {
        this.dynamicNum = dynamicNum;
    }

    public String getThrough() {
        return through;
    }

    public void setThrough(String through) {
        this.through = through;
    }

    public String getValidationMes() {
        return validationMes;
    }

    public void setValidationMes(String validationMes) {
        this.validationMes = validationMes;
    }

    public boolean getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
