/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mp.shared.common;

import java.io.Serializable;
import java.util.List;

/**
 * TODO 通用分页对象.
 * 
 * @author kai
 *
 */
public class Page<E> implements Serializable, IsValid{
    private static final long serialVersionUID = -6328576888173456966L;

    public static final Integer PAGESIZE = 10;

    private Integer pageSize = PAGESIZE;
    private Integer pageNo = 1;

    private List<E> items;

    private Integer totalCount = 0;

    private Integer totalPage = 0;

    private Integer previousPage = 0;

    private Integer nextPage = 0;

    private ResourceVersion version;

    // 排序方式
    public enum OrderType {
        asc, desc
    }

    private String property;// 查找属性名称
    private String keyword;// 查找关键字
    private String orderBy = "createDate";// 排序字段
    private OrderType orderType = OrderType.desc;// 排序方式

    public Page() {
    }

    public Page(List<E> items, Integer totalCount) {
        setTotalCount(totalCount);
        setItems(items);
    }

    public Page(List<E> items, Integer totalCount, Integer startIndex) {
        setTotalCount(totalCount);
        setItems(items);
    }

    /**
     * 构造一个分页对象.
     *
     * @param items
     *            当前页的记录
     * @param totalCount
     *            总记录数
     * @param pageSize
     *            每页记录书
     * @param startIndex
     *            起始页码
     */
    public Page(List<E> items, Integer totalCount, Integer pageSize, Integer startIndex) {
        setPageSize(pageSize);
        setTotalCount(totalCount);
        setItems(items);
        setPageNo(startIndex / getPageSize() + 1);
    }

    public List<E> getItems() {
        return items;
    }

    public void setItems(List<E> items) {
        this.items = items;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     * 设置总记录数.
     *
     * @param totalCount
     *            总记录数
     */
    public void setTotalCount(Integer totalCount) {
        if (totalCount > 0) {
            this.totalCount = totalCount;
            Integer count = totalCount / getPageSize();
            this.totalPage = count;
        } else {
            this.totalCount = 0;
            this.totalPage = 0;
        }
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    /**
     * 设置页码.
     *
     * @param pageNo
     *            页码
     */
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        this.previousPage = pageNo - 1 <= 0 ? pageNo : pageNo - 1;
        this.nextPage = pageNo + 1 > totalPage ? pageNo : pageNo + 1;
    }

    /**
     * 判断数据是否有效
     * @return false or true
     */
    public boolean isValid() {
        return items != null && items.size() != 0 && this.version != null;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public Integer getPreviousPage() {
        return previousPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public void setPreviousPage(Integer previousPage) {
        this.previousPage = previousPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public ResourceVersion getVersion() {
        return version;
    }

    public void setVersion(ResourceVersion version) {
        this.version = version;
    }

}
