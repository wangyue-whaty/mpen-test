package com.mpen.api.bean;

/**
 * 我的勋章 涉及：App2.0涉及勋章相关接口
 */
public class MyMedal {
    private String medalId;
    // 勋章名字
    private String medalName;
    // 勋章口号
    private String slogan;
    // 勋章规则
    private String medalRule;
    // 用户头像url
    private String photoUrl;
    // 单个勋章数量
    private int medalNum;
    // 佩戴 0 拆下 1
    private String type;
    // 勋章状态 0未获得 1已获得 2佩戴中
    private int medalState;
    // 佩戴状态 0未佩戴过 1佩戴过
    private int wearState;
    // 最近获取 1
    private int recObtain;

    private String action;

    public String getMedalName() {
        return medalName;
    }

    public void setMedalName(String medalName) {
        this.medalName = medalName;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getMedalRule() {
        return medalRule;
    }

    public void setMedalRule(String medalRule) {
        this.medalRule = medalRule;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getMedalNum() {
        return medalNum;
    }

    public void setMedalNum(int medalNum) {
        this.medalNum = medalNum;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMedalId() {
        return medalId;
    }

    public void setMedalId(String medalId) {
        this.medalId = medalId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRecObtain() {
        return recObtain;
    }

    public void setRecObtain(int recObtain) {
        this.recObtain = recObtain;
    }

}
