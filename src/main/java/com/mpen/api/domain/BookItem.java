/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.domain;

import java.io.Serializable;

/**
 * 课本信息.
 *
 * @author kai
 *
 */
public final class BookItem implements Serializable {

    private static final long serialVersionUID = 6015784574967560400L;

    private String id;
    private String code;
    private String name;
    private String version;
    private String bookLink;
    private boolean isRepeat;
    private boolean isLineRead;

    public String getBookLink() {
        return bookLink;
    }

    public void setBookLink(String bookLink) {
        this.bookLink = bookLink;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    public boolean isLineRead() {
        return isLineRead;
    }

    public void setLineRead(boolean isLineRead) {
        this.isLineRead = isLineRead;
    }

}
