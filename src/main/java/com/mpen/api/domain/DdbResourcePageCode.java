package com.mpen.api.domain;

import java.io.Serializable;
import java.time.Instant;

import com.mpen.api.util.CommUtil;

public class DdbResourcePageCode implements Serializable {
    private static final long serialVersionUID = -6977263217776902685L;
    private String id;
    private String name;
    private Instant createDatetime;
    private int pageNum;
    private String fkBookId;
    private float width;
    private float height;

    public DdbResourcePageCode() {

    }

    public DdbResourcePageCode(String name, int pageNum, String fkBookId, float width, float height) {
        this.id = CommUtil.genRecordKey();
        this.name = name;
        this.createDatetime = Instant.now();
        this.pageNum = pageNum;
        this.fkBookId = fkBookId;
        this.width = width;
        this.height = height;
    }

    public final float getWidth() {
        return width;
    }

    public final void setWidth(float width) {
        this.width = width;
    }

    public final float getHeight() {
        return height;
    }

    public final void setHeight(float height) {
        this.height = height;
    }

    public final String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final Instant getCreateDatetime() {
        return createDatetime;
    }

    public final void setCreateDatetime(Instant createDatetime) {
        this.createDatetime = createDatetime;
    }

    public final int getPageNum() {
        return pageNum;
    }

    public final void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public final String getFkBookId() {
        return fkBookId;
    }

    public final void setFkBookId(String fkBookId) {
        this.fkBookId = fkBookId;
    }

}
