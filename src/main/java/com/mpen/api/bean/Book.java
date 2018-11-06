/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;

import com.mp.shared.common.FullBookInfo;
import com.mpen.api.domain.DdbResourcePageScope;
import com.mpen.api.util.MpCodeBuilder.FileType;

/**
 * TODO 书籍资源参数bean.
 * 
 * @author zyt
 *
 */
public final class Book implements Serializable {
    private static final long serialVersionUID = 17982076915329630L;
    private String action;
    private String bookId;
    private String date;
    private String name;
    private String isbn;
    private FullBookInfo.Type type;
    private FileType fileType;

    private int pageNum;
    private String version;
    private String baseVersion;
    private DotParam defaultDotParam;
    private PageParam[] pageParam;
    private String id;
    // 0 云知声评测信息  1 驰声评测信息
    private int assessmentType;
    // 是否支持云点读(默支持云点读)
    private int isLineRead = 1;
    
    public final DotParam getDefaultDotParam() {
        return defaultDotParam;
    }

    public final void setDefaultDotParam(DotParam defaultDotParam) {
        this.defaultDotParam = defaultDotParam;
    }

    public final PageParam[] getPageParam() {
        return pageParam;
    }

    public final void setPageParam(PageParam[] pageParam) {
        this.pageParam = pageParam;
    }

    public final int getPageNum() {
        return pageNum;
    }

    public final void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getBaseVersion() {
        return baseVersion;
    }

    public void setBaseVersion(String baseVersion) {
        this.baseVersion = baseVersion;
    }

    public FullBookInfo.Type getType() {
        return type;
    }

    public void setType(FullBookInfo.Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAction() {
        return action;
    }

    public String getBookId() {
        return bookId;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public int getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(int assessmentType) {
        this.assessmentType = assessmentType;
    }

    public int getIsLineRead() {
        return isLineRead;
    }

    public void setIsLineRead(int isLineRead) {
        this.isLineRead = isLineRead;
    }



    public static final class DotParam implements Serializable {
        private static final long serialVersionUID = 6292336250275597932L;
        private int matrixGap = 0;
        private int[] margin = { 0, 0, 0, 0 };
        private int matrixSize = 5;
        private int dotDistanceInpixels = 16;
        private int dotSize = 3;
        private int dotShift = 2;

        public final int getMatrixGap() {
            return matrixGap;
        }

        public final void setMatrixGap(int matrixGap) {
            this.matrixGap = matrixGap;
        }

        public final int[] getMargin() {
            return margin;
        }

        public final void setMargin(int[] margin) {
            this.margin = margin;
        }

        public final int getMatrixSize() {
            return matrixSize;
        }

        public final void setMatrixSize(int matrixSize) {
            this.matrixSize = matrixSize;
        }

        public final int getDotDistanceInpixels() {
            return dotDistanceInpixels;
        }

        public final void setDotDistanceInpixels(int dotDistanceInpixels) {
            this.dotDistanceInpixels = dotDistanceInpixels;
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
    }

    public static final class PageParam implements Serializable {
        private static final long serialVersionUID = -4515500663150504647L;
        private int num;
        private float widthMm;
        private float heightMm;
        private SubPageParam[] subPages;
        private DotParam dotParam;
        // 码值是否变化（0:变化；1：不变）
        private int isNewCode;

        public PageParam() {

        }

        public PageParam(DdbResourcePageScope scope) {
            this.dotParam = new DotParam();
            this.dotParam.matrixGap = scope.getMatrixGap();
            this.dotParam.matrixSize = scope.getMatrixSize();
            this.dotParam.dotDistanceInpixels = scope.getDotDistanceInPixels();
            this.dotParam.dotSize = scope.getDotSize();
            this.dotParam.dotShift = scope.getDotShift();
            this.dotParam.margin = new int[] { scope.getLeftMargin(), scope.getTopMargin(), scope.getRightMargin(),
                scope.getBottomMargin() };
        }

        public final int getIsNewCode() {
            return isNewCode;
        }

        public final void setIsNewCode(int isNewCode) {
            this.isNewCode = isNewCode;
        }

        public final int getNum() {
            return num;
        }

        public final void setNum(int num) {
            this.num = num;
        }

        public final float getWidthMm() {
            return widthMm;
        }

        public final void setWidthMm(float widthMm) {
            this.widthMm = widthMm;
        }

        public final float getHeightMm() {
            return heightMm;
        }

        public final void setHeightMm(float heightMm) {
            this.heightMm = heightMm;
        }

        public final SubPageParam[] getSubPages() {
            return subPages;
        }

        public final void setSubPages(SubPageParam[] subPages) {
            this.subPages = subPages;
        }

        public final DotParam getDotParam() {
            return dotParam;
        }

        public final void setDotParam(DotParam dotParam) {
            this.dotParam = dotParam;
        }

    }

    public static final class SubPageParam implements Serializable {
        private static final long serialVersionUID = -5935933539207110157L;
        private float top;
        private float left;
        private float bottom;
        private float right;
        private int num;

        public SubPageParam() {
            super();
        }

        public SubPageParam(float top, float left, float bottom, float right, int num) {
            super();
            this.top = top;
            this.left = left;
            this.bottom = bottom;
            this.right = right;
            this.num = num;
        }

        public final float getTop() {
            return top;
        }

        public final void setTop(float top) {
            this.top = top;
        }

        public final float getLeft() {
            return left;
        }

        public final void setLeft(float left) {
            this.left = left;
        }

        public final float getBottom() {
            return bottom;
        }

        public final void setBottom(float bottom) {
            this.bottom = bottom;
        }

        public final float getRight() {
            return right;
        }

        public final void setRight(float right) {
            this.right = right;
        }

        public final int getNum() {
            return num;
        }

        public final void setNum(int num) {
            this.num = num;
        }
    }
}
