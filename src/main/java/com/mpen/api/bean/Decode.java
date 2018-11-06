/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;

import com.mp.shared.common.Code;

public final class Decode implements Serializable {
    private static final long serialVersionUID = -3524409720696367170L;
    private String bookId;
    private String code;
    private int audioType;
    private Code.Type codeType;
    private Integer x;
    private Integer y;
    private Integer page;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getAudioType() {
        return audioType;
    }

    public void setAudioType(int audioType) {
        this.audioType = audioType;
    }

    public Code.Type getCodeType() {
        return codeType;
    }

    public void setCodeType(Code.Type codeType) {
        this.codeType = codeType;
    }

}
