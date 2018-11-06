package com.mpen.api.bean;

import org.springframework.web.multipart.MultipartFile;

import com.mpen.api.common.Constants;

/**
 * 动态 涉及：app2.0动态
 *
 */
public class Dynamic {
    private String action;
    private String id;
    private String loginId;
    // 封面
    private String cover;
    private MultipartFile file;
    private int pageNo = Constants.DEFAULT_PAGENO;
    private int pageSize = Constants.MAX_PAGESIZE;
    private int pageIndex;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public int getPageIndex() {
        return (this.pageNo - 1) * this.pageSize;
    }

}
