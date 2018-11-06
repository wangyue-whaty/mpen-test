package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户好友domain
 * 涉及：App2.0好友相关
 */
public class DdbUserRelationship implements Serializable{
    private static final long serialVersionUID = 4213263704497155568L;
    private String id;
    // 用户LoginId
    private String userLoginId;
    // 好友loginId
    private String friendLoginId;
    // 是否好友 1是 0不是
    private String relStatus;
    // 好友备注（用户对好友的备注）
    private String aliasUser;
    // 好友备注(好友对用户的备注)
    private String aliasFriend;
    // 验证信息
    private String reqMsg;
    private Date createTime;
    private Date updateTime;
    // 新的朋友是否删除 0 未删除 1 删除
    private String isDel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getFriendLoginId() {
        return friendLoginId;
    }

    public void setFriendLoginId(String friendLoginId) {
        this.friendLoginId = friendLoginId;
    }

    public String getRelStatus() {
        return relStatus;
    }

    public void setRelStatus(String relStatus) {
        this.relStatus = relStatus;
    }

    public String getAliasUser() {
        return aliasUser;
    }

    public void setAliasUser(String aliasUser) {
        this.aliasUser = aliasUser;
    }

    public String getAliasFriend() {
        return aliasFriend;
    }

    public void setAliasFriend(String aliasFriend) {
        this.aliasFriend = aliasFriend;
    }

    public String getReqMsg() {
        return reqMsg;
    }

    public void setReqMsg(String reqMsg) {
        this.reqMsg = reqMsg;
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

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }
    

}
