package com.mpen.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpen.api.bean.Friend;
import com.mpen.api.bean.FriendListInfo;
import com.mpen.api.bean.UserPhoto;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants.Sex;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbUserClass;
import com.mpen.api.domain.DdbUserRelationship;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.DdbUserClassMapper;
import com.mpen.api.mapper.DdbUserDynamicRecordMapper;
import com.mpen.api.mapper.DdbUserRelationshipMapper;
import com.mpen.api.mapper.PeCustomMapper;
import com.mpen.api.service.UserRelationshipService;
import com.mpen.api.util.CommUtil;

/**
 * 好友关系服务类
 * 涉及：App2.0好友模块
 */
@Service
public class UserRelationshipServiceImpl implements UserRelationshipService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRelationshipServiceImpl.class);
    @Autowired
    private PeCustomMapper peCustomMapper;
    @Autowired
    private DdbUserClassMapper ddbUserClassMapper;
    @Autowired
    private DdbUserRelationshipMapper ddbUserRelationshipMapper;
    @Autowired
    private DdbUserDynamicRecordMapper dynamicRecordMapper;

    /**
     * 搜索好友
     */
    @Override
    public Friend getFriend(String loginId, UserSession userSession) {
        final Friend friend = new Friend();
        final DdbPeCustom ddbPeCustom = peCustomMapper.getByLoginId(loginId);
        if (ddbPeCustom == null) {
            return null;
        }
        // 获取昵称
        final String nickName = ddbPeCustom.getNickName() == null ? "user_" + loginId.substring(7, 11)
                : ddbPeCustom.getNickName();
        // 获取头像
        final List<String> loginIds = new ArrayList<>();
        loginIds.add(loginId);
        try {
            if (loginIds.size() != 0) {
                final List<UserPhoto> listPhotos = CommUtil.listPhotos(loginIds);
                friend.setPhotoUrl(listPhotos.get(0).getPhoto());
            }
        } catch (SdkException e) {
            e.printStackTrace();
        }
        friend.setNickName(nickName);
        if (ddbPeCustom.getFlagGender() != null) {
            // 获取性别
            friend.setSex(ddbPeCustom.getFlagGender().equals("男") ? Sex.MALE : Sex.FEMALE);
        }
        // 获取学校，班级，老师
        final DdbUserClass ddbUserClass = ddbUserClassMapper.getClassByLoginId(loginId);
        if (ddbUserClass != null) {
            friend.setClassName(ddbUserClass.getClassName());
            friend.setTeacher(ddbUserClass.getEnglishTeacher());
            friend.setSchool(ddbUserClass.getSchool());
        }
        // 获取loginId
        friend.setLoginId(loginId);
        // 是否互为好友
        final DdbUserRelationship friends = ddbUserRelationshipMapper.getFriend(loginId, userSession.getLoginId());
        if (friends != null) {
            friend.setId(friends.getId());
            // 设置别名
            friend.setRemark(friends.getAliasUser());
            // 是好友
            friend.setIsFriend(true);
            // 获取好友动态数量
            final int count = dynamicRecordMapper.getTotalCount(loginIds);
            friend.setDynamicNum(String.valueOf(count));
        }
        return friend;
    }

    /**
     * 好友申请
     */
    @Override
    public boolean applyFriend(UserSession userSession, Friend friend) {
        // 自己不能添加自己为好友
        if (userSession.getLoginId().equals(friend.getLoginId())) {
            return false;
        }
        // 查找是否已经是好友
        final DdbUserRelationship isFriend = ddbUserRelationshipMapper.getFriend(userSession.getLoginId(),
                friend.getLoginId());
        if (isFriend != null) {
            return false;
        }
        // 查找是否已经存在好友申请
        final DdbUserRelationship friends = ddbUserRelationshipMapper.getIsFriend(userSession.getLoginId(),
                friend.getLoginId());
        if (friends != null) {
            ddbUserRelationshipMapper.updateUserApplyMsg(friends.getId(), friend.getRemark(),
                    friend.getValidationMes());
            return true;
        }
        final DdbUserRelationship ddbUserRelationship = new DdbUserRelationship();
        ddbUserRelationship.setId(CommUtil.genRecordKey());
        ddbUserRelationship.setCreateTime(new Date());
        ddbUserRelationship.setUserLoginId(userSession.getLoginId());
        ddbUserRelationship.setFriendLoginId(friend.getLoginId());
        ddbUserRelationship.setAliasUser(friend.getRemark());
        ddbUserRelationship.setReqMsg(friend.getValidationMes());
        ddbUserRelationshipMapper.saveFriend(ddbUserRelationship);
        return true;
    }

    /**
     * 修改好友备注
     */
    @Override
    public boolean changeRemark(UserSession userSession, Friend friend) {
        final String id = friend.getId();
        // 根据id查询好友数据
        final DdbUserRelationship ddbUserRelationship = ddbUserRelationshipMapper.getFriendById(id);
        if (ddbUserRelationship == null) {
            return false;
        }
        // 判断更新那个字段
        if (ddbUserRelationship.getUserLoginId().equals(userSession.getLoginId())) {
            ddbUserRelationshipMapper.updateAliasUser(friend.getRemark(), id);
        } else {
            ddbUserRelationshipMapper.updateAliasFriend(friend.getRemark(), id);
        }
        return true;
    }

    /**
     * 获取新的朋友列表
     */
    @Override
    public List<Friend> getNewFriendList(UserSession userSession) {
        final List<Friend> friends = new ArrayList<>();
        // 根据被申请者查找申请者loginId
        final List<DdbUserRelationship> ddbUserRelationships = ddbUserRelationshipMapper
                .getByFriendLoginId(userSession.getLoginId());
        if (ddbUserRelationships.size() == 0) {
            return null;
        }
        // 获取用户头像
        final List<String> loginIds = new ArrayList<>();
        for (DdbUserRelationship ddbUserRelationship : ddbUserRelationships) {
            loginIds.add(ddbUserRelationship.getUserLoginId());
        }
        final Map<String, String> photoMap = new HashMap<>();
        try {
            if (loginIds.size() != 0) {
                final List<UserPhoto> listPhotos = CommUtil.listPhotos(loginIds);
                for (UserPhoto userPhoto : listPhotos) {
                    photoMap.put(userPhoto.getLoginId(), userPhoto.getPhoto());
                }
            }
        } catch (SdkException e) {
            e.printStackTrace();
        }
        for (DdbUserRelationship ddbUserRelationship : ddbUserRelationships) {
            final Friend friend = new Friend();
            friend.setId(ddbUserRelationship.getId());
            friend.setLoginId(ddbUserRelationship.getUserLoginId());
            friend.setValidationMes(ddbUserRelationship.getReqMsg());
            friend.setThrough(ddbUserRelationship.getRelStatus());
            friend.setPhotoUrl(photoMap.get(ddbUserRelationship.getUserLoginId()));
            final DdbPeCustom ddbPeCustom = peCustomMapper.getByLoginId(ddbUserRelationship.getUserLoginId());
            if (ddbPeCustom != null) {
                final String nickName = ddbPeCustom.getNickName() == null
                        ? "user_" + ddbUserRelationship.getUserLoginId().substring(7, 11) : ddbPeCustom.getNickName();
                friend.setNickName(nickName);
            }
            friends.add(friend);
        }
        return friends;
    }
    /**
     * 好友列表
     */
    @Override
    public FriendListInfo listFriend(UserSession userSession) throws SdkException {
        final String loginId = userSession.getLoginId();
        final FriendListInfo friendInfo = new FriendListInfo();
        // 获取好友列表
        final List<DdbUserRelationship> relationships = ddbUserRelationshipMapper.getFriends(loginId);

        // 获取申请个数
        final int applyNum = ddbUserRelationshipMapper.getApplyNum(loginId);
        friendInfo.setApplyNum(applyNum);
        // 获取用户昵称
        final List<DdbUserRelationship> passedFriends = new ArrayList<>();

        final List<String> loginIds = new ArrayList<>();
        for (DdbUserRelationship relationship : relationships) {
            passedFriends.add(relationship);
            loginIds.add(relationship.getFriendLoginId());
        }

        final List<Friend> friends = new ArrayList<>();

        if (loginIds != null && loginIds.size() > 0) {
            // 获取用户头像
            final List<DdbPeCustom> peCustoms = peCustomMapper.listByLoginIds(loginIds);
            final List<UserPhoto> listPhotos = CommUtil.listPhotos(loginIds);
            for (DdbUserRelationship relationship : passedFriends) {
                final Friend friend = new Friend();
                friend.setId(relationship.getId());
                friend.setLoginId(relationship.getFriendLoginId());
                for (UserPhoto userPhoto : listPhotos) {
                    if (relationship.getFriendLoginId().equals(userPhoto.getLoginId())) {
                        friend.setPhotoUrl(userPhoto.getPhoto());
                        break;
                    }
                }
                if (StringUtils.isBlank(relationship.getAliasUser())) {
                    for (DdbPeCustom peCustom : peCustoms) {
                        final String loginIdPe = peCustom.getLoginId();
                        if (relationship.getFriendLoginId().equals(loginIdPe)) {
                            final String nickName = peCustom.getNickName() != null ? peCustom.getNickName()
                                    : "user_" + loginIdPe.substring(loginIdPe.length() - 4);
                            friend.setNickName(nickName);
                            break;
                        }
                    }
                } else {
                    friend.setNickName(relationship.getAliasUser());
                }
                friends.add(friend);
            }
        }
        friendInfo.setFriends(friends);
        return friendInfo;
    }

    /**
     * 删除好友，物理删除
     */
    @Override
    public boolean deleteFriend(Friend friend) {
        ddbUserRelationshipMapper.deleteById(friend.getId());
        return true;
    }

    /**
     * 删除好友，逻辑删除
     */
    @Override
    public boolean deleteNewFriend(Friend friend) {
        ddbUserRelationshipMapper.updateNewFriendById(friend.getId());
        return true;
    }

    /**
     * 好友申请通过
     */
    @Override
    public boolean passFriend(Friend friend) {
        ddbUserRelationshipMapper.passFriend(friend.getId());
        return true;
    }
}
