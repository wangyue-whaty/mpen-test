package com.mpen.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mp.shared.common.Page;
import com.mpen.api.bean.Dynamic;
import com.mpen.api.bean.PersonDynamicInfo;
import com.mpen.api.bean.UserCoverInfo;
import com.mpen.api.bean.UserPhoto;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbUserDynamicPraise;
import com.mpen.api.domain.DdbUserDynamicRecord;
import com.mpen.api.domain.DdbUserRelationship;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.DdbUserDynamicPraiseMapper;
import com.mpen.api.mapper.DdbUserDynamicRecordMapper;
import com.mpen.api.mapper.DdbUserIntegralRecordMapper;
import com.mpen.api.mapper.DdbUserMedalRecordMapper;
import com.mpen.api.mapper.DdbUserRelationshipMapper;
import com.mpen.api.mapper.PeCustomMapper;
import com.mpen.api.mapper.RecordUserBookMapper;
import com.mpen.api.service.DdbUserDynamicRecordService;
import com.mpen.api.util.CommUtil;

/**
 * 用户动态记录服务类 涉及：教师端批阅作业以及app2.0相关接口
 * 
 * @author hzy
 * @since 2018-08-09
 */
@Component
public class DdbUserDynamicRecordServiceImpl implements DdbUserDynamicRecordService {
    @Autowired
    private DdbUserDynamicRecordMapper dynamicRecordMapper;
    @Autowired
    private DdbUserDynamicPraiseMapper dynamicPraiseMapper;
    @Autowired
    private DdbUserRelationshipMapper relationshipMapper;
    @Autowired
    private PeCustomMapper peCustomMapper;
    @Autowired
    private DdbUserMedalRecordMapper medalRecordMapper;
    @Autowired
    private DdbUserIntegralRecordMapper ddbUserIntegralRecordMapper;
    @Autowired
    private RecordUserBookMapper recordUserBookMapper;
    @Autowired
    private DdbUserDynamicRecordMapper ddbUserDynamicRecordMapper;

    /**
     * 获取动态列表
     */
    @Override
    public Page<PersonDynamicInfo> pageDynamic(Dynamic dynamic, UserSession user) {
        final String loginId = dynamic.getLoginId();
        // 获取好友列表
        final List<DdbUserRelationship> friends = relationshipMapper.getFriends(loginId);
        final List<String> dynamicLoginIds = new ArrayList<>();
        dynamicLoginIds.add(loginId);
        for (DdbUserRelationship relationship : friends) {
            dynamicLoginIds.add(relationship.getFriendLoginId());
        }
        return getPageDynamic(dynamic, dynamicLoginIds, user);
    }

    @Override
    public Page<PersonDynamicInfo> pagePersonalDynamic(Dynamic dynamic, UserSession user) {
        final List<String> dynamicLoginIds = new ArrayList<>();
        // 好友的loginId动态
        final List<DdbUserRelationship> byFriendLoginId = relationshipMapper.getFriends(user.getLoginId());
        for (DdbUserRelationship ddbUserRelationship : byFriendLoginId) {
            dynamicLoginIds.add(ddbUserRelationship.getFriendLoginId());
        }
        dynamicLoginIds.add(dynamic.getLoginId());
        return getPageDynamic(dynamic, dynamicLoginIds, user);
    }

    private Page<PersonDynamicInfo> getPageDynamic(Dynamic dynamic, List<String> dynamicLoginIds, UserSession user) {
        // 获取分页动态
        final Page<PersonDynamicInfo> page = new Page<PersonDynamicInfo>();
        // 获取总记录数
        final int totalCount = dynamicRecordMapper.getTotalCount(dynamicLoginIds);
        // 处理数据
        final List<PersonDynamicInfo> dynamicInfos = new ArrayList<>();
        final Map<String, String> loginIdPhotoMap = new HashMap<>();
        final List<String> loginIds = new ArrayList<>();
        if (totalCount == 0) {
            // TODO 老用户默认一条动态记录
            final PersonDynamicInfo dynamicInfo = new PersonDynamicInfo();
            loginIds.add(user.getLoginId());
            List<UserPhoto> listPhotos;
            try {
                listPhotos = CommUtil.listPhotos(loginIds);
                dynamicInfo.setPhoto(listPhotos.get(0).getPhoto());
            } catch (SdkException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // TODO 老用户注册时无创建时间，默认2017-01-01
            dynamicInfo.setTime(CommUtil.parseTimeFormattoDate("2017-01-01").getTime());
            dynamicInfo
                    .setDynamicContent("恭喜你成功注册为 外研通会员 开启了和好友互动的旅程，<br/>获得勋章 <font color='#ff7951'[初出茅庐]></font> 一枚");
            dynamicInfo.setLoginId(user.getLoginId());
            dynamicInfo.setNickName(user.getPeCustom().getNickName());
            dynamicInfos.add(dynamicInfo);
            page.setItems(dynamicInfos);
            page.setTotalCount(1);
            page.setPageNo(1);
            return page;
        }

        // 获取昵称
        final List<DdbPeCustom> peCustoms = peCustomMapper.listByLoginIds(dynamicLoginIds);
        final Map<String, String> loginIdNickNameMap = new HashMap<>();
        for (DdbPeCustom ddbPeCustom : peCustoms) {
            final String loginId = ddbPeCustom.getLoginId();
            final String nickName = ddbPeCustom.getNickName() != null ? ddbPeCustom.getNickName()
                    : "user_" + loginId.substring(loginId.length() - 4);
            loginIdNickNameMap.put(loginId, nickName);
        }

        final List<DdbUserDynamicRecord> userDynamics = dynamicRecordMapper.listDynamics(dynamic.getPageIndex(),
                dynamic.getPageSize(), dynamicLoginIds);
        if (userDynamics == null || userDynamics.size() == 0) {
            return page;
        }
        final List<String> dynamicIds = new ArrayList<>();

        for (DdbUserDynamicRecord dynamicRecord : userDynamics) {
            dynamicIds.add(dynamicRecord.getId());
            if (!loginIds.contains(dynamicRecord.getFkLoginId())) {
                loginIds.add(dynamicRecord.getFkLoginId());
            }
        }
        // 获取点赞记录
        final List<DdbUserDynamicPraise> dynamicPraises = dynamicPraiseMapper.listByDynamicIds(dynamicIds);

        for (DdbUserDynamicPraise dynamicPraise : dynamicPraises) {
            if (!loginIds.contains(dynamicPraise.getFkLoginId())) {
                loginIds.add(dynamicPraise.getFkLoginId());
            }
        }

        try {
            final List<UserPhoto> listPhotos = CommUtil.listPhotos(loginIds);
            for (UserPhoto userPhoto : listPhotos) {
                loginIdPhotoMap.put(userPhoto.getLoginId(), userPhoto.getPhoto());
            }
        } catch (SdkException e) {
            e.printStackTrace();
        }

        for (DdbUserDynamicRecord dynamicRecord : userDynamics) {
            final PersonDynamicInfo dynamicInfo = new PersonDynamicInfo();
            dynamicInfo.setTime(dynamicRecord.getCreateTime().getTime());
            dynamicInfo.setDynamicContent(dynamicRecord.getDynamicContent());
            dynamicInfo.setLoginId(dynamicRecord.getFkLoginId());
            dynamicInfo.setNickName(loginIdNickNameMap.get(dynamicRecord.getFkLoginId()));
            dynamicInfo.setPhoto(loginIdPhotoMap.get(dynamicRecord.getFkLoginId()));
            final List<String> photos = new ArrayList<>();
            int isPraise = 0;
            for (DdbUserDynamicPraise dynamicPraise : dynamicPraises) {
                if (dynamicRecord.getId().equals(dynamicPraise.getFkDynamicId())) {
                    photos.add(loginIdPhotoMap.get(dynamicPraise.getFkLoginId()));
                    if (dynamicPraise.getFkLoginId().equals(user.getLoginId())) {
                        isPraise = 1;
                    }
                }
            }
            dynamicInfo.setIsPraise(isPraise);
            dynamicInfo.setNum(photos.size());
            dynamicInfo.setPhotos(photos);
            dynamicInfo.setId(dynamicRecord.getId());
            dynamicInfos.add(dynamicInfo);
        }
        page.setItems(dynamicInfos);
        page.setTotalCount(totalCount);
        page.setPageNo(dynamic.getPageNo());
        return page;
    }

    @Override
    public UserCoverInfo getUserCover(UserSession user) throws SdkException {
        final String loginId = user.getLoginId();
        // 获取勋章数量
        final int medalSum = medalRecordMapper.getMedalSum(user.getLoginId());
        // 获取昵称及封面
        final DdbPeCustom peCustom = peCustomMapper.getByLoginId(loginId);

        final String nickName = peCustom.getNickName() != null ? peCustom.getNickName()
                : "user_" + loginId.substring(loginId.length() - 4);
        // 获取用户头像
        final List<String> loginIds = new ArrayList<>();
        loginIds.add(loginId);
        final List<UserPhoto> listPhotos = CommUtil.listPhotos(loginIds);
        String photo = null;
        if (listPhotos != null && listPhotos.size() > 0) {
            photo = listPhotos.get(0).getPhoto();
        }
        final UserCoverInfo userCoverInfo = new UserCoverInfo();
        userCoverInfo.setCover(peCustom.getCover());
        userCoverInfo.setPhoto(photo);
        userCoverInfo.setMedalNum(medalSum);
        userCoverInfo.setNickName(nickName);
        return userCoverInfo;
    }

    /**
     * 动态保存
     */
    @Override
    public void saveDynamic(String loginId, String type, String content) {
        final DdbUserDynamicRecord dynamicRecord = new DdbUserDynamicRecord();
        dynamicRecord.setId(CommUtil.genRecordKey());
        dynamicRecord.setFkLoginId(loginId);
        final Date curDate = new Date();
        dynamicRecord.setCreateTime(curDate);
        dynamicRecord.setDynamicContent(content);
        dynamicRecord.setUpdateTime(curDate);
        // 动态保存
        ddbUserDynamicRecordMapper.save(dynamicRecord);
    }

}