package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

public class DdbPenCmd implements Serializable {
    private static final long serialVersionUID = 7794736059942648050L;
    private String id;
    private String fkPenId;
    private String cmd;
    // 命令状态0：未执行；1：已执行
    private int status;
    private String result;
    private String url;
    private String description;
    private Date createDate;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFkPenId() {
        return fkPenId;
    }

    public void setFkPenId(String fkPenId) {
        this.fkPenId = fkPenId;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
