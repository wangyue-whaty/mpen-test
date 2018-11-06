package com.mpen.api.domain;

import java.util.Date;

import com.mp.shared.record.ActionRecord;

public final class DdbActionRecord {
    private Integer id;
    private String uploadUuid;
    private Integer sequceNumInBatch;
    private String fkPenId;
    private Date uploadTime;
    private ActionRecord.Type type;
    private ActionRecord.Subtype subType;
    private String data;
    private int version;

    public Integer getId() {
        return id;
    }

    public String getUploadUuid() {
        return uploadUuid;
    }

    public Integer getSequceNumInBatch() {
        return sequceNumInBatch;
    }

    public String getFkPenId() {
        return fkPenId;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public ActionRecord.Type getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public int getVersion() {
        return version;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUploadUuid(String uploadUuid) {
        this.uploadUuid = uploadUuid;
    }

    public void setSequceNumInBatch(Integer sequceNumInBatch) {
        this.sequceNumInBatch = sequceNumInBatch;
    }

    public void setFkPenId(String fkPenId) {
        this.fkPenId = fkPenId;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setType(ActionRecord.Type type) {
        this.type = type;
    }

    public ActionRecord.Subtype getSubType() {
        return subType;
    }

    public void setSubType(ActionRecord.Subtype subType) {
        this.subType = subType;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}
