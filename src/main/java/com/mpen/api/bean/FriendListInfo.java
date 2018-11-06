package com.mpen.api.bean;

import java.util.List;

/**
 * 好友列表信息 涉及：App2.0好友模块
 *
 */
public class FriendListInfo {
    // 申请好友的个数
    private int applyNum;
    // 好友信息
    private List<Friend> friends;

    public int getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(int applyNum) {
        this.applyNum = applyNum;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

}
