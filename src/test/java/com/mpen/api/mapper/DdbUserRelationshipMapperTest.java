package com.mpen.api.mapper;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mpen.TestBase;
import com.mpen.api.domain.DdbUserRelationship;

/**
 * 操作好友表Mapper测试类
 * 
 * @author sxg
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbUserRelationshipMapperTest extends TestBase {

    @Autowired
    private DdbUserRelationshipMapper ddbUserRelationshipMapper;

    /**
     * 好友相关sql测试
     */
    @Test
    @Transactional(rollbackFor = Exception.class)
    public void testDdbUserRelationship() {
        // 获取用户user的五个好友
        final List<DdbUserRelationship> ddbUserRelationships = getDdbUserRelationships();

        // 添加好友
        for (DdbUserRelationship ddbUserRelationship : ddbUserRelationships) {
            // 向好友发送请求
            final int save = this.ddbUserRelationshipMapper.saveFriend(ddbUserRelationship);
            Assert.assertEquals(save, 1);

            // 获取该用户被申请的数量
            final int getApplyNumber = this.ddbUserRelationshipMapper
                    .getApplyNum(ddbUserRelationship.getFriendLoginId());
            Assert.assertEquals(getApplyNumber, 1);

            // 好友请求通过
            final int passFriend = this.ddbUserRelationshipMapper.passFriend(ddbUserRelationship.getId());
            Assert.assertEquals(passFriend, 1);
        }

        // 查询好友列表
        final DdbUserRelationship user = ddbUserRelationships.get(0);
        final List<DdbUserRelationship> listFriends = this.ddbUserRelationshipMapper.getFriends(user.getUserLoginId());
        Assert.assertEquals(listFriends.size(), 5);

        // 查找用户的指定好友
        final DdbUserRelationship friend = ddbUserRelationships.get(1);
        final DdbUserRelationship getFriend = this.ddbUserRelationshipMapper.getFriend(user.getUserLoginId(),
                friend.getFriendLoginId());
        Assert.assertEquals(getFriend.getFriendLoginId(), friend.getFriendLoginId());

        // 查看是否是好友，
        final DdbUserRelationship isFriend = this.ddbUserRelationshipMapper.getIsFriend(user.getUserLoginId(),
                friend.getFriendLoginId());
        Assert.assertEquals(isFriend, null);

        // 更新用户别名
        final int updateUserAlias = this.ddbUserRelationshipMapper.updateAliasUser(user.getId(), "updateUserAlias");
        Assert.assertEquals(updateUserAlias, 1);

        // 更新好友别名
        final int updateFriendAlias = this.ddbUserRelationshipMapper.updateAliasFriend("updateFriendAlias",
                user.getId());
        Assert.assertEquals(updateFriendAlias, 1);

        // 根据ID查询好友
        final DdbUserRelationship getFriendById = this.ddbUserRelationshipMapper.getFriendById(user.getId());
        Assert.assertEquals(getFriendById.getFriendLoginId(), user.getFriendLoginId());

        // 根据ID逻辑删除好友
        final int isDel = this.ddbUserRelationshipMapper.updateNewFriendById(user.getId());
        Assert.assertEquals(isDel, 1);

        // 获取好友被多少人加过
        final List<DdbUserRelationship> listFriendByLoginid = this.ddbUserRelationshipMapper
                .getByFriendLoginId(friend.getFriendLoginId());
        Assert.assertEquals(listFriendByLoginid.size(), 1);

        // 获取用户好友的分页列表
        final List<DdbUserRelationship> listFriendsPage = this.ddbUserRelationshipMapper
                .getFriendsPage(user.getUserLoginId(), 0, 3);
        Assert.assertEquals(listFriendsPage.size(), 3);

        // 更新申请消息
        final int updateApplayMessag = this.ddbUserRelationshipMapper.updateUserApplyMsg(user.getId(),
                "updateUserAlias2", "my name is updateUserAlias2");
        Assert.assertEquals(updateApplayMessag, 1);

        // 删除好友
        for (DdbUserRelationship ddbUserRelationship : ddbUserRelationships) {
            // 根据ID删除好友
            final int deleteById = this.ddbUserRelationshipMapper.deleteById(ddbUserRelationship.getId());
            Assert.assertEquals(deleteById, 1);
        }
    }
}
