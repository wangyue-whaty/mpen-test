/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.mp.shared.common.FullBookInfo;

/**
 * ORM对象：DdbResourceBook.
 *
 * @author kai
 *
 */
public final class DdbResourceBook implements Serializable {

    private static final long serialVersionUID = 221459500053484175L;

    private String id;
    private String name;
    private String code;
    private String identifiaction;
    private String isbn;
    private BigDecimal price;
    private String pdfLink;
    private String photo;
    private String introduction;
    private String catalog;
    private Date createDatetime;
    private String author;
    private Date lastEditDatetime;
    private long resSize;
    private String bookLink;
    private int bookPageNum;
    private int xaxesCodeNum;
    private int yaxesCodeNum;
    private int width;
    private int height;
    private String poster;
    private String englishCompose;
    // 是否为预下载资源
    private String isPreDownload;
    // 是否支持云点读
    private int isLineRead;
    private String suitImage;
    private int pointSize;
    private String version;
    private int pointOffset;
    private int pointDistance;
    private int pointPedding;
    private int pointTopMargin;
    private int pointLeftMargin;
    private String pointVersion;
    private int pointNum;
    private String sonixFilePath;
    private String vedioFilePath;
    private FullBookInfo.Type type;
    private long updateTime;
    private String grade;
    private int sequence;
    // 图片包地址，书本本身对应的每页的图片
    private String mppLink;
    // 视频包地址
    private String mpvLink;
    // 教学资源包地址，包含老师添加的:word,ppt,图片等.
    private String teachLink;
    // mp文件的md5值
    private String mpLinkMd5;
    // mpp文件的md5值
    private String mppLinkMd5;
    // mpv文件的md5值
    private String mpvLinkMd5;
    // 教学资源文件的md5值
    private String teachLinkMd5;
    
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getMppLink() {
        return mppLink;
    }

    public void setMppLink(String mppLink) {
        this.mppLink = mppLink;
    }

    public String getMpvLink() {
        return mpvLink;
    }

    public void setMpvLink(String mpvLink) {
        this.mpvLink = mpvLink;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public int getXaxesCodeNum() {
        return xaxesCodeNum;
    }

    public int getYaxesCodeNum() {
        return yaxesCodeNum;
    }

    public int getPointLeftMargin() {
        return pointLeftMargin;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public void setXaxesCodeNum(int xaxesCodeNum) {
        this.xaxesCodeNum = xaxesCodeNum;
    }

    public void setYaxesCodeNum(int yaxesCodeNum) {
        this.yaxesCodeNum = yaxesCodeNum;
    }

    public void setPointLeftMargin(int pointLeftMargin) {
        this.pointLeftMargin = pointLeftMargin;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdentifiaction() {
        return identifiaction;
    }

    public void setIdentifiaction(String identifiaction) {
        this.identifiaction = identifiaction;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPdfLonk() {
        return pdfLink;
    }

    public void setPdfLonk(String pdfLonk) {
        this.pdfLink = pdfLonk;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLastEditDatetime() {
        return lastEditDatetime;
    }

    public void setLastEditDatetime(Date lastEditDatetime) {
        this.lastEditDatetime = lastEditDatetime;
    }

    public long getResSize() {
        return resSize;
    }

    public void setResSize(long resSize) {
        this.resSize = resSize;
    }

    public String getBookLink() {
        return bookLink;
    }

    public void setBookLink(String bookLink) {
        this.bookLink = bookLink;
    }

    public int getBookPageNum() {
        return bookPageNum;
    }

    public void setBookPageNum(int bookPageNum) {
        this.bookPageNum = bookPageNum;
    }

    public int getxCodeNum() {
        return xaxesCodeNum;
    }

    public void setxCodeNum(int xaxesCodeNum) {
        this.xaxesCodeNum = xaxesCodeNum;
    }

    public int getyCodeNum() {
        return yaxesCodeNum;
    }

    public void setyCodeNum(int yaxesCodeNum) {
        this.yaxesCodeNum = yaxesCodeNum;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getEnglishCompose() {
        return englishCompose;
    }

    public void setEnglishCompose(String englishCompose) {
        this.englishCompose = englishCompose;
    }

    public String getIsPreDownload() {
        return isPreDownload;
    }

    public void setIsPreDownload(String isPreDownload) {
        this.isPreDownload = isPreDownload;
    }

    public int getIsLineRead() {
        return isLineRead;
    }

    public void setIsLineRead(int isLineRead) {
        this.isLineRead = isLineRead;
    }

    public String getSuitImage() {
        return suitImage;
    }

    public void setSuitImage(String suitImage) {
        this.suitImage = suitImage;
    }

    public int getPointSize() {
        return pointSize;
    }

    public void setPointSize(int pointSize) {
        this.pointSize = pointSize;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getPointOffset() {
        return pointOffset;
    }

    public void setPointOffset(int pointOffset) {
        this.pointOffset = pointOffset;
    }

    public int getPointDistance() {
        return pointDistance;
    }

    public void setPointDistance(int pointDistance) {
        this.pointDistance = pointDistance;
    }

    public int getPointPedding() {
        return pointPedding;
    }

    public void setPointPedding(int pointPedding) {
        this.pointPedding = pointPedding;
    }

    public int getPointTopMargin() {
        return pointTopMargin;
    }

    public void setPointTopMargin(int pointTopMargin) {
        this.pointTopMargin = pointTopMargin;
    }

    public int getPointLeftMagin() {
        return pointLeftMargin;
    }

    public void setPointLeftMagin(int pointLeftMagin) {
        this.pointLeftMargin = pointLeftMagin;
    }

    public String getPointVersion() {
        return pointVersion;
    }

    public void setPointVersion(String pointVersion) {
        this.pointVersion = pointVersion;
    }

    public int getPointNum() {
        return pointNum;
    }

    public void setPointNum(int pointNum) {
        this.pointNum = pointNum;
    }

    public String getSonixFilePath() {
        return sonixFilePath;
    }

    public void setSonixFilePath(String sonixFilePath) {
        this.sonixFilePath = sonixFilePath;
    }

    public String getVedioFilePath() {
        return vedioFilePath;
    }

    public void setVedioFilePath(String vedioFilePath) {
        this.vedioFilePath = vedioFilePath;
    }

    public FullBookInfo.Type getType() {
        return type;
    }

    public void setType(FullBookInfo.Type type) {
        this.type = type;
    }

    public String getTeachLink() {
        return teachLink;
    }

    public void setTeachLink(String teachLink) {
        this.teachLink = teachLink;
    }

    public String getMpLinkMd5() {
        return mpLinkMd5;
    }

    public void setMpLinkMd5(String mpLinkMd5) {
        this.mpLinkMd5 = mpLinkMd5;
    }

    public String getMppLinkMd5() {
        return mppLinkMd5;
    }

    public void setMppLinkMd5(String mppLinkMd5) {
        this.mppLinkMd5 = mppLinkMd5;
    }

    public String getMpvLinkMd5() {
        return mpvLinkMd5;
    }

    public void setMpvLinkMd5(String mpvLinkMd5) {
        this.mpvLinkMd5 = mpvLinkMd5;
    }

    public String getTeachLinkMd5() {
        return teachLinkMd5;
    }

    public void setTeachLinkMd5(String teachLinkMd5) {
        this.teachLinkMd5 = teachLinkMd5;
    }

    
}
