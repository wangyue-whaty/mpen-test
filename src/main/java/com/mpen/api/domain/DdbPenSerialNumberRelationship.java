package com.mpen.api.domain;

import java.io.Serializable;
import java.time.Instant;

public final class DdbPenSerialNumberRelationship implements Serializable {

    private static final long serialVersionUID = 1495939605736022800L;
    private String id;
    private String fkPenId;
    private Integer isValid;
    private Instant createDate;
    private String serialNumber;

    public String getFkPenId() {
        return fkPenId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setFkPenId(String fkPenId) {
        this.fkPenId = fkPenId;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getId() {
        return id;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

}
