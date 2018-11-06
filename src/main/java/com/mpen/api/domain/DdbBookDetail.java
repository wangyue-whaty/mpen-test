package com.mpen.api.domain;

import java.io.Serializable;

import com.google.gson.reflect.TypeToken;
import com.mp.shared.common.FullBookInfo;
import com.mp.shared.common.Page;
import com.mpen.api.common.Constants;

public class DdbBookDetail implements Serializable, CacheInfos<FullBookInfo> {
    private static final long serialVersionUID = -6256558683925540523L;
    private int id;
    private String bookInfos;
    private String version;

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

    public DdbBookDetail() {

    }

    public DdbBookDetail(Page<FullBookInfo> page) {
        this.bookInfos = Constants.GSON.toJson(page);
        this.version = page.getVersion().toString();
    }

    public Page<FullBookInfo> formDetail() {
        return Constants.GSON.fromJson(this.bookInfos, new TypeToken<Page<FullBookInfo>>() {
        }.getType());
    }
}
