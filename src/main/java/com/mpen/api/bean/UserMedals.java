package com.mpen.api.bean;

import java.util.List;

/**
 * 用户勋章
 * 
 */
public class UserMedals {
    // 勋章
    private List<MyMedal> myMedals;
    // 用户头像
    private String photoUrl;
    // 勋章总数
    private int medalSum;

    public List<MyMedal> getMyMedals() {
        return myMedals;
    }

    public void setMyMedals(List<MyMedal> myMedals) {
        this.myMedals = myMedals;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setMedalSum(int medalSum) {
        this.medalSum = medalSum;
    }

    public int getMedalSum() {
        return medalSum;
    }

}
