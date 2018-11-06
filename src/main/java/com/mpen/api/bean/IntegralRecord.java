package com.mpen.api.bean;

import com.mpen.api.common.Constants;

/**
 * 用户积分记录 涉及：App2.0积分相关
 *
 */
public class IntegralRecord {
    // 积分类型
    private String integralType;
    // TODO 记录时间 精确到天 (以后用 dateInMs 或者 dateInS)
    private String date;
    // 积分
    private int integral;
    // 页数
    private int pageNo = Constants.DEFAULT_PAGENO;
    // 尺寸
    private int pageSize = Constants.MAX_PAGESIZE;
    // 好友 0 全部 1
    private String type;

    private String action;
    // TODO 记录时间 精确到月 (以后用 dateInMs 或者 dateInS)
    private String monthDate;
    private int pageIndex;
    
    public String getIntegralType() {
        return integralType;
    }

    public void setIntegralType(String integralType) {
        this.integralType = integralType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(String monthDate) {
        this.monthDate = monthDate;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return (this.pageNo - 1) * this.pageSize;
    }

    public String getType() {
        return type;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setType(String type) {
        this.type = type;
    }

}
