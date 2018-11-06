/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.domain;

import java.io.Serializable;

public final class DdbPeCustom implements Serializable {

    private static final long serialVersionUID = -7596099754399969670L;

    private String id;

    /**
     * 登录号.
     */
    private String loginId;

    /**
     * SSO_USER外键.
     */
    private String fkUserId;

    /**
     * 年级.
     */
    private String fkLabelId;

    private String fkLabelOneId;

    private String fkLabelTwoId;

    private String fkLabelThreeId;

    /**
     * 真实姓名.
     */
    private String trueName;

    /**
     * 用户编号.
     */
    private String regNo;

    /**
     * 证件号码.
     */
    private String cardNo;

    /**
     * 通讯地址.
     */
    private String address;

    /**
     * 邮编.
     */
    private String zipAddress;

    /**
     * 工作单位.
     */
    private String workUnit;

    /**
     * 手机号码.
     */
    private String mobilephone;

    /**
     * 职务.
     */
    private String post;

    /**
     * 联系电话.
     */
    private String phone;

    /**
     * 电子邮箱.
     */
    private String email;

    /**
     * QQ号码.
     */
    private String qq;

    /**
     * 注册日期.
     */
    private String registrationDate;

    /**
     * 生日.
     */
    private String brithday;
    /**
     * 性别.
     */
    private String flagGender;

    /**
     * 昵称.
     */
    private String nickName;
    private Integer age;

    private String school;
    // 封面
    private String cover;

    public Integer getAge() {
        return age;
    }

    public String getSchool() {
        return school;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(String fkUserId) {
        this.fkUserId = fkUserId;
    }

    public String getFkLabelId() {
        return fkLabelId;
    }

    public void setFkLabelId(String fkLabelId) {
        this.fkLabelId = fkLabelId;
    }

    public String getFkLabelOneId() {
        return fkLabelOneId;
    }

    public void setFkLabelOneId(String fkLabelOneId) {
        this.fkLabelOneId = fkLabelOneId;
    }

    public String getFkLabelTwoId() {
        return fkLabelTwoId;
    }

    public void setFkLabelTwoId(String fkLabelTwoId) {
        this.fkLabelTwoId = fkLabelTwoId;
    }

    public String getFkLabelThreeId() {
        return fkLabelThreeId;
    }

    public void setFkLabelThreeId(String fkLabelThreeId) {
        this.fkLabelThreeId = fkLabelThreeId;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipAddress() {
        return zipAddress;
    }

    public void setZipAddress(String zipAddress) {
        this.zipAddress = zipAddress;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getBrithday() {
        return brithday;
    }

    public void setBrithday(String brithday) {
        this.brithday = brithday;
    }

    public String getFlagGender() {
        return flagGender;
    }

    public void setFlagGender(String flagGender) {
        this.flagGender = flagGender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
    
}
