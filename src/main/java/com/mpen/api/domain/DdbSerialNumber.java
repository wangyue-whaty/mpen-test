package com.mpen.api.domain;

import java.io.Serializable;
import java.time.Instant;

public final class DdbSerialNumber implements Serializable {

    private static final long serialVersionUID = -9086090143464879691L;

    private String id;
    private String prefix;
    private Long suffixStart;
    private Long suffixEnd;
    private Integer isValid;
    private Instant createDate;
    private String item;

    public String getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public Long getSuffixStart() {
        return suffixStart;
    }

    public Long getSuffixEnd() {
        return suffixEnd;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public String getItem() {
        return item;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffixStart(Long suffixStart) {
        this.suffixStart = suffixStart;
    }

    public void setSuffixEnd(Long suffixEnd) {
        this.suffixEnd = suffixEnd;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
