package com.mpen.api.domain;

import java.util.Date;

/**
 * 用户积分记录 domain
 * 涉及：教师端以及app2.0涉及积分相关接口
 *
 */
public class DdbUserIntegralRecord {

    private String id;
    // 积分
    private int integral;
    // 创建时间
    private Date createTime;
    private String fkLoginId;
    // 积分类型
    private String integralType;
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFkLoginId() {
        return fkLoginId;
    }

    public void setFkLoginId(String fkLoginId) {
        this.fkLoginId = fkLoginId;
    }

    public String getIntegralType() {
        return integralType;
    }

    public void setIntegralType(String integralType) {
        this.integralType = integralType;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
