/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

public final class SsoUser implements Serializable {

    private static final long serialVersionUID = 4400049200793253822L;

    private String id;

    private String loginId;

    private String password;

    /**
     * 户用姓名.
     */
    private String trueName;

    private String fkRoleId;

    /**
     * ENUMCONST是否有效.
     */
    private String flagIsvalid;

    private String nickName;

    private String onlineTime;

    private String photo;

    private String mobile;

    private String qqidentifier;

    private String weixinidentifier;

    private Date updateDate;

    private Date createDate;

    private String email;

    private String bindmobile;

    private String bindemail;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getFkRoleId() {
        return fkRoleId;
    }

    public void setFkRoleId(String fkRoleId) {
        this.fkRoleId = fkRoleId;
    }

    public String getFlagIsvalid() {
        return flagIsvalid;
    }

    public void setFlagIsvalid(String flagIsvalid) {
        this.flagIsvalid = flagIsvalid;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getQqidentifier() {
        return qqidentifier;
    }

    public void setQqidentifier(String qqidentifier) {
        this.qqidentifier = qqidentifier;
    }

    public String getWeixinidentifier() {
        return weixinidentifier;
    }

    public void setWeixinidentifier(String weixinidentifier) {
        this.weixinidentifier = weixinidentifier;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBindmobile() {
        return bindmobile;
    }

    public void setBindmobile(String bindmobile) {
        this.bindmobile = bindmobile;
    }

    public String getBindemail() {
        return bindemail;
    }

    public void setBindemail(String bindemail) {
        this.bindemail = bindemail;
    }

}