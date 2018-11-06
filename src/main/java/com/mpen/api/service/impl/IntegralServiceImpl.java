package com.mpen.api.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mp.shared.common.Page;
import com.mpen.api.bean.IntegralRecord;
import com.mpen.api.bean.UserIntegralSet;
import com.mpen.api.bean.UserPhoto;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbObtainIntegralInstruction;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbPeLabel;
import com.mpen.api.domain.DdbUserClass;
import com.mpen.api.domain.DdbUserIntegral;
import com.mpen.api.domain.DdbUserIntegralRecord;
import com.mpen.api.domain.DdbUserPraiseRelationship;
import com.mpen.api.domain.DdbUserRelationship;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.DdbObtainIntegralInstructionMapper;
import com.mpen.api.mapper.DdbUserIntegralRecordMapper;
import com.mpen.api.mapper.DdbUserPraiseRelationshipMapper;
import com.mpen.api.mapper.DdbUserRelationshipMapper;
import com.mpen.api.mapper.PeCustomMapper;
import com.mpen.api.mapper.PeLabelMapper;
import com.mpen.api.mapper.PrPenCustomMapper;
import com.mpen.api.mapper.RecordUserBookMapper;
import com.mpen.api.service.DdbUserClassService;
import com.mpen.api.service.DdbUserMessageService;
import com.mpen.api.service.IntegralService;
import com.mpen.api.util.CommUtil;

/**
 * 积分服务类 涉及：App2.0相关
 */
@Service
public class IntegralServiceImpl implements IntegralService {
    @Autowired
    private DdbUserIntegralRecordMapper ddbUserIntegralRecordMapper;
    @Autowired
    private PeCustomMapper peCustomMapper;
    @Autowired
    private DdbUserPraiseRelationshipMapper ddbUserPraiseRelationshipMapper;
    @Autowired
    private DdbUserRelationshipMapper ddbUserRelationshipMapper;
    @Autowired
    private RecordUserBookMapper recordUserBookMapper;
    @Autowired
    private PeLabelMapper peLabelMapper;
    @Autowired
    private DdbObtainIntegralInstructionMapper ddbObtainIntegralInstructionMapper;
    @Autowired
    private DdbUserMessageService ddbUserMessageService;
    @Autowired
    private DdbUserClassService ddbUserClassService;
    @Autowired
    private PrPenCustomMapper prPenCustomMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegralServiceImpl.class);

    /**
     * 获取用户积分列表
     * 
     * @return
     */
    @Override
    public Page<UserIntegralSet> getIntegralRecord(UserSession userSession, IntegralRecord integralRecord) {
        final String loginId = userSession.getLoginId();
        if (loginId == null) {
            return null;
        }
        final Page<UserIntegralSet> page = new Page<UserIntegralSet>();
        final List<UserIntegralSet> userIntegralSets = new ArrayList<>();
        Map<String, List<IntegralRecord>> groupIntegralRecordMapOne = null;
        // 查找该用户总记录数
        final int count = ddbUserIntegralRecordMapper.getCount(loginId);
        if (count == 0) {
            return page;
        }
        // 查找该用户积分记录
        final List<DdbUserIntegralRecord> userIntegralRecords = ddbUserIntegralRecordMapper.getAll(loginId,
                integralRecord.getPageIndex(), integralRecord.getPageSize());
        final List<IntegralRecord> integralRecords = new ArrayList<>();
        for (DdbUserIntegralRecord ddbUserIntegralRecord : userIntegralRecords) {
            final IntegralRecord userIntegralRecord = new IntegralRecord();
            BeanUtils.copyProperties(ddbUserIntegralRecord, userIntegralRecord);
            // 时间
            final Date createTime = ddbUserIntegralRecord.getCreateTime();
            // 时间精确到天(yyyy-MM-dd)
            final String date = CommUtil.parseTimeFormattoDayDate(createTime);
            // 时间精确到月(yyyy-MM)
            final String month = CommUtil.parseTimeFormattoMonthDate(createTime);
            userIntegralRecord.setMonthDate(month);
            userIntegralRecord.setDate(date);
            integralRecords.add(userIntegralRecord);
        }
        // 根据月份进行分组
        groupIntegralRecordMapOne = integralRecords.stream()
                .collect(Collectors.groupingBy(IntegralRecord::getMonthDate));
        // 对分组之后数据在进行封装
        groupIntegralRecordMapOne.forEach((key, value) -> {
            final UserIntegralSet userIntegralSet = new UserIntegralSet();
            userIntegralSet.setIntegralRecords(value);
            userIntegralSet.setMonthDate(key);
            final String[] split = key.split("-");
            // 该月份的总积分
            final int integralSum = ddbUserIntegralRecordMapper.getMonthIntegralSum(loginId, split[1], split[0]);
            userIntegralSet.setIntegralSum(integralSum);
            userIntegralSets.add(userIntegralSet);
        });
        // 对集合进行降序排序
        Collections.sort(userIntegralSets, new Comparator<UserIntegralSet>() {
            @Override
            public int compare(UserIntegralSet o1, UserIntegralSet o2) {
                if (o1.getMonthDate() != null && o2.getMonthDate() != null) {
                    return o2.getMonthDate().compareTo(o1.getMonthDate());
                } else if (o1.getMonthDate() != null) {
                    return 1;
                } else if (o2.getMonthDate() != null) {
                    return -1;
                } else { // both null
                    return 0;
                }
            }
        });
        page.setItems(userIntegralSets);
        page.setTotalCount(count);
        return page;
    }
   
    /**
     * 获取总积分排行榜
     */
    @Override
    public Page<UserIntegralSet> getRankingList(UserSession userSession, IntegralRecord integralRecord) {
        final String loginId = userSession.getLoginId();
        if (loginId == null) {
            return null;
        }
        final Page<UserIntegralSet> page = new Page<UserIntegralSet>();
        final List<UserIntegralSet> userIntegralSets = new ArrayList<>();
        final Map<String, String> day = CommUtil.getDay();
        final String todayDate = day.get("todayDate");
        final String beforeDate = day.get("beforeDate");
        List<DdbUserIntegral> ddbUserIntegrals = null;
        final List<DdbUserRelationship> ddbUserRelationships = ddbUserRelationshipMapper.getFriends(loginId);
        int integralNumber = 0;
        switch (integralRecord.getAction()) {
        // 总排行榜
        case Constants.GET_RANKING_LIST:
            // 查询总排行榜数
            integralNumber = ddbUserIntegralRecordMapper.getNumberSum(beforeDate, todayDate);
            // 查询昨天22点之后、今天22点之前的数据
            ddbUserIntegrals = ddbUserIntegralRecordMapper.getAllUserRanking(beforeDate, todayDate,
                    integralRecord.getPageIndex(), integralRecord.getPageSize());
            // 积分榜为空，查询用户表
            if (ddbUserIntegrals == null || ddbUserIntegrals.size() <= 0) {
                final List<DdbPeCustom> lists = peCustomMapper.getPeCustom(integralRecord.getPageIndex(),
                        integralRecord.getPageSize());
                for (DdbPeCustom ddbPeCustom : lists) {
                    final DdbUserIntegral ddbUserIntegral = new DdbUserIntegral();
                    ddbUserIntegral.setFkLoginId(ddbPeCustom.getLoginId());
                    ddbUserIntegrals.add(ddbUserIntegral);
                }
            }
            break;
        // 好友排行榜
        case Constants.GET_FRIENDS_LIST:
            // 查询查询昨天22点之后、今天22点之前总分前
            final List<String> loginIds = new ArrayList<>();
            loginIds.add(loginId);
            for (DdbUserRelationship ddbUserRelationship : ddbUserRelationships) {
                loginIds.add(ddbUserRelationship.getFriendLoginId());
            }
            ddbUserIntegrals = ddbUserIntegralRecordMapper.getFriendRanking(loginIds, beforeDate, todayDate,
                    integralRecord.getPageIndex(), integralRecord.getPageSize());
            // 查询好友排行榜数
            integralNumber = ddbUserIntegralRecordMapper.getFriendCount(loginIds, beforeDate, todayDate);
            // 积分表为空，查询好友表
            if (ddbUserIntegrals == null || ddbUserIntegrals.size() <= 0) {
                final List<DdbUserRelationship> friendsPage = ddbUserRelationshipMapper.getFriendsPage(loginId,
                        integralRecord.getPageIndex(), integralRecord.getPageSize());
                for (DdbUserRelationship ddbUserRelationship : friendsPage) {
                    final DdbUserIntegral ddbUserIntegral = new DdbUserIntegral();
                    ddbUserIntegral.setFkLoginId(ddbUserRelationship.getFriendLoginId());
                    ddbUserIntegrals.add(ddbUserIntegral);
                }
            }
            break;
        default:
            break;
        }

        final List<String> loginIds = new ArrayList<>();
        for (DdbUserIntegral ddbUserIntegral : ddbUserIntegrals) {
            loginIds.add(ddbUserIntegral.getFkLoginId());
        }
        Map<String, String> userPhotos = this.getUserPhotos(loginIds);
        for (DdbUserIntegral ddbUserIntegral : ddbUserIntegrals) {
            final UserIntegralSet userIntegralSet = new UserIntegralSet();
           
            // 根据当天的前100用户的点赞数量，以及是否被该用户点赞过 点赞状态
            final int count = ddbUserPraiseRelationshipMapper.getPraiseCount(ddbUserIntegral.getFkLoginId(), beforeDate,
                    todayDate);
            userIntegralSet.setPraiseNum(count);
            // 检查该用户当天是否点赞过
            final int state = ddbUserPraiseRelationshipMapper.getPraiseCountByCurrent(userSession.getLoginId(),
                    ddbUserIntegral.getFkLoginId(), beforeDate, todayDate);
            if (state != 0) {
                // 被点赞过
                userIntegralSet.setState(1);
            }
            // 本人备注
            if (userSession.getLoginId().equals(ddbUserIntegral.getFkLoginId())) {
                final DdbPeCustom ddbPeCustom = peCustomMapper.getByLoginId(ddbUserIntegral.getFkLoginId());
                if (ddbPeCustom.getNickName() == null && ddbUserIntegral.getFkLoginId().length() == 11) {
                    ddbUserIntegral.setAliasUser("user_" + ddbUserIntegral.getFkLoginId().substring(7, 11));
                } else {
                    ddbUserIntegral.setAliasUser(ddbPeCustom.getNickName());
                }
            }
            // 好友备注
            for (DdbUserRelationship ddbUserRelationship : ddbUserRelationships) {
                if (ddbUserIntegral.getFkLoginId().equals(ddbUserRelationship.getFriendLoginId())) {
                    ddbUserIntegral.setAliasUser(ddbUserRelationship.getAliasUser());
                }
            }
            // 排名
            userIntegralSet.setRank(ddbUserIntegral.getRank());
            userIntegralSet.setAliasUser(ddbUserIntegral.getAliasUser());
            // 积分总分
            userIntegralSet.setIntegralSum(ddbUserIntegral.getScore());
            // 用户姓名
            final String fkIoginId = ddbUserIntegral.getFkLoginId();
            // 用户头像
            final String photo = userPhotos.get(fkIoginId);
            userIntegralSet.setPhotoUrl(photo);
            userIntegralSet.setLoginId(fkIoginId);
            // 用户昵称
            final DdbPeCustom ddbPeCustom = peCustomMapper.getByLoginId(fkIoginId);
            if (ddbPeCustom != null) {
                final DdbPeLabel ddbPeLabel = peLabelMapper.getById(ddbPeCustom.getFkLabelId());
                if (ddbPeLabel != null) {
                    userIntegralSet.setClassName(ddbPeLabel.getName());
                }

                userIntegralSet.setCover(ddbPeCustom.getCover());
                // 昵称
                userIntegralSet.setNickName(this.getDdbPeCustom(fkIoginId).getNickName());
            }
            userIntegralSets.add(userIntegralSet);
        }
        page.setItems(userIntegralSets);
        page.setTotalCount(integralNumber);
        page.setPageNo(integralRecord.getPageNo());
        return page;
    }
    
    /**
     * 获取用户头像
     */
    private Map<String, String> getUserPhotos(List<String> loginIds){
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
        return photoMap;
        
    }
    
    /**
     * 当前用户积分情况
     */
    @Override
    public List<UserIntegralSet> getUserRanking(UserSession userSession, String type) {
        final List<UserIntegralSet> lists = new ArrayList<UserIntegralSet>();
        final Map<String, String> day = CommUtil.getDay();
        final String todayDate = day.get("todayDate");
        final String beforeDate = day.get("beforeDate");
        DdbUserIntegral ddbUserIntegral = null;
        int count = 0;
        final UserIntegralSet userIntegralSet = new UserIntegralSet();
        switch (type) {
        case Constants.TOTAL_LIST:
            // 当前用户积分情况
            ddbUserIntegral = ddbUserIntegralRecordMapper.getUserIntegral(userSession.getLoginId(), beforeDate,
                    todayDate);
            // 查询出总人数
            count = ddbUserIntegralRecordMapper.getNumberSum(beforeDate, todayDate);
            // 再获取n积分排名
            final DdbUserIntegral newDdbUserIntegral = ddbUserIntegralRecordMapper.getUserNewIntegral(
                    userSession.getLoginId(), beforeDate, todayDate, userIntegralSet.getAddIntegral());
            if (newDdbUserIntegral != null && ddbUserIntegral != null) {
                final int beyond = newDdbUserIntegral.getRank() - ddbUserIntegral.getRank();
                userIntegralSet.setBeyondRank(beyond);
            }
            break;
        case Constants.FRIENDS_LIST:
            final List<DdbUserRelationship> ddbUserRelationships = ddbUserRelationshipMapper.getFriends(userSession.getLoginId());
            // 查询查询昨天22点之后、今天22点之前总分前
            final List<String> loginIds = new ArrayList<>();
            loginIds.add(userSession.getLoginId());
            for (DdbUserRelationship ddbUserRelationship : ddbUserRelationships) {
                loginIds.add(ddbUserRelationship.getFriendLoginId());
            }
            // 当前用户在好友中排行
            ddbUserIntegral = ddbUserIntegralRecordMapper.getInFriendIntegral(userSession.getLoginId(), loginIds,
                    beforeDate, todayDate);
            // 查询出好友总人数
            count = loginIds.size();
            break;
        default:
            break;
        }
        if (ddbUserIntegral != null) {
            // 超越比例
            final double proportion = (double) (count - ddbUserIntegral.getRank()) / count;
            userIntegralSet.setProportion(proportion);
            // 点赞数量
            userIntegralSet.setPraiseNum(ddbUserIntegral.getPraiseNum());
            // 排名
            userIntegralSet.setRank(ddbUserIntegral.getRank());
            // 积分总分
            userIntegralSet.setIntegralSum(ddbUserIntegral.getScore());
        }
        // 查询该用户为自己点赞过，设置状态，
        final int state = ddbUserPraiseRelationshipMapper.getPraisesState(userSession.getLoginId(),
                userSession.getLoginId(), beforeDate, todayDate);
        if (state != 0) {
            // 被点赞过
            userIntegralSet.setState(1);
        }
        // 用户姓名
        final String fkIoginId = userSession.getLoginId();
        final List<String> loginIds = new ArrayList<>();
        loginIds.add(fkIoginId);
        userIntegralSet.setPhotoUrl(this.getUserPhotos(loginIds).get(fkIoginId));
        userIntegralSet.setLoginId(fkIoginId);
        DdbPeCustom ddbPeCustom = this.getDdbPeCustom(fkIoginId);
        // 昵称
        userIntegralSet.setNickName(ddbPeCustom.getNickName());
        // 获取排行榜首用户信息
        final UserIntegralSet topListInfo = getTopListInfo(userSession, type, todayDate, beforeDate);
        lists.add(userIntegralSet);
        lists.add(topListInfo);
        return lists;

    }

    /**
     * 获取排行榜首用户信息
     */
    private UserIntegralSet getTopListInfo(UserSession userSession, String type, String todayDate, String beforeDate) {
        final UserIntegralSet userIntegralSet = new UserIntegralSet();
        DdbUserIntegral ddbUserIntegral = null;
        switch (type) {
        case Constants.TOTAL_LIST:
            ddbUserIntegral = ddbUserIntegralRecordMapper.getAllRankTop(beforeDate, todayDate);
            if (ddbUserIntegral == null) {
                final List<DdbPeCustom> lists = peCustomMapper.getPeCustom(0, 1);
                ddbUserIntegral = new DdbUserIntegral();
                ddbUserIntegral.setFkLoginId(lists.get(0).getLoginId());
            }
            break;
        case Constants.FRIENDS_LIST:
            ddbUserIntegral = ddbUserIntegralRecordMapper.getFriendRankTop(userSession.getLoginId(), beforeDate,
                    todayDate);
            if (ddbUserIntegral == null) {
                final List<DdbUserRelationship> friendsPage = ddbUserRelationshipMapper
                        .getFriendsPage(userSession.getLoginId(), 0, 1);
                ddbUserIntegral = new DdbUserIntegral();
                if (friendsPage.size() <= 0 || friendsPage == null) {
                    ddbUserIntegral.setFkLoginId(userSession.getLoginId());
                } else {
                    ddbUserIntegral.setFkLoginId(friendsPage.get(0).getFriendLoginId());
                }

            }
            break;
        default:
            break;
        }
        final String fkLoginId = ddbUserIntegral.getFkLoginId();
        // 根据loginId获取用户头像和用户封面，昵称
        final List<String> loginIds = new ArrayList<>();
        loginIds.add(ddbUserIntegral.getFkLoginId());
        userIntegralSet.setPhotoUrl(this.getUserPhotos(loginIds).get(ddbUserIntegral.getFkLoginId()));
        DdbPeCustom ddbPeCustom = this.getDdbPeCustom(fkLoginId);
        // 昵称
        userIntegralSet.setNickName(ddbPeCustom.getNickName());
        userIntegralSet.setCover(ddbPeCustom.getCover());
        return userIntegralSet;
    }

    /**
     * 点赞更新
     */
    @Override
    public boolean updatePraiseNum(UserIntegralSet userIntegralSet) {
        final DdbUserPraiseRelationship ddbUserPraiseRelationship = new DdbUserPraiseRelationship();
        // 查询当天该用户是否点赞过该用户
        // 拼接时间
        final Map<String, String> day = CommUtil.getDay();
        String todayDate = day.get("todayDate");
        String beforeDate = day.get("beforeDate");
        // 检查该用户当天是否点赞过
        final int praiseCountByCurrent = ddbUserPraiseRelationshipMapper.getPraiseCountByCurrent(
                userIntegralSet.getPraiseLoginId(), userIntegralSet.getByPraiseLoginId(), beforeDate, todayDate);
        if (praiseCountByCurrent >= 1) {
            return false;
        }

        ddbUserPraiseRelationship.setId(CommUtil.genRecordKey());
        ddbUserPraiseRelationship.setByPraiseLoginId(userIntegralSet.getByPraiseLoginId());
        ddbUserPraiseRelationship.setPraiseLoginId(userIntegralSet.getPraiseLoginId());
        ddbUserPraiseRelationship.setCreateTime(new Date());
        ddbUserPraiseRelationshipMapper.save(ddbUserPraiseRelationship);
        // 点赞消息推送
        Constants.CACHE_THREAD_POOL.execute(() -> {
            this.greatePush(userIntegralSet);
        });
        return true;
    }

    /**
     * 点赞消息推送
     */
    private void greatePush(UserIntegralSet userIntegralSet) {
        final String praiseLoginId = userIntegralSet.getPraiseLoginId();
        DdbPeCustom ddbPeCustom = this.getDdbPeCustom(praiseLoginId);
        switch (userIntegralSet.getType()) {
        case Constants.TOTAL_LIST:
            ddbUserMessageService.saveMessage(userIntegralSet.getPraiseLoginId(), Constants.FABULOUS_ME,
                    ddbPeCustom.getNickName() + "，在积分总榜赞了你", Constants.StudentMsgPushType.INTEGRALSUM.toString());
            break;
        case Constants.FRIENDS_LIST:
            ddbUserMessageService.saveMessage(userIntegralSet.getPraiseLoginId(), Constants.FABULOUS_ME,
                    ddbPeCustom.getNickName() + "，在积分榜赞了你", Constants.StudentMsgPushType.INTEGRAL.toString());
            break;
        default:
            break;
        }
    }

    /**
     * 个人近期7天的得分
     */
    @Override
    public UserIntegralSet getRecentIntegral(UserSession userSession) {
        final UserIntegralSet userIntegralSet = new UserIntegralSet();
        int IntegralScoreSum = 0;
        final List<IntegralRecord> integralRecords = new ArrayList<>();

        final Map<String, String> day = CommUtil.getDay();
        String todayDate = day.get("todayDate");
        String beforeDate = day.get("beforeDate");
        // 查询近7天的积分
        for (int i = 1; i <= 7; i++) {
            // 总分
            final int integral = ddbUserIntegralRecordMapper.getIntegralSum(userSession.getLoginId(), todayDate,
                    beforeDate);
            final IntegralRecord integralRecord = new IntegralRecord();
            integralRecord.setDate(todayDate);
            integralRecord.setIntegral(integral);
            IntegralScoreSum += integral;
            // todayDate-1天 beforeDate-1天
            todayDate = CommUtil.subDay(todayDate);
            beforeDate = CommUtil.subDay(beforeDate);
            integralRecords.add(integralRecord);
        }
        userIntegralSet.setIntegralRecords(integralRecords);
        userIntegralSet.setIntegralSum(IntegralScoreSum);
        // 用户姓名
        final String fkIoginId = userSession.getLoginId();
        final List<String> loginIds = new ArrayList<>();
        loginIds.add(fkIoginId);
        try {
            final List<UserPhoto> listPhotos = CommUtil.listPhotos(loginIds);
            userIntegralSet.setPhotoUrl(listPhotos.get(0).getPhoto());
        } catch (SdkException e) {
            e.printStackTrace();
        }
        userIntegralSet.setLoginId(fkIoginId);
        DdbPeCustom ddbPeCustom = this.getDdbPeCustom(fkIoginId);
        // 昵称
        userIntegralSet.setNickName(ddbPeCustom.getNickName());
        userIntegralSet.setCover(ddbPeCustom.getCover());
        return userIntegralSet;
    }

    /**
     * 获取用户昵称
     */
    private DdbPeCustom getDdbPeCustom(String loginId) {
        // 用户昵称
        final DdbPeCustom ddbPeCustom = peCustomMapper.getByLoginId(loginId);
        if (ddbPeCustom.getNickName() == null && loginId.length() == 11) {
            ddbPeCustom.setNickName("user_" + loginId.substring(7, 11));
        }
        return ddbPeCustom;
    }

    /**
     * 如何获取积分说明
     */
    @Override
    public JSONArray getIntegralInstruction(UserSession user) {
        final List<DdbObtainIntegralInstruction> list = ddbObtainIntegralInstructionMapper.findAll();
        JSONArray jsonArray = null;
        if (list != null && list.size() > 0) {
            jsonArray = new JSONArray();
            int count = 0;
            for (DdbObtainIntegralInstruction instruction : list) {
                // 加载一级菜单
                if (StringUtils.isEmpty(instruction.getParentId())) {
                    count++;
                    final JSONObject jsonObject = new JSONObject();
                    jsonObject.put("title", instruction.getName());
                    final JSONArray array = new JSONArray();
                    jsonObject.put("category", getIntegralJson(list, instruction.getId(), array, count, user));
                    jsonArray.add(jsonObject);
                }
            }
        }
        return jsonArray;
    }

    /**
     * 加载一级子菜单
     * json：从ddb_obtain_integral_instruction表中查出相关数据，进行解析，返回给前端
     * 该解析出的json格式：
     *  {
     * "title": "日常赚取积分方式",
     *"category": [
     *   {
     *     "isClick": true,
     *     "name": "知识学习",
     *     "includes": [
     *       {
     *         "scoreMethod": "完成6次获取一个积分",
     *         "title": "课本点读",
     *         "desc": "每日最高有效上线5分"
     *       },
     *       {
     *         "scoreMethod": "完成2次获取一个积分",
     *         "title": "课本点读",
     *         "desc": "每日最高有效上线10分"
     *       }
     *     ],
     *     "sketch": "使用点读笔学习书本"
     *   },
     *   {
     *     "isClick": false,
     *     "name": "作业练习",
     *     "includes": [
     *       {
     *         "scoreMethod": "完成6次获取一个积分",
     *         "title": "练习",
     *         "desc": "每日最高有效上线5分"
     *       }
     *     ],
     *     "sketch": "使用点读笔学习书本"
     *   },
     *   {
     *     "isClick": false,
     *     "name": "其他",
     *     "includes": [
     *       {
     *         "scoreMethod": "完成6次获取一个积分",
     *         "title": "书本练习",
     *         "desc": "每日最高有效上线5分"
     *       }
     *     ],
     *     "sketch": "其他"
     *   }
     * ]
     *  },
     * { 
     * "title": "做任务赚积分",
     * "category": [
     *   {
     *     "isClick": false,
     *     "score": 800,
     *     "name": "完善班级信息",
     *     "includes": [],
     *     "sketch": "完善后即可和老师、同学互动",
     *     "isDone": false
     *   },
     *   {
     *     "isClick": false,
     *     "score": 800,
     *     "name": "首次连接智能笔",
     *     "includes": [],
     *     "sketch": "连接后，可拥有更多丰富功能",
     *     "isDone": true
     *   }
     * ]
     *}
     * 
     */
    private JSONArray getIntegralJson(List<DdbObtainIntegralInstruction> list, String id, JSONArray array, int count,
            UserSession user) {
        for (DdbObtainIntegralInstruction instruction : list) {
            if (StringUtils.isNotBlank(instruction.getParentId()) && instruction.getParentId().equals(id)) {
                final JSONObject jsonObject = new JSONObject();
                if (instruction.getTypeId().equals("2")) {
                    count++;
                    jsonObject.put("name", instruction.getName());
                    jsonObject.put("sketch", instruction.getFistDesc());
                    jsonObject.put("isClick", count == 2 ? true : false);
                    if (instruction.getScore() != 0) {
                        if (instruction.getName().equals(Constants.INTEGRAL_CLASS)) {
                            final DdbUserClass ddbUserClass = ddbUserClassService.getClassByLoginId(user.getLoginId());
                            jsonObject.put("isDone", ddbUserClass == null ? true : false);
                        } else if (instruction.getName().equals(Constants.INTEGRAL_LINK)) {
                            final int penNum = prPenCustomMapper.getBindPenNum(user.getPeCustom().getId());
                            jsonObject.put("isDone", penNum > 0 ? false : true);
                        }
                        jsonObject.put("score", instruction.getScore());
                    }
                    final JSONArray jsonArray = new JSONArray();
                    jsonObject.put("includes", getIntegralJson(list, instruction.getId(), jsonArray, count, user));
                    array.add(jsonObject);
                } else if (instruction.getTypeId().equals("3")) {
                    jsonObject.put("title", instruction.getName());
                    jsonObject.put("scoreMethod", instruction.getFistDesc());
                    jsonObject.put("desc", instruction.getSubDesc());
                    array.add(jsonObject);
                }
            }
        }
        return array;
    }
}
