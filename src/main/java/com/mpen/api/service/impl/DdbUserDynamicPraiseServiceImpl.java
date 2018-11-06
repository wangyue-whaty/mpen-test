package com.mpen.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mpen.api.bean.PersonDynamicInfo;
import com.mpen.api.bean.UserPhoto;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbUserDynamicPraise;
import com.mpen.api.domain.DdbUserDynamicRecord;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.DdbUserDynamicPraiseMapper;
import com.mpen.api.mapper.DdbUserDynamicRecordMapper;
import com.mpen.api.service.DdbUserDynamicPraiseService;
import com.mpen.api.service.DdbUserMessageService;
import com.mpen.api.util.CommUtil;

/**
 * 动态点赞表 服务实现类 涉及：App2.0动态相关
 * 
 * @author hzy
 * @since 2018-08-09
 */
@Component
public class DdbUserDynamicPraiseServiceImpl implements DdbUserDynamicPraiseService {
    @Autowired
    private DdbUserDynamicPraiseMapper dynamicPraiseMapper;
    @Autowired
    private DdbUserDynamicRecordMapper dynamicRecordMapper;
    @Autowired
    private DdbUserMessageService userMessageService;
    @Autowired
    private DdbUserDynamicRecordMapper ddbUserDynamicRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PersonDynamicInfo savePraise(UserSession user, String id) {
        // 判断是否已点赞
        final DdbUserDynamicPraise praise = dynamicPraiseMapper.getByDynamicIdLoginId(user.getLoginId(), id);
        if (praise == null) {
            final DdbUserDynamicPraise dynamicPraise = new DdbUserDynamicPraise();
            dynamicPraise.setId(CommUtil.genRecordKey());
            dynamicPraise.setFkDynamicId(id);
            dynamicPraise.setFkLoginId(user.getLoginId());
            final Date curDate = new Date();
            dynamicPraise.setCreateTimeInMs(curDate.getTime());
            dynamicPraise.setUpdateTimeInMs(curDate.getTime());
            dynamicPraiseMapper.save(dynamicPraise);

            Constants.CACHE_THREAD_POOL.execute(() -> {
                // 获取动态详情
                final DdbUserDynamicRecord dynamicRecord = dynamicRecordMapper.getById(id);
                String nickeName = user.getPeCustom().getNickName();
                final String loginId = user.getLoginId();
                nickeName = nickeName != null ? nickeName : loginId.substring(loginId.length() - 4);
                final String content = nickeName + "，点赞了你，“" + dynamicRecord.getDynamicContent() + "”";
                userMessageService.saveMessage(loginId, Constants.FABULOUS_ME, content,
                        Constants.StudentMsgPushType.MYDYNAMIC.toString());
            });
        }
        // 返回点赞动态和用户信息，app端局部刷新
        final List<String> dynamicIds = new ArrayList<>();
        dynamicIds.add(id);
        // 获取点赞记录
        final List<DdbUserDynamicPraise> dynamicPraises = dynamicPraiseMapper.listByDynamicIds(dynamicIds);
        final List<String> loginIds = new ArrayList<>();
        for (DdbUserDynamicPraise ddbUserDynamicPraise : dynamicPraises) {
            loginIds.add(ddbUserDynamicPraise.getFkLoginId());
        }
        // 获取点赞人头像
        final List<String> loginIdPhotoList = new ArrayList<>();
        try {
            final List<UserPhoto> listPhotos = CommUtil.listPhotos(loginIds);
            for (UserPhoto userPhoto : listPhotos) {
                loginIdPhotoList.add(userPhoto.getPhoto());
            }
        } catch (SdkException e) {
            e.printStackTrace();
        }
        final PersonDynamicInfo dynamicInfo = new PersonDynamicInfo();
        dynamicInfo.setPhotos(loginIdPhotoList);
        return dynamicInfo;
    }
}