/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.domain;

import java.io.Serializable;

public final class DdbResourceBookCatalog implements Serializable {
    private static final long serialVersionUID = 7648274816264154060L;
    private String id;
    private String name;
    private Integer number;
    private String item;
    private String fkBookId;
    private String fkCatalogId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getFkBookId() {
        return fkBookId;
    }

    public String getFkCatalogId() {
        return fkCatalogId;
    }

    public void setFkBookId(String fkBookId) {
        this.fkBookId = fkBookId;
    }

    public void setFkCatalogId(String fkCatalogId) {
        this.fkCatalogId = fkCatalogId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
