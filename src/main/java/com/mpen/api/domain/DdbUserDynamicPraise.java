package com.mpen.api.domain;

import java.util.Date;
import java.io.Serializable;

/**
 * 动态点赞表
 * 涉及：App2.0动态相关
 * @author hzy
 * @since 2018-08-09
 */
public class DdbUserDynamicPraise implements Serializable {
    private static final long serialVersionUID = 3450328765744770268L;

    private String id;
    private String fkDynamicId;
    private String fkLoginId;
    private long createTimeInMs;
    private long updateTimeInMs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFkDynamicId() {
        return fkDynamicId;
    }

    public void setFkDynamicId(String fkDynamicId) {
        this.fkDynamicId = fkDynamicId;
    }

    public String getFkLoginId() {
        return fkLoginId;
    }

    public void setFkLoginId(String fkLoginId) {
        this.fkLoginId = fkLoginId;
    }

    public long getCreateTimeInMs() {
        return createTimeInMs;
    }

    public void setCreateTimeInMs(long createTimeInMs) {
        this.createTimeInMs = createTimeInMs;
    }

    public long getUpdateTimeInMs() {
        return updateTimeInMs;
    }

    public void setUpdateTimeInMs(long updateTimeInMs) {
        this.updateTimeInMs = updateTimeInMs;
    }
}