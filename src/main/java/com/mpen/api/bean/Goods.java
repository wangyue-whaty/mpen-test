/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;

import com.mp.shared.common.Publisher;
import com.mpen.api.domain.MobileApp;

public final class Goods implements Serializable {
    private static final long serialVersionUID = -5748464374399752657L;
    private String action;
    private String id;
    private Integer pageNo;
    private Integer pageSize;
    private String bookType;
    private String suitGrade;
    private String bookName;
    private String bookId;
    private Publisher.PublisherID publisherID;
    private MobileApp.Type systemType;
    // 是否为教学资源连接下载 
    private boolean needTeachLink;

    public MobileApp.Type getSystemType() {
        return systemType;
    }

    public void setSystemType(MobileApp.Type systemType) {
        this.systemType = systemType;
    }

    public Publisher.PublisherID getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(Publisher.PublisherID publisherID) {
        this.publisherID = publisherID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public String getSuitGrade() {
        return suitGrade;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setSuitGrade(String suitGrade) {
        this.suitGrade = suitGrade;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public boolean isNeedTeachLink() {
        return needTeachLink;
    }

    public void setNeedTeachLink(boolean needTeachLink) {
        this.needTeachLink = needTeachLink;
    }
}
