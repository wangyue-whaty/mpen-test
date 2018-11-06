package com.mpen.api.bean;

import java.util.List;
/**
 * 个人动态消息
 */
public class PersonDynamicInfo {
    // 动态记录id
    private String id;
    // 动态头像
    private String photo;
    // 发表时间
    private long time;
    // 发表动态loginId
    private String loginId;
    // 发表动态人昵称
    private String nickName;
    // 动态内容
    private String dynamicContent;
    // 点赞标识
    private int isPraise;
    // 点赞数量
    private int num;
    // 点赞人头像集合
    private List<String> photos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDynamicContent() {
        return dynamicContent;
    }

    public void setDynamicContent(String dynamicContent) {
        this.dynamicContent = dynamicContent;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

}
