package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户动态记录表 
 * 涉及：教师端批阅作业 以及app2.0动态模块相关接口
 * 
 * @author hzy
 * @since 2018-08-09
 */
public class DdbUserDynamicRecord implements Serializable {
    private static final long serialVersionUID = 196951089807017876L;

    private String id;
    // loginId
    private String fkLoginId;
    // 创建时间
    private Date createTime;
    // 更新时间
    private Date updateTime;
    // 动态内容
    private String dynamicContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFkLoginId() {
        return fkLoginId;
    }

    public void setFkLoginId(String fkLoginId) {
        this.fkLoginId = fkLoginId;
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

    public String getDynamicContent() {
        return dynamicContent;
    }

    public void setDynamicContent(String dynamicContent) {
        this.dynamicContent = dynamicContent;
    }

}