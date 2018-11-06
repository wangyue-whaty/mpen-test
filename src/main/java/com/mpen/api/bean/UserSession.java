/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.SsoUser;

import java.io.Serializable;
import java.sql.Timestamp;

public final class UserSession implements Serializable {

    private static final long serialVersionUID = 4538822591930338135L;

    public UserSession() {

    }

    private DdbPeCustom peCustom;

    private SsoUser ssoUser;

    private String id;

    private String loginId;

    private String userName;

    private String userLoginType;

    private String roleId;

    private Timestamp lastLoginDate;

    public SsoUser getSsoUser() {
        return ssoUser;
    }

    public void setSsoUser(SsoUser ssoUser) {
        this.ssoUser = ssoUser;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLoginType() {
        return userLoginType;
    }

    public void setUserLoginType(String userLoginType) {
        this.userLoginType = userLoginType;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Timestamp getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Timestamp lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public DdbPeCustom getPeCustom() {
        return peCustom;
    }

    public void setPeCustom(DdbPeCustom peCustom) {
        this.peCustom = peCustom;
    }

}
