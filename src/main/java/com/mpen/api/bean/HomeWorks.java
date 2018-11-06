package com.mpen.api.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mpen.api.common.Constants;

/**
 * app2.0所涉及的作业 bean
 *
 */
public final class HomeWorks implements Serializable {

    private static final long serialVersionUID = -3506352651408896723L;

    private String action;
    private String id;
    // 页数
    private int pageNo = Constants.DEFAULT_PAGENO;
    // 页尺寸
    private int pageSize = Constants.MAX_PAGESIZE;
    // 作业类型
    private Integer type;
    private String fkClassId;
    // 开始时间
    private Date endDate;
    // 结束时间
    private Date startDate;
    // 书别名
    private String bookAlias;
    // 模块
    private String model;
    // 作业资源url
    private List<HomeworkResourceUrl> resourceUrl;

    int pageIndex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFkClassId() {
        return fkClassId;
    }

    public void setFkClassId(String fkClassId) {
        this.fkClassId = fkClassId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getBookAlias() {
        return bookAlias;
    }

    public void setBookAlias(String bookAlias) {
        this.bookAlias = bookAlias;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<HomeworkResourceUrl> getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(List<HomeworkResourceUrl> resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public int getPageIndex() {
        return (this.pageNo - 1) * this.pageSize;
    }

}
