package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

public class DdbQuestionItem implements Serializable {
    private static final long serialVersionUID = -4249883442909677858L;
    private String id;
    private String name;
    private String item;
    private int sequence;
    private Date createDate;

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
