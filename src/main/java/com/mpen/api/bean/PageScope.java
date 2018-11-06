/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import com.mp.shared.common.ResourceVersion;
import java.io.Serializable;

/**
 * TODO 页信息bean.
 * 
 * @author zyt
 */
public final class PageScope implements Serializable {
    private static final long serialVersionUID = -4197456182856454954L;
    private String id;
    private String bookId;
    private int pageNum;
    private long codeStart;
    private long codeEnd;
    private Integer xCodeNum;
    private Integer yCodeNum;
    private Integer matrixGap; // 两个点阵键的填充黑点个数，一般0，或者1
    private Integer topMargin; // left, top, right, bottom 页边界margin
    private Integer bottomMargin;
    private Integer leftMargin;
    private Integer rightMargin;
    private Integer matrixSize; // 5, 6, 7, 8

    private Integer dotDistanceInPixels = 16; // 黑点间距：缺省 16 像素点
    // 以下的在笔里关系不太大，加上保持全面
    private Integer dotSize; // 黑点大小，一般是 2:2X2 或者 3:3X3
    private Integer dotShift; // 数据黑点偏移的pixel数量，一般是2

    private ResourceVersion version;
    
    private String subPages;

    public final String getSubPages() {
        return subPages;
    }

    public final void setSubPages(String subPages) {
        this.subPages = subPages;
    }

    public Integer getTopMargin() {
        return topMargin;
    }

    public Integer getBottomMargin() {
        return bottomMargin;
    }

    public Integer getLeftMargin() {
        return leftMargin;
    }

    public Integer getRightMargin() {
        return rightMargin;
    }

    public void setTopMargin(Integer topMargin) {
        this.topMargin = topMargin;
    }

    public void setBottomMargin(Integer bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    public void setLeftMargin(Integer leftMargin) {
        this.leftMargin = leftMargin;
    }

    public void setRightMargin(Integer rightMargin) {
        this.rightMargin = rightMargin;
    }

    public String getId() {
        return id;
    }

    public String getBookId() {
        return bookId;
    }

    public long getCodeEnd() {
        return codeEnd;
    }

    public Integer getMatrixGap() {
        return matrixGap;
    }

    public Integer getMatrixSize() {
        return matrixSize;
    }

    public Integer getDotDistanceInPixels() {
        return dotDistanceInPixels;
    }

    public Integer getDotSize() {
        return dotSize;
    }

    public Integer getDotShift() {
        return dotShift;
    }

    public ResourceVersion getVersion() {
        return version;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setCodeEnd(long codeEnd) {
        this.codeEnd = codeEnd;
    }

    public void setMatrixGap(Integer matrixGap) {
        this.matrixGap = matrixGap;
    }

    public void setMatrixSize(Integer matrixSize) {
        this.matrixSize = matrixSize;
    }

    public void setDotDistanceInPixels(Integer dotDistanceInPixels) {
        this.dotDistanceInPixels = dotDistanceInPixels;
    }

    public void setDotSize(Integer dotSize) {
        this.dotSize = dotSize;
    }

    public void setDotShift(Integer dotShift) {
        this.dotShift = dotShift;
    }

    public void setVersion(ResourceVersion version) {
        this.version = version;
    }

    public long getCodeStart() {
        return codeStart;
    }

    public void setCodeStart(long codeStart) {
        this.codeStart = codeStart;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getxCodeNum() {
        return xCodeNum;
    }

    public void setxCodeNum(Integer xCodeNum) {
        this.xCodeNum = xCodeNum;
    }

    public Integer getyCodeNum() {
        return yCodeNum;
    }

    public void setyCodeNum(Integer yCodeNum) {
        this.yCodeNum = yCodeNum;
    }

}
