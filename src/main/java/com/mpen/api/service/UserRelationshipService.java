package com.mpen.api.service;

import java.util.List;

import com.mpen.api.bean.Friend;
import com.mpen.api.bean.FriendListInfo;
import com.mpen.api.bean.UserSession;
import com.mpen.api.exception.SdkException;

public interface UserRelationshipService {


    Friend getFriend(String loginId, UserSession userSession);

    boolean applyFriend(UserSession userSession,Friend friend);

    boolean changeRemark(UserSession userSession, Friend friend);

    List<Friend> getNewFriendList(UserSession userSession);
    
    FriendListInfo listFriend(UserSession userSession) throws SdkException;

    boolean deleteFriend(Friend friend);

    boolean passFriend(Friend friend);

    boolean deleteNewFriend(Friend friend);

}
