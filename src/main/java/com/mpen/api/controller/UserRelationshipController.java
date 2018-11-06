package com.mpen.api.controller;

import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mp.shared.common.NetworkResult;
import com.mpen.api.bean.Friend;
import com.mpen.api.bean.IntegralRecord;
import com.mpen.api.bean.UserIntegralSet;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.service.UserRelationshipService;
import com.mpen.api.service.UserSessionService;

/**
 * 用户好友操作控制类
 * 涉及：App2.0好友模块
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_FRIENDS)
public class UserRelationshipController {
    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private UserRelationshipService userRelationshipService;

    /*
     * 好友信息
     */
    @GetMapping(Uris.RELATIONSHIP)
    public @ResponseBody Callable<NetworkResult<Object>> getComments(Friend friend, final HttpServletRequest request,
            final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession userSession = userSessionService.getUser(request, response);
                switch (friend.getAction()) {
                // 搜索用户
                case Constants.SEARCH:
                    final Friend myFriend = userRelationshipService.getFriend(friend.getLoginId(), userSession);
                    if (myFriend == null) {
                        return RsHelper.success("搜索的用户不存在");
                    }
                    return RsHelper.success(myFriend);
                // 新的朋友列表
                case Constants.GET_NEWFRIENDLIST:
                    final List<Friend> newFriendList = userRelationshipService.getNewFriendList(userSession);

                    if (newFriendList == null || newFriendList.size() <= 0) {
                        return RsHelper.success("新好友列表为空");
                    }
                    return RsHelper.success(newFriendList);
                // 好友列表
                case Constants.FRIEND_LIST:
                    return RsHelper.success(userRelationshipService.listFriend(userSession));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }

    @PostMapping(Uris.RELATIONSHIP)
    public @ResponseBody Callable<NetworkResult<Object>> savePraiseNum(@RequestBody final Friend friend,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession userSession = userSessionService.getUser(request, response);
                switch (friend.getAction()) {
                // 好友申请
                case Constants.APPLY:
                    return RsHelper.success(userRelationshipService.applyFriend(userSession, friend));
                // 修改备注
                case Constants.CHANGE_REMARK:
                    return RsHelper.success(userRelationshipService.changeRemark(userSession, friend));
                // 删除好友，物理删除
                case Constants.DELETE_FRIEND:
                    return RsHelper.success(userRelationshipService.deleteFriend(friend));
                // 删除新的好友,逻辑删除
                case Constants.DELETE_NEW_FRIEND:
                    return RsHelper.success(userRelationshipService.deleteNewFriend(friend));
                // 好友请求通过
                case Constants.PASS_FRIEND:
                    return RsHelper.success(userRelationshipService.passFriend(friend));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }

}
