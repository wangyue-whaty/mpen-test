package com.mpen.api.domain;

import java.io.Serializable;

import com.google.gson.reflect.TypeToken;
import com.mp.shared.common.LearnWordStructureInfo;
import com.mp.shared.common.Page;
import com.mpen.api.common.Constants;

public class DdbLearnWordStructureDetail implements Serializable, CacheInfos<LearnWordStructureInfo> {
    private static final long serialVersionUID = -6256558683925540523L;
    private int id;
    private String learnWordStructureInfos;
    private String version;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLearnWordStructureInfos() {
        return learnWordStructureInfos;
    }

    public void setLearnWordStructureInfos(String learnWordStructureInfos) {
        this.learnWordStructureInfos = learnWordStructureInfos;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public DdbLearnWordStructureDetail() {

    }

    public DdbLearnWordStructureDetail(Page<LearnWordStructureInfo> page) {
        this.learnWordStructureInfos = Constants.GSON.toJson(page);
        this.version = page.getVersion().toString();
    }

    public Page<LearnWordStructureInfo> formDetail() {
        return Constants.GSON.fromJson(this.learnWordStructureInfos, new TypeToken<Page<LearnWordStructureInfo>>() {
        }.getType());
    }
}
