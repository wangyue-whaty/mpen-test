package com.mpen.api.domain;

import java.io.Serializable;

public class DdbResourceBookPrint implements Serializable {
    private static final long serialVersionUID = 4912807256629624084L;

    private String id;
    private String fkBookId;
    private String name;
    private String isDefault;
    private String note;
    private String zipLink;

    public DdbResourceBookPrint() {

    }

    public DdbResourceBookPrint(String id, String fkBookId, String name) {
        super();
        this.id = id;
        this.fkBookId = fkBookId;
        this.name = name;
    }

    public final String getZipLink() {
        return zipLink;
    }

    public final void setZipLink(String zipLink) {
        this.zipLink = zipLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFkBookId() {
        return fkBookId;
    }

    public void setFkBookId(String fkBookId) {
        this.fkBookId = fkBookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
