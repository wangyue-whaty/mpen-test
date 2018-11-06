package com.mpen.api.domain;

import java.util.Date;

/**
 * 点赞与被点赞关系
 * 涉及：教师端以及app2.0点赞相关
 *
 */
public class DdbUserPraiseRelationship {
    private String id;
    // 点赞者
    private String praiseLoginId;
    // 被点赞者
    private String byPraiseLoginId;
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPraiseLoginId() {
        return praiseLoginId;
    }

    public void setPraiseLoginId(String praiseLoginId) {
        this.praiseLoginId = praiseLoginId;
    }

    public String getByPraiseLoginId() {
        return byPraiseLoginId;
    }

    public void setByPraiseLoginId(String byPraiseLoginId) {
        this.byPraiseLoginId = byPraiseLoginId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
