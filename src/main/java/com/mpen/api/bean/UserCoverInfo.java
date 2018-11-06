package com.mpen.api.bean;

/**
 * 用户封面 涉及：勋章模块
 *
 */
public class UserCoverInfo {
    // 昵称
    private String nickName;
    // 封面
    private String cover;
    // 头像
    private String photo;
    // 勋章数量
    private int medalNum;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getMedalNum() {
        return medalNum;
    }

    public void setMedalNum(int medalNum) {
        this.medalNum = medalNum;
    }
}
