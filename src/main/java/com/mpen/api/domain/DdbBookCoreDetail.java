package com.mpen.api.domain;

import java.io.Serializable;

import com.google.gson.reflect.TypeToken;
import com.mp.shared.common.BookInfo;
import com.mp.shared.common.Page;
import com.mpen.api.common.Constants;

public class DdbBookCoreDetail implements Serializable, CacheInfos<BookInfo> {
    private static final long serialVersionUID = -4298176950959279759L;
    private int id;
    private String bookInfos;
    private String version;
    private Boolean isActive; // 是否为正在使用 false:否.true:是

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookInfos() {
        return bookInfos;
    }

    public void setBookInfos(String bookInfos) {
        this.bookInfos = bookInfos;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public DdbBookCoreDetail() {

    }

    public DdbBookCoreDetail(Page<BookInfo> page) {
        this.bookInfos = Constants.GSON.toJson(page);
        this.version = page.getVersion().toString();
    }

    public Page<BookInfo> formDetail() {
        return Constants.GSON.fromJson(this.bookInfos, new TypeToken<Page<BookInfo>>() {
        }.getType());
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

  
}
