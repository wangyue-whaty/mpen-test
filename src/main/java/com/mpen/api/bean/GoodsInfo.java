/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;

import com.mp.shared.common.FullBookInfo;

public final class GoodsInfo implements Serializable {
    private static final long serialVersionUID = 4561063013181911962L;
    private String id;
    private String name;
    private String author;
    private String isbn;
    private String introduction;
    private String photo;
    private long resSize;
    private Integer readnum;
    // TODO 为了兼容老版本，暂时保留该字段
    private String bookId;
    private String link;
    private String poster;
    // 是否支持口语评测 true 支持 false 不支持
    private boolean hasSpeakingEvaluation;

    public GoodsInfo() {

    }

    public GoodsInfo(FullBookInfo fullBookInfo) {
        this.id = fullBookInfo.bookInfo.id;
        this.name = fullBookInfo.bookInfo.fullName;
        this.author = fullBookInfo.author;
        this.isbn = fullBookInfo.isbn;
        this.introduction = fullBookInfo.introduction;
        this.photo = fullBookInfo.photo;
        this.resSize = fullBookInfo.bookInfo.downloadSize;
        this.bookId = fullBookInfo.bookInfo.id;
        this.hasSpeakingEvaluation = fullBookInfo.bookInfo.hasSpeakingEvaluation;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getPhoto() {
        return photo;
    }

    public long getResSize() {
        return resSize;
    }

    public Integer getReadnum() {
        return readnum;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setResSize(long resSize) {
        this.resSize = resSize;
    }

    public void setReadnum(Integer readnum) {
        this.readnum = readnum;
    }

    public boolean isHasSpeakingEvaluation() {
        return hasSpeakingEvaluation;
    }

    public void setHasSpeakingEvaluation(boolean hasSpeakingEvaluation) {
        this.hasSpeakingEvaluation = hasSpeakingEvaluation;
    }

}
