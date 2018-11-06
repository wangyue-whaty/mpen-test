package com.mpen.api.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mpen.api.bean.ClassInfo;
import com.mpen.api.bean.UserPhoto;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbUserClass;
import com.mpen.api.domain.DdbUserClassRela;
import com.mpen.api.domain.DdbUserIntegralRecord;
import com.mpen.api.domain.DdbUserMedalDictionary;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.DdbUserClassMapper;
import com.mpen.api.mapper.DdbUserClassRelaMapper;
import com.mpen.api.mapper.DdbUserIntegralRecordMapper;
import com.mpen.api.mapper.DdbUserMedalDictionaryMapper;
import com.mpen.api.service.DdbUserClassRelaService;
import com.mpen.api.service.DdbUserDynamicRecordService;
import com.mpen.api.service.DdbUserMessageService;
import com.mpen.api.service.MedalService;
import com.mpen.api.util.CommUtil;

/**
 * 班级学生表 服务实现类
 * 涉及：App2.0班级模块
 * @author hzy
 * @since 2018-07-03
 */
@Component
public class DdbUserClassRelaServiceImpl implements DdbUserClassRelaService {
    @Autowired
    private DdbUserClassRelaMapper userClassRelaMapper;
    @Autowired
    private DdbUserClassMapper userClassMapper;
    @Autowired
    private DdbUserIntegralRecordMapper ddbUserIntegralRecordMapper;
    @Autowired
    private DdbUserMessageService ddbUserMessageService;
    @Autowired
    private DdbUserDynamicRecordService ddbUserDynamicRecordService;
    @Autowired
    private MedalService medalService;
    @Autowired
    private DdbUserMedalDictionaryMapper medalDictionaryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ClassInfo classInfo, UserSession user) {
        // 获取用户班级
        final DdbUserClass userClass = userClassMapper.getClassByLoginId(user.getLoginId());
        // 判断用户班级是否为null
        if (userClass != null) {
            // 不为空则判断是否为有老师班级
            if (userClass.getType() != 1) {
                // 是则返回false(应先退出有老师班级,然后再加入班级)
                return false;
            } else {
                // 为无老师班级,则删除对应用户班级记录
                userClassRelaMapper.deleteByLoginIdClassId(user.getLoginId(), userClass.getId());
            }
        }
        // 保存加入班级信息
        final DdbUserClassRela userClassRela = new DdbUserClassRela();
        userClassRela.setId(CommUtil.genRecordKey());
        userClassRela.setFkClassId(classInfo.getId());
        userClassRela.setFkLoginId(user.getLoginId());
        userClassRela.setTrueName(classInfo.getTrueName());
        Date curDate = new Date();
        userClassRela.setCreateTime(curDate);
        userClassRela.setUpdateTime(curDate);
        userClassRelaMapper.create(userClassRela);
        DdbUserIntegralRecord userIntegral = ddbUserIntegralRecordMapper.getUserIntegralByloginIdAndType(user.getLoginId(), "设置班级");
        if (userIntegral == null) {
            Constants.CACHE_THREAD_POOL.execute(() -> {
                saveMedalAndDynamicAndMsgAndIntegral(user);
            });
        }
        return true;
    }

    /**
     * 完善班级信息获取勋章
     */
    @Transactional(rollbackFor = Exception.class)
    private void saveMedalAndDynamicAndMsgAndIntegral(UserSession userSession) {
        final DdbUserMedalDictionary dictionary = medalDictionaryMapper.getByMedalType(Constants.COMPLETEEXERCISE);
        final String medalDynamic = dictionary.getMedalDynamic();
        // 保存动态
        ddbUserDynamicRecordService.saveDynamic(userSession.getLoginId(), "完善班级", medalDynamic);
        // 保存用户获得勋章记录
        medalService.saveMedal(userSession.getLoginId(), Constants.IMPROVINGCLASSINF, 1);
        // 保存消息记录
        ddbUserMessageService.saveMessage(userSession.getLoginId(), Constants.MEDAL_RECORD, medalDynamic,
                Constants.StudentMsgPushType.MEDAL.toString());
        final DdbUserIntegralRecord ddbUserIntegralRecord = new DdbUserIntegralRecord();
        final Date curDate=new Date();
        ddbUserIntegralRecord.setCreateTime(curDate);
        ddbUserIntegralRecord.setIntegral(200);
        ddbUserIntegralRecord.setFkLoginId(userSession.getLoginId());
        ddbUserIntegralRecord.setUpdateTime(curDate);
        ddbUserIntegralRecord.setIntegralType("设置班级");
        ddbUserIntegralRecord.setId(CommUtil.genRecordKey());
        // 保存积分
        ddbUserIntegralRecordMapper.save(ddbUserIntegralRecord);
    }

    @Override
    public List<DdbUserClassRela> listByClassId(String classId) {
        return userClassRelaMapper.getDdbUserClassRelaByClassId(classId);
    }

    /**
     * 获取班级学生头像
     */
    @Override
    public List<DdbUserClassRela> listPhotos(List<DdbUserClassRela> userClassRelas) throws SdkException {
        // 获取loginId集合
        final List<String> loginIds = new ArrayList<>();
        for (DdbUserClassRela ddbUserClassRela : userClassRelas) {
            loginIds.add(ddbUserClassRela.getFkLoginId());
        }
        // 请求用户中心获取头像集合
        final List<UserPhoto> listPhotos = CommUtil.listPhotos(loginIds);
        // 处理数据并排序
        for (DdbUserClassRela classRela : userClassRelas) {
            for (UserPhoto userPhoto : listPhotos) {
                if (classRela.getFkLoginId().equals(userPhoto.getLoginId())) {
                    classRela.setPhoto(userPhoto.getPhoto());
                }
            }
        }
        Collections.sort(userClassRelas, new Comparator<DdbUserClassRela>() {
            @Override
            public int compare(DdbUserClassRela o1, DdbUserClassRela o2) {
                if (o1.getCreateTime() != null && o2.getCreateTime() != null) {
                    return o1.getCreateTime().toString().compareTo(o2.getCreateTime().toString());
                } else if (o1.getCreateTime() != null) {
                    return 1;
                } else if (o2.getCreateTime() != null) {
                    return -1;
                } else { // both null
                    return 0;
                }
            }
        });
        return userClassRelas;
    }

}