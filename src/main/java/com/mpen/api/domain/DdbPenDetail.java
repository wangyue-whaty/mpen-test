package com.mpen.api.domain;

import java.io.Serializable;

import com.google.gson.reflect.TypeToken;
import com.mp.shared.common.Page;
import com.mpen.api.common.Constants;

/**
 * 教师笔版本信息实体类
 * @author wangyue
 *
 */
public class DdbPenDetail implements Serializable, CacheInfos<DdbPePen>{

    private static final long serialVersionUID = 3625399608972814981L;
    private int id;
    private String penInfos;
    private String version;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public String getPenInfos() {
        return penInfos;
    }

    public void setPenInfos(String penInfos) {
        this.penInfos = penInfos;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }

    public DdbPenDetail() {

    }

    public DdbPenDetail(Page<DdbPePen> page) {
        this.penInfos = Constants.GSON.toJson(page);
        this.version = page.getVersion().toString();
    }

    /**
     * penInfos信息转换为json格式
     */
    public Page<DdbPePen> formDetail() {
        return Constants.GSON.fromJson(this.penInfos, new TypeToken<Page<DdbPePen>>() {
        }.getType());
    }

}
