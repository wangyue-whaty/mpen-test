package com.mpen.api.domain;
/**
 * 如何获取积分说明
 * 涉及：App2.0 积分相关
 */

import java.io.Serializable;
import java.sql.Date;

public class DdbObtainIntegralInstruction implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    // 分类
    private String name;
    // 一级描述
    private String fistDesc;
    // 二级描述
    private String subDesc;
    // 父类id
    private String parentId;
    // 积分数
    private int score;
    // 创建日期
    private Date createTime;
    // 层级
    private String typeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFistDesc() {
        return fistDesc;
    }

    public void setFistDesc(String fistDesc) {
        this.fistDesc = fistDesc;
    }

    public String getSubDesc() {
        return subDesc;
    }

    public void setSubDesc(String subDesc) {
        this.subDesc = subDesc;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

}
