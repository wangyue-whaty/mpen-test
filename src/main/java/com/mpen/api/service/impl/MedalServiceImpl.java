package com.mpen.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpen.api.bean.MyMedal;
import com.mpen.api.bean.UserMedals;
import com.mpen.api.bean.UserPhoto;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbUserMedalDictionary;
import com.mpen.api.domain.DdbUserMedalRecord;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.DdbUserMedalDictionaryMapper;
import com.mpen.api.mapper.DdbUserMedalRecordMapper;
import com.mpen.api.service.MedalService;
import com.mpen.api.util.CommUtil;

/**
 * 勋章服务类 
 * 涉及：教师端批阅作业以及app2.0相关接口
 */
@Service
public class MedalServiceImpl implements MedalService {

    @Autowired
    private DdbUserMedalRecordMapper ddbUserMedalRecordMapper;
    @Autowired
    private DdbUserMedalDictionaryMapper ddbUserMedalDictionaryMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(MedalServiceImpl.class);

    /**
     * 获取用户勋章信息
     */
    @Override
    public UserMedals getMyMedal(UserSession userSession) {
        final String loginId = userSession.getLoginId();
        if (loginId == null) {
            return null;
        }
        final UserMedals userMedals = new UserMedals();
        final List<MyMedal> medals = new ArrayList<>();
        // 查询字典表信息，查询勋章信息
        final List<DdbUserMedalDictionary> ddbUserMedalDictionarys = ddbUserMedalDictionaryMapper.getAll();
        // 根据用户信息佩戴的状态、数量
        final List<DdbUserMedalRecord> Usermedals = ddbUserMedalRecordMapper.getUserMedalRecord(loginId);
        //查找最新解锁的勋章
        final DdbUserMedalRecord RecUserMedal=ddbUserMedalRecordMapper.getRecMedalRecord(loginId);
        for (DdbUserMedalDictionary ddbUserMedalDictionary : ddbUserMedalDictionarys) {
            final MyMedal medal = new MyMedal();
            BeanUtils.copyProperties(ddbUserMedalDictionary, medal);
            medal.setMedalId(ddbUserMedalDictionary.getId());
            for (DdbUserMedalRecord ddbUserMedalRecord : Usermedals) {
                if (!ddbUserMedalDictionary.getId().equals(ddbUserMedalRecord.getFkMedalDicId())) {
                    continue;
                }
                if (RecUserMedal != null) {
                    if (ddbUserMedalRecord.getFkMedalDicId().equals(RecUserMedal.getFkMedalDicId())) {
                        medal.setRecObtain(1);
                    }
                }
                BeanUtils.copyProperties(ddbUserMedalRecord, medal);
            }
            medals.add(medal);
        }
        // 根据用户id获取用户头像
        final List<String> loginIds = new ArrayList<>();
        loginIds.add(loginId);
        try {
            final List<UserPhoto> listPhotos = CommUtil.listPhotos(loginIds);
            if (listPhotos.size() != 0) {
                userMedals.setPhotoUrl(listPhotos.get(0).getPhoto());
            }
        } catch (SdkException e) {
            e.printStackTrace();
        }
        // 获取该用户总勋章数量
        final int sum = ddbUserMedalRecordMapper.getMedalSum(loginId);
        userMedals.setMedalSum(sum);
        userMedals.setMyMedals(medals);
        return userMedals;
    }

    /**
     * 勋章佩戴，拆下
     */
    @Override
    public boolean updateMedalWear(UserSession userSession, MyMedal myMedal) {
        final String loginId = userSession.getLoginId();
        if (loginId == null) {
            return false;
        }
        final String type = myMedal.getAction();
        switch (type) {
        case Constants.MEDALWEAR:
            // 定位有没有穿戴的
            final DdbUserMedalRecord ddbUserMedalRecod = ddbUserMedalRecordMapper.getMedalWear(loginId);
            if (ddbUserMedalRecod != null) {
                // 改为已获得状态
                ddbUserMedalRecod.setMedalState(1);
                ddbUserMedalRecordMapper.updateMedal(ddbUserMedalRecod);
            }
            ddbUserMedalRecordMapper.updateMedalWear(loginId, myMedal.getMedalId());
            break;
        case Constants.MEDALOFF:
            ddbUserMedalRecordMapper.RemoveMedal(loginId, myMedal.getMedalId());
            break;
        default:
            break;
        }
        return true;
    }
    /**
     * 保存勋章
     */
    @Override
    public void saveMedal(String loginId, String type, int number) {
        final DdbUserMedalDictionary byMedalType = ddbUserMedalDictionaryMapper.getByMedalType(type);
        final String id = byMedalType.getId();
        final DdbUserMedalRecord byLoginIdMedalId = ddbUserMedalRecordMapper.getByLoginIdMedalId(loginId, id);
        if (byLoginIdMedalId == null) {
            final DdbUserMedalRecord ddbUserMedalRecord = new DdbUserMedalRecord();
            ddbUserMedalRecord.setCreateTime(CommUtil.parseTimeToString(new Date()));
            ddbUserMedalRecord.setFkLoginId(loginId);
            ddbUserMedalRecord.setFkMedalDicId(id);
            // 勋章数量
            ddbUserMedalRecord.setMedalNum(1);
            // 勋章状态 1已获得 2佩戴中
            ddbUserMedalRecord.setMedalState(1);
            // 佩戴状态 0 未佩戴过 1 佩戴过
            ddbUserMedalRecord.setWearState(0);
            ddbUserMedalRecord.setId(CommUtil.genRecordKey());
            ddbUserMedalRecordMapper.save(ddbUserMedalRecord);
        } else {
            int medalNum = byLoginIdMedalId.getMedalNum();
            byLoginIdMedalId.setMedalNum(medalNum + number);
            ddbUserMedalRecordMapper.updateMedalNum(byLoginIdMedalId);
        }
    }
}
