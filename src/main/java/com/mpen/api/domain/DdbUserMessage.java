package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户消息表 涉及：App2.0消息相关
 * 
 * @author hzy
 * @since 2018-08-21
 */
public class DdbUserMessage implements Serializable {

    private static final long serialVersionUID = 107725962973089940L;
    private String id;
    /**
     * 用户登录id
     */
    private String loginId;
    /**
     * 消息类型
     */
    private String type;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 是否已读(0:未读,1:已读)
     */
    private int isRead;
    /**
     * 是否删除(0:否1:是)
     */
    private int isDel;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    // 给APP提供消息具体类型 进行页面跳转 目前：积分榜 积分总榜 点赞作业 跳转
    private String typeDetail;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
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

    public String getTypeDetail() {
        return typeDetail;
    }

    public void setTypeDetail(String typeDetail) {
        this.typeDetail = typeDetail;
    }

}