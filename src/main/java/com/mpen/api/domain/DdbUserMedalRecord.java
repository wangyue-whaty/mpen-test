package com.mpen.api.domain;

/**
 * 用户勋章记录domain
 * 涉及：教师端以及app2.0有关勋章接口
 *
 */
public class DdbUserMedalRecord {

    private String id;
    private String createTime;
    private String fkLoginId;
    // 勋章状态 1已获得 2佩戴中
    private int medalState;
    // 佩戴状态 0 未佩戴过 1 佩戴过
    private int wearState;
    // 勋章字典表外键
    private String fkMedalDicId;
    // 勋章数量
    private int medalNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFkLoginId() {
        return fkLoginId;
    }

    public void setFkLoginId(String fkLoginId) {
        this.fkLoginId = fkLoginId;
    }

    public void setMedalNum(int medalNum) {
        this.medalNum = medalNum;
    }

    public String getFkMedalDicId() {
        return fkMedalDicId;
    }

    public void setFkMedalDicId(String fkMedalDicId) {
        this.fkMedalDicId = fkMedalDicId;
    }

    public int getMedalState() {
        return medalState;
    }

    public void setMedalState(int medalState) {
        this.medalState = medalState;
    }

    public int getWearState() {
        return wearState;
    }

    public void setWearState(int wearState) {
        this.wearState = wearState;
    }

    public int getMedalNum() {
        return medalNum;
    }

}
