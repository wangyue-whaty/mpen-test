package com.mpen.api.domain;

import java.util.Date;

/**
 * 固定字典评论 domain 涉及：教师端评论学生信息
 */
public class DdbUserComment {
    public String id;
    // 评语
    public String comment;
    // 创建时间
    public String createdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
