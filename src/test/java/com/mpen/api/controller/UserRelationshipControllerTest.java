package com.mpen.api.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.bean.Friend;
import com.mpen.api.common.Constants;

/**
 * 好友模块controller测试
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserRelationshipControllerTest extends TestBase {
    private static final String FRIENDS_RELATIONSHIP = "/v1/friends/relationship";
    private static final String userName = "18931334240";
    private static final String password = "5455001765033-1540449365985-088c7c75d9bcd8854450390aa9d206df";

    /**
     * 好友相关sql测试
     */
    @Test
    public void testUserRelationshipController() {
        final Friend friend = new Friend();
        friend.setAction("apply");
        friend.setLoginId("17600884244");
        friend.setRemark("好好学习zo9");
        friend.setValidationMes("17600884244");
        // 查找好友
        this.getControllerTest(userName, password, FRIENDS_RELATIONSHIP + "?action=search&loginId=15003232810");
        // 请求好友
        this.postControllerTest(userName, password, friend, FRIENDS_RELATIONSHIP);
        // 查看新增好友列表
        this.getControllerTest(userName, password, FRIENDS_RELATIONSHIP + "?action=getNewFriendList");
        friend.setId("1132db6adf854b1f8a61fc42c3af172e");
        friend.setAction(Constants.PASS_FRIEND);
        // 同意申请
        this.postControllerTest(userName, password, friend, FRIENDS_RELATIONSHIP);
        friend.setId("1132db6adf854b1f8a61fc42c3af172e");
        friend.setRemark("小李");
        friend.setAction("changeRemark");
        // 修改好友的备注
        this.postControllerTest(userName, password, friend, FRIENDS_RELATIONSHIP);
        // 用户查看好友列表
        this.getControllerTest(userName, password, FRIENDS_RELATIONSHIP + "?action=friendList");
        friend.setId("1132db6adf854b1f8a61fc42c3af172e");
        friend.setAction("deleteNewFriend");
        // 新的朋友删除
        this.postControllerTest(userName, password, friend, FRIENDS_RELATIONSHIP);
        friend.setId("1132db6adf854b1f8a61fc42c3af172e");
        friend.setAction("deleteNewFriend");
        // 删除好友
        this.postControllerTest(userName, password, friend, FRIENDS_RELATIONSHIP);
    }
}
