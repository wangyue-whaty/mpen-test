/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import com.mpen.api.common.Constants;
import com.mpen.api.util.CommUtil;

public final class DdbResourcePageScope implements Serializable {
    private static final long serialVersionUID = -6129236292477649592L;
    private String id;
    private long codeStart;
    private long codeEnd;
    private Date createTime;
    private String tifLink;
    private Integer xCodeNum;
    private Integer yCodeNum;
    private Integer sign;
    private String fkPageId;
    private int matrixGap;
    private int matrixSize;
    private int dotDistanceInPixels;
    private int dotSize;
    private int dotShift;
    private int topMargin;
    private int bottomMargin;
    private int leftMargin;
    private int rightMargin;
    private String fkTypeId;
    private String rebuild;

    private String subPages;

    public DdbResourcePageScope() {
    }

    public DdbResourcePageScope(long codeStart, long codeEnd, String tifLink, Integer sign, String fkPageId,
        int matrixGap, int matrixSize, int dotDistanceInPixels, int dotSize, int dotShift, String rebuild,
        String subPages) {
        this.id = CommUtil.genRecordKey();
        this.createTime = Date.from(Instant.now());
        this.codeStart = codeStart;
        this.codeEnd = codeEnd;
        this.tifLink = tifLink;
        this.sign = sign;
        this.fkPageId = fkPageId;
        this.matrixGap = matrixGap;
        this.matrixSize = matrixSize;
        this.dotDistanceInPixels = dotDistanceInPixels;
        this.dotSize = dotSize;
        this.dotShift = dotShift;
        this.rebuild = rebuild;
        this.subPages = subPages;
    }

    public final String getSubPages() {
        return subPages;
    }

    public final void setSubPages(String subPages) {
        this.subPages = subPages;
    }

    public final String getRebuild() {
        return rebuild;
    }

    public final void setRebuild(String rebuild) {
        this.rebuild = rebuild;
    }

    public String getFkTypeId() {
        return fkTypeId;
    }

    public void setFkTypeId(String fkTypeId) {
        this.fkTypeId = fkTypeId;
    }

    public final int getMatrixGap() {
        return matrixGap;
    }

    public final void setMatrixGap(int matrixGap) {
        this.matrixGap = matrixGap;
    }

    public final int getMatrixSize() {
        return matrixSize;
    }

    public final void setMatrixSize(int matrixSize) {
        this.matrixSize = matrixSize;
    }

    public final int getDotDistanceInPixels() {
        return dotDistanceInPixels;
    }

    public final void setDotDistanceInPixels(int dotDistanceInPixels) {
        this.dotDistanceInPixels = dotDistanceInPixels;
    }

    public final int getDotSize() {
        return dotSize;
    }

    public final void setDotSize(int dotSize) {
        this.dotSize = dotSize;
    }

    public final int getDotShift() {
        return dotShift;
    }

    public final void setDotShift(int dotShift) {
        this.dotShift = dotShift;
    }

    public final int getTopMargin() {
        return topMargin;
    }

    public final void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
    }

    public final int getBottomMargin() {
        return bottomMargin;
    }

    public final void setBottomMargin(int bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    public final int getLeftMargin() {
        return leftMargin;
    }

    public final void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }

    public final int getRightMargin() {
        return rightMargin;
    }

    public final void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
    }

    public Integer getSign() {
        return sign;
    }

    public void setSign(Integer sign) {
        this.sign = sign;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCodeStart() {
        return codeStart;
    }

    public void setCodeStart(long codeStart) {
        this.codeStart = codeStart;
    }

    public long getCodeEnd() {
        return codeEnd;
    }

    public void setCodeEnd(long codeEnd) {
        this.codeEnd = codeEnd;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTifLink() {
        return tifLink;
    }

    public void setTifLink(String tifLink) {
        this.tifLink = tifLink;
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

    public String getFkPageId() {
        return fkPageId;
    }

    public void setFkPageId(String fkPageId) {
        this.fkPageId = fkPageId;
    }

}
