package com.mpen.api.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mp.shared.common.CodeInfo;
import com.mp.shared.common.Exam;
import com.mp.shared.common.Page;
import com.mp.shared.utils.FUtils;
import com.mpen.api.bean.ActivityStudyDetail;
import com.mpen.api.bean.Book;
import com.mpen.api.bean.ClassAfterExamDetail;
import com.mpen.api.bean.ClassAfterLearnDetail;
import com.mpen.api.bean.ClassAfterLearnDetail.BookWork;
import com.mpen.api.bean.ClassAssignments;
import com.mpen.api.bean.ClassInfo;
import com.mpen.api.bean.ClassLearnContent;
import com.mpen.api.bean.EnglishBookContent;
import com.mpen.api.bean.EnglishBookContent.Activity;
import com.mpen.api.bean.ExamPaper;
import com.mpen.api.bean.ExamResult;
import com.mpen.api.bean.HomeWorkDetail;
import com.mpen.api.bean.HomeWorkInfo;
import com.mpen.api.bean.HomeWorkInfo.ActivityVideo;
import com.mpen.api.bean.HomeWorks;
import com.mpen.api.bean.HomeworkAfterClass;
import com.mpen.api.bean.HomeworkResourceUrl;
import com.mpen.api.bean.NoClassBookSource;
import com.mpen.api.bean.OralExamination;
import com.mpen.api.bean.OralTestInfo.OralTestPaper;
import com.mpen.api.bean.PreviewBeforeClass;
import com.mpen.api.bean.PreviewContent;
import com.mpen.api.bean.Sentence;
import com.mpen.api.bean.Student;
import com.mpen.api.bean.TextBookLearning;
import com.mpen.api.bean.Unit;
import com.mpen.api.bean.UserPhoto;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Constants.StudentMsgPushType;
import com.mpen.api.domain.DdbOraltestBook;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbResourceBook;
import com.mpen.api.domain.DdbResourceCode;
import com.mpen.api.domain.DdbResourceVideo;
import com.mpen.api.domain.DdbUserClass;
import com.mpen.api.domain.DdbUserClassRela;
import com.mpen.api.domain.DdbUserComment;
import com.mpen.api.domain.DdbUserHomework;
import com.mpen.api.domain.DdbUserHomeworkState;
import com.mpen.api.domain.DdbUserMedalDictionary;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.DdbOralTestBookMapper;
import com.mpen.api.mapper.DdbUserClassMapper;
import com.mpen.api.mapper.DdbUserClassRelaMapper;
import com.mpen.api.mapper.DdbUserCommentsMapper;
import com.mpen.api.mapper.DdbUserHomeworkMapper;
import com.mpen.api.mapper.DdbUserHomeworkStateMapper;
import com.mpen.api.mapper.DdbUserMedalDictionaryMapper;
import com.mpen.api.mapper.OralTestDetailMapper;
import com.mpen.api.mapper.RecordUserBookMapper;
import com.mpen.api.mapper.ResourceBookCatalogMapper;
import com.mpen.api.mapper.ResourceBookMapper;
import com.mpen.api.mapper.ResourceVideoMapper;
import com.mpen.api.service.DdbUserDynamicRecordService;
import com.mpen.api.service.DdbUserMessageService;
import com.mpen.api.service.DecodeService;
import com.mpen.api.service.HomeWorkService;
import com.mpen.api.service.MedalService;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.service.ResourceBookService;
import com.mpen.api.service.ResourceCodeService;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.FileUtils;
import com.mpen.api.util.JPushUtil;

/**
 * 学生作业相关Service服务
 * 涉及：教师端以及app2.0班级作业，个人作业情况相关的服务
 */
@Service
public class HomeWorkServiceImpl implements HomeWorkService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeWorkServiceImpl.class);

    @Autowired
    private DdbUserClassMapper ddbUserClassMapper;
    @Autowired
    private DdbUserHomeworkMapper ddbUserHomeworkMapper;
    @Autowired
    private DdbUserMessageService ddbUserMessageService;
    @Autowired
    private DdbUserClassRelaMapper ddbUserClassRelaMapper;
    @Autowired
    private MemCacheService memCacheService;
    @Autowired
    private ResourceBookCatalogMapper resourceBookCatalogMapper;
    @Autowired
    private ResourceBookMapper resourceBookMapper;
    @Autowired
    private ResourceBookService resourceBookService;
    @Autowired
    private DdbUserHomeworkStateMapper ddbUserHomeworkStateMapper;
    @Autowired
    private RecordUserBookMapper recordUserBookMapper;
    @Autowired
    private ResourceCodeService resourceCodeService;
    @Autowired
    private DdbUserCommentsMapper ddbUserCommentsMapper;
    @Autowired
    DdbOralTestBookMapper ddbOralTestBookMapper;
    @Autowired
    private DecodeService decodeService;
    @Autowired
    private OralTestDetailMapper testDetailMapper;
    @Autowired
    private MedalService medalService;
    @Autowired
    private DdbUserMedalDictionaryMapper medalDictionaryMapper;
    @Autowired
    private DdbUserDynamicRecordService ddbUserDynamicRecordService;
    @Autowired
    private ResourceVideoMapper videoMapper;
    /**
     * 保存课后作业或者课前导学
     */
    @Override
    public boolean savePreviewOrHomework(UserSession userSession, PreviewBeforeClass previewBeforeClass,
            HomeworkAfterClass homeworkAfterClass) {
        final String loginId = userSession.getLoginId();
        if (StringUtils.isBlank(loginId)) {
            return false;
        }
        DdbUserHomework ddbUserHomework = new DdbUserHomework();
        ddbUserHomework.setId(CommUtil.genRecordKey());
        ddbUserHomework.setCreateDate(new Date());
        ddbUserHomework.setFkLoginId(loginId);
        // 获取班级名称
        String className = previewBeforeClass != null ? previewBeforeClass.getClassName()
                : homeworkAfterClass.getClassName();
        // 根据loginld和班级模糊查询出classIds,如果className不为空则是给指定班级布置作业，否则则是给所有班级布置作业
        List<String> classIds = ddbUserClassMapper.getClassId(loginId,
                (className.equals("") == true) ? null : className);
        if (classIds == null || classIds.size() <= 0) {
            return false;
        }
        // 如果不是空，布置的是课前导学，否则是课后作业
        if (previewBeforeClass != null) {
            // 完善课前导学
            ddbUserHomework.setEndDate(CommUtil.parseTimeFormattoDate(previewBeforeClass.getEndDate()));
            ddbUserHomework.setType(Constants.PREVIEW);
            // content内容 bean转换装换为json格式的类型
            final PreviewContent content = previewBeforeClass.getPreviewContent();
            final String contentJson = new Gson().toJson(content);
            ddbUserHomework.setContent(contentJson);
            // 存储课前导学
            return saveHomeWork(previewBeforeClass, homeworkAfterClass, classIds, ddbUserHomework);
        } else {
            // 完善课后作业
            ddbUserHomework.setEndDate(CommUtil.parseTimeFormattoDate(homeworkAfterClass.getEndDate()));
            ddbUserHomework.setRemark(homeworkAfterClass.getRemark());
            ddbUserHomework.setType(Constants.HOMEWORK_AFTER);
            // content内容 bean转换装换为json格式的类型
            final ClassLearnContent content = homeworkAfterClass.getContent();
            final String contentJson = new Gson().toJson(content);
            ddbUserHomework.setContent(contentJson);
            // 存储课后作业
            return saveHomeWork(previewBeforeClass, homeworkAfterClass, classIds, ddbUserHomework);

        }
    }

    /*
     * 保存作业或课前导学
     */
    private boolean saveHomeWork(PreviewBeforeClass previewBeforeClass, HomeworkAfterClass homeworkAfterClass,
            List<String> classIds, DdbUserHomework ddbUserHomework) {
        for (String classId : classIds) {
            // 完整课后作业
            ddbUserHomework.setId(CommUtil.genRecordKey());
            ddbUserHomework.setFkClassId(classId);
            // 存储作业
            ddbUserHomeworkMapper.save(ddbUserHomework);
            Constants.CACHE_THREAD_POOL.execute(() -> {
                sendHomeWorkMsg(previewBeforeClass, homeworkAfterClass, classId, ddbUserHomework);
            });
        }
        return true;
    }

    /**
     * 布置作业消息推送
     */
    private void sendHomeWorkMsg(PreviewBeforeClass previewBeforeClass, HomeworkAfterClass homeworkAfterClass,
            String classId, DdbUserHomework ddbUserHomework) {
        List<String> loginIds = ddbUserClassRelaMapper.getByClassId(classId);
        for (String loginId : loginIds) {
            final Map<String, String> msg = new HashMap<>();
            msg.put("type", StudentMsgPushType.HOMEWORKDETAIL.toString());
            msg.put("homeWorkId", ddbUserHomework.getId());
            msg.put("homeWorkType", String.valueOf(ddbUserHomework.getType()));
            final String title = homeworkAfterClass == null ? previewBeforeClass.getPreviewContent().getName()
                    : homeworkAfterClass.getContent().getTitle();
            ddbUserMessageService.pushMessage(loginId, "你有新的作业," + title + "快去看看", msg);
        }
    }
    /**
     * 获取班级作业列表
     */
    @Override
    public List<ClassInfo> getAssignments(UserSession userSession, ClassInfo classInfo) {
        final String loginId = userSession.getLoginId();
        if (StringUtils.isBlank(loginId)) {
            return null;
        }
        // 条件查询班级ID 年级可能为空
        final List<String> classIds = ddbUserClassMapper.getClassId(loginId, classInfo.getClassName());
        final List<ClassAssignments> classAssignmentses = new ArrayList<>();
        final List<ClassInfo> classInfos = new ArrayList<>();
        Map<String, List<ClassAssignments>> groupClassAssignmentsrMapOne = null;
        for (String classId : classIds) {
            final DdbUserClass ddbUserClass = ddbUserClassMapper.getByClassId(classId);
            // 根据截至时间，班级id,loginId查询 已经截至，没有截至
            // 根据班级id,loginld查询出作业
            final List<DdbUserHomework> ddbUserHomeworks = ddbUserHomeworkMapper.getHomeWorks(loginId, classId);
            for (DdbUserHomework ddbUserHomework : ddbUserHomeworks) {
                final ClassAssignments classAssignments = new ClassAssignments();
                classAssignments.setId(ddbUserHomework.getId());
                // 班级
                classAssignments.setClassName(ddbUserClass.getClassName());
                // 创建时间
                classAssignments.setCreateDate(CommUtil.parseTimeToString(ddbUserHomework.getCreateDate()));
                // 已提交人数
                final int submitNum = ddbUserHomeworkStateMapper.getSubmitNum(ddbUserHomework.getId());
                classAssignments.setSubmit(submitNum);
                // 未提交人数
                final int noSubmitNum = ddbUserHomeworkStateMapper.getNoSubmitNum(ddbUserHomework.getId());
                classAssignments.setNotSubmit(noSubmitNum);
                classAssignments.setSubmit(submitNum);
                // 截至时间
                classAssignments.setEndDate(CommUtil.parseTimeToString(ddbUserHomework.getEndDate()));
                // 截止时间 精确到天
                classAssignments
                        .setCreateDayDate(new java.sql.Date(ddbUserHomework.getCreateDate().getTime()).toString());
                // 作业类型
                classAssignments.setType(ddbUserHomework.getType());
                final String contentStr = ddbUserHomework.getContent();
                switch (ddbUserHomework.getType()) {
                case Constants.HOMEWORK_AFTER:
                    // 课后作业
                    final ClassLearnContent content = new Gson().fromJson(contentStr, ClassLearnContent.class);
                    classAssignments.setTitle(content.getTitle());
                    break;
                case Constants.PREVIEW:
                    // 课前导学
                    final PreviewContent previewContent = new Gson().fromJson(contentStr, PreviewContent.class);
                    classAssignments.setTitle(previewContent.getName());
                    classAssignments.setSubmits(previewContent.getSubmits());
                default:
                    break;
                }
                final Date endDate = ddbUserHomework.getEndDate();
                final long time = endDate.getTime();
                final long currentTime = new Date().getTime();
                // 是否超过截止时间，根据type类型来进行判断,是否返回给APP
                if (classInfo.getState() == 0) {
                    classAssignmentses.add(classAssignments);
                } else if (classInfo.getState() == 1 && time > currentTime) {
                    // 截至时间内
                    classAssignmentses.add(classAssignments);
                } else if (classInfo.getState() == 2 && time < currentTime) {
                    // 截至时间外
                    classAssignmentses.add(classAssignments);
                }
                // 根据创建日期进行分组
                groupClassAssignmentsrMapOne = classAssignmentses.stream()
                        .collect(Collectors.groupingBy(ClassAssignments::getCreateDayDate));
            }
        }
        if (groupClassAssignmentsrMapOne == null) {
            return null;
        }
        // 对分组之后数据在进行封装
        groupClassAssignmentsrMapOne.forEach((key, value) -> {
            final ClassInfo classinfo = new ClassInfo();
            // 对当日的作业精确到时分秒进行排序
            Collections.sort(value, new Comparator<ClassAssignments>() {
                @Override
                public int compare(ClassAssignments o1, ClassAssignments o2) {
                    if (o1.getCreateDate() != null && o2.getCreateDate() != null) {
                        if (CommUtil.parseTimeFormattoDayDate(o1.getCreateDate())
                                .after(CommUtil.parseTimeFormattoDayDate(o2.getCreateDate()))) {
                            return -1;
                        }
                    }
                    return 1;
                }
            });
            classinfo.setHomeworks(value);
            classinfo.setDate(key);
            classInfos.add(classinfo);
        });
        // 对集合进行降序排序
        Collections.sort(classInfos, new Comparator<ClassInfo>() {
            @Override
            public int compare(ClassInfo o1, ClassInfo o2) {
                if (o1.getDate() != null && o2.getDate() != null) {
                    if (CommUtil.parseTimeFormattoDayDate(o1.getDate())
                            .after(CommUtil.parseTimeFormattoDayDate(o2.getDate()))) {
                        return -1;
                    }
                }
                return 1;
            }
        });

        return classInfos;
    }

    /**
     * 班级、学生个人作业详情
     */
    @Override
    public HomeWorkDetail getHomeWorkList(ClassInfo classInfo, Student student, int type) {
        List<String> loginIds = new ArrayList<>();
        final String homeWorkId;
        if (classInfo != null) {
            // 作业ID
            homeWorkId = classInfo.getHomeWorkId();
            // 根据作业Id查找已提交作业的学生loginId
            loginIds = ddbUserHomeworkStateMapper.getLoginId(homeWorkId);
        } else {
            loginIds.add(student.getLoginId());
            homeWorkId = student.getHomeworkId();
        }
        HomeWorkDetail homeWorkDetail = new HomeWorkDetail();
        // 得到班级作业
        final DdbUserHomework ddbUserHomework = ddbUserHomeworkMapper.getById(homeWorkId);
        if (ddbUserHomework == null) {
            return null;
        }
        // 得到班级作业内容
        final String contentStr = ddbUserHomework.getContent();
        // 课后作业内容bean
        final ClassLearnContent contentAfter = JSONObject.parseObject(contentStr, ClassLearnContent.class);
        // 作业内容标题
        homeWorkDetail.setTitle(contentAfter.getTitle());
        final List<TextBookLearning> leanings = contentAfter.getTextBookLearnings();
        final List<OralExamination> examinations = contentAfter.getOralExaminations();
        if (leanings != null && leanings.size() > 0) {
            // 班级课本学习作业情况
            final List<TextBookLearning> learnDetails = classLearnDetail(ddbUserHomework, leanings, loginIds);
            switch (type) {
            case Constants.COLLECTIVE:
                // 每个单元平均点读总次数
                int classAvgCountTimesSum = 0;
                // 每个单元平均点读总分数
                float classAvgScoreSum = 0;
                // 每个单元
                for (TextBookLearning textBookLeaning : learnDetails) {
                    int classUnitAvgCountTimes = textBookLeaning.getClassUnitAvgCountTimes();
                    classAvgCountTimesSum = classUnitAvgCountTimes;
                    float classUnitAvgScore = textBookLeaning.getClassUnitAvgScore();
                    classAvgScoreSum += classUnitAvgScore;
                }
                final ClassLearnContent content = new ClassLearnContent();
                content.setTextBookLearnings(learnDetails);
                // 班级平均点读次数
                content.setClassAvgCountTimes(classAvgCountTimesSum / (learnDetails.size() != 0 ? learnDetails.size() : 1));
                // 班级平均分数
                content.setClassAvgScore(classAvgScoreSum / (learnDetails.size() != 0 ? learnDetails.size() : 1));
                homeWorkDetail.setClassTextBookLeanings(content);
                break;
            case Constants.PERSONAL:
                final ClassLearnContent perContent = new ClassLearnContent();
                perContent.setTextBookLearnings(learnDetails);
                homeWorkDetail.setClassTextBookLeanings(perContent);
                break;
            default:
                break;
            }

        }
        if (examinations != null && examinations.size() > 0) {
            // 口语考试卷详情
            List<ClassAfterExamDetail> examDetails = getExamDetails(examinations, loginIds, type);
            homeWorkDetail.setExamResult(examDetails);
        }

        return homeWorkDetail;

    }
    /**
     * 班级、个人课后作业课本点读，口语评测学习情况
     */
    private List<TextBookLearning> classLearnDetail(DdbUserHomework ddbUserHomework, List<TextBookLearning> leanings,
            List<String> loginIds) {
        // 作业布置时间
        final String startDate = CommUtil.parseTimeToString(ddbUserHomework.getCreateDate());
        // 作业截止时间
        final String endDate = CommUtil.parseTimeToString(ddbUserHomework.getEndDate());
        for (TextBookLearning textBookLeaning : leanings) {
            ArrayList<com.mpen.api.bean.EnglishBookContent.Activity> activities = textBookLeaning.getActivities();
            switch (textBookLeaning.getType()) {
            case Constants.TEXTBOOKREAD:
                // 每个单元activity数量
                int LearnactivitySum = activities.size();
                // 每个activity平均点读累加
                int pointSum = 0;
                for (com.mpen.api.bean.EnglishBookContent.Activity activity : activities) {
                    final List<Integer> times = new ArrayList<>();
                    int timesSum = 0;
                    if (loginIds.size() != 0) {
                        for (String loginId : loginIds) {
                            final String activityId = activity.id;
                            // 得到id和bookId和loginId创建时间，提交时间，定位每个activity,得到每个人点读次数
                            final int countTimes = recordUserBookMapper.getCountTimes(loginId, activityId, startDate,
                                    endDate, CommUtil.getRecordTableName(loginId));
                            times.add(countTimes);
                            timesSum += countTimes;
                        }
                        // 每个activity学生的平均点读，最高点读，最低点读
                        activity.maxCountTimes = Collections.max(times);
                        activity.minCountTimes = Collections.min(times);
                        activity.avgCountTimes = timesSum / loginIds.size();
                        activity.countTimes = times.size() == 0 ? 0 : times.get(0);
                        pointSum += activity.avgCountTimes;
                    }
                }
                // 班级课本unit点读平均点读次数
                textBookLeaning.setClassUnitAvgCountTimes(LearnactivitySum != 0 ? (pointSum / LearnactivitySum) : 0);
                break;
            case Constants.ORALTEST:
                // 班级口语考试
                classOraltestCalCulate(activities, textBookLeaning, loginIds, startDate, endDate);
                break;
            default:
                break;
            }
        }

        return leanings;
    }
    
    /**
     * 班级口语考试
     */
    private void classOraltestCalCulate(ArrayList<Activity> activities, TextBookLearning textBookLeaning, List<String> loginIds,
            String startDate, String endDate) {
        // 每个单元有多少个acitivity
        int size = activities.size();
        // 每个acitivity平均分的累加
        double activityScoresSum = 0;
        for (com.mpen.api.bean.EnglishBookContent.Activity activity : activities) {
            String id = activity.id;
            List<com.mpen.api.bean.EnglishBookContent.Sentence> sentences = new ArrayList<>();
            List<DdbResourceCode> list = resourceCodeService.getByCatalogId(textBookLeaning.getBookId(), id);
            if (list != null && list.size() > 0) {
                // activity中句子数量
                int sentenceSum = list.size();
                // activity中句子平均分累加总和
                double sentenceScoreAvgSum = 0;
                // activity中句子最大分数累加总和
                double sentenceScoreMaxSum = 0;
                // activity中句子最小分数累加总和
                double sentenceScoreMinSum = 0;
                // 得到每个句子
                for (DdbResourceCode code : list) {
                    com.mpen.api.bean.EnglishBookContent.Sentence sentence = new com.mpen.api.bean.EnglishBookContent.Sentence();
                    sentence.title = code.getText().replace("#1", "'");
                    if (loginIds.size() != 0) {
                        // 计算每个句子的平均分，最高分，最低分，优秀，良好，一般，不及格
                        // 全部学生的总分
                        double sentenceScoreSum = 0;
                        // 每个句子学生分数的集合
                        List<Double> scores = new ArrayList<>();
                        for (String loginId : loginIds) {
                            // 得到学生每个句子的最大分数
                            final Double sentenceScore = recordUserBookMapper.getMaxScore(loginId, activity.id,
                                    sentence.title, startDate, endDate, CommUtil.getRecordTableName(loginId));
                            sentenceScoreSum += sentenceScore == null ? 0 : sentenceScore;
                            scores.add(sentenceScore == null ? 0 : sentenceScore);
                        }
                        // 计算平均分，最高分，最低分
                        if (scores.size() != 0) {
                            sentence.maxScore = Collections.max(scores);
                            sentenceScoreMaxSum += sentence.maxScore;
                            sentence.minScore = Collections.min(scores);
                            sentenceScoreMinSum += sentence.minScore;
                            sentence.avgScore = sentenceScoreSum / loginIds.size();
                            sentenceScoreAvgSum += sentence.avgScore;
                            sentence.score = scores.get(0);
                            // 计算优秀，良好，一般，不及格四种情况
                            int excellent = 0;
                            int favorable = 0;
                            int general = 0;
                            int flunk = 0;
                            for (Double score : scores) {
                                if (score < 60) {
                                    ++flunk;
                                } else if (score < 80) {
                                    ++general;
                                } else if (score < 90) {
                                    ++favorable;
                                } else {
                                    ++excellent;
                                }
                            }
                            // 优秀比例
                            sentence.excellentProportion = 1.0 * excellent / loginIds.size();
                            // 良好比例
                            sentence.favorableProportion = 1.0 * favorable / loginIds.size();
                            // 一般比例
                            sentence.generalProportion = 1.0 * general / loginIds.size();
                            // 不及格比例
                            sentence.flunkProportion = 1.0 * flunk / loginIds.size();
                        }

                    }
                    sentences.add(sentence);
                }
                activity.sentences = sentences;
                // activity中的全部句子的最大分数总和/句子数量
                activity.maxScore = sentenceSum == 0 ? 0 : sentenceScoreMaxSum / sentenceSum;
                // activity中的全部句子的最小分数总和/句子数量
                activity.minScore = sentenceSum == 0 ? 0 : sentenceScoreMinSum / sentenceSum;
                // activity中的全部句子的平均分数总和/句子数量
                activity.avgScore = sentenceSum == 0 ? 0 : sentenceScoreAvgSum / sentenceSum;
                activityScoresSum += activity.avgScore;
            }
        }
        // 班级口语评测unit评测平均分数
        textBookLeaning.setClassUnitAvgScore(size != 0 ? ((float) (activityScoresSum / size)) : 0);
    }

    /**
     * 班级、学生口语考试试卷详情
     */
    private List<ClassAfterExamDetail> getExamDetails(List<OralExamination> examinations, List<String> loginIds,
            int type) {
        // 口语考试卷书籍集合
        List<ClassAfterExamDetail> examDetails = new ArrayList<>();
        // 获取试卷详情
        for (OralExamination oralExamination : examinations) {
            // 口语考试卷书籍
            ClassAfterExamDetail examDetail = new ClassAfterExamDetail();
            final String bookId = oralExamination.getBookId();
            examDetail.setBookId(bookId);
            examDetail.setName(oralExamination.getName());
            // 口语考试卷卷子集合
            final List<OralTestPaper> examPaper = oralExamination.getExamPaper();
            List<ExamResult> examResults = new ArrayList<>();
            for (OralTestPaper oralTestPaper : examPaper) {
                // 教师获取学生的口语考试卷评测详情
                ExamResult oralTestInfo = null;
                try {
                    switch (type) {
                    case Constants.COLLECTIVE:
                        oralTestInfo = resourceBookService.getOralTestInfo(bookId, oralTestPaper.num, new DdbPeCustom(),
                                Constants.AIENGINE_YZS, loginIds, type);
                        break;
                    case Constants.PERSONAL:
                        DdbPeCustom ddbPeCustom = new DdbPeCustom();
                        ddbPeCustom.setLoginId(loginIds.get(0));
                        oralTestInfo = resourceBookService.getOralTestInfo(bookId, oralTestPaper.num, ddbPeCustom,
                                Constants.AIENGINE_YZS, null, type);
                        break;
                    default:
                        break;
                    }

                    examResults.add(oralTestInfo);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            examDetail.setOralExams(examResults);
            examDetails.add(examDetail);
        }
        return examDetails;
    }

    /**
     * 获取评语
     */
    @Override
    public List<DdbUserComment> getAllComments() {

        return ddbUserCommentsMapper.getAllComments();

    }

    /**
     * 课前导学查看已批阅，未批阅，未提交人数 课后作业查看已提交未提交人数
     */
    @Override
    public ClassAssignments getNumber(String homeworkId, UserSession userSession) {
        final ClassAssignments classAssignments = new ClassAssignments();
        // 已提交人数
        final int submitNum = ddbUserHomeworkStateMapper.getSubmitNum(homeworkId);
        classAssignments.setSubmit(submitNum);
        // 已经批阅人数
        final int reviewNum = ddbUserHomeworkStateMapper.getHasReviewNum(homeworkId);
        // 未批阅人数
        final int noReviewNum = ddbUserHomeworkStateMapper.getHasNoReviewNum(homeworkId);
        // 未提交人数
        final int noSubmitNum = ddbUserHomeworkStateMapper.getNoSubmitNum(homeworkId);
        classAssignments.setReview(reviewNum);
        classAssignments.setNoReview(noReviewNum);
        classAssignments.setNotSubmit(noSubmitNum);
        return classAssignments;
    }

    /**
     * 获取已批阅学生，未批阅学生，未提交,已提交学生信息
     */
    @Override
    public List<Student> getReviewMember(String id, String reviewType, UserSession userSession) {
        List<Student> students = new ArrayList<>();
        switch (reviewType) {
        // 未批阅,已批阅
        case Constants.READOVER:
        case Constants.NOTREAD:
            // 作业
            final List<DdbUserHomeworkState> ddbUserHomeworkStates = ddbUserHomeworkStateMapper.getByBookIdAndType(id,
                    reviewType);
            for (DdbUserHomeworkState ddbUserHomeworkState : ddbUserHomeworkStates) {
                final Student student = new Student();
                student.setHomeworkId(id);
                student.setLoginId(ddbUserHomeworkState.getFkLoginId());
                // 截至时间
                student.setSubmitDate(CommUtil.parseTimeToString(ddbUserHomeworkState.getUploadTime()));
                // 查询出学生id找班级关联表
                final DdbUserClassRela ddbUserClassRela = ddbUserClassRelaMapper
                        .getByLoginId(ddbUserHomeworkState.getFkLoginId());
                student.setName(ddbUserClassRela.getTrueName());
                final List<String> loginIds = new ArrayList<>();
                loginIds.add(ddbUserClassRela.getFkLoginId());
                try {
                    final List<UserPhoto> listPhotos = CommUtil.listPhotos(loginIds);
                    final String photo = listPhotos.get(0).getPhoto();
                    student.setPhotoUrl(photo);
                } catch (SdkException e) {
                    e.printStackTrace();
                }
                student.setFlowerNum(ddbUserHomeworkState.getRewardNum());
                if (ddbUserHomeworkState.getResourceUrl() != null) {
                    final List<HomeworkResourceUrl> fromJson = new Gson().fromJson(ddbUserHomeworkState.getResourceUrl(),
                            List.class);
                    student.setResourceUrls(fromJson);
                }
                if (ddbUserHomeworkState.getAudioRemarkUrls() != null) {
                    student.setAudioUrls(new Gson().fromJson(ddbUserHomeworkState.getAudioRemarkUrls(), List.class));
                }
                student.setRemark(ddbUserHomeworkState.getRemark());
                students.add(student);
            }
            break;
        case Constants.NOSUBMIT:
            students = ddbUserHomeworkMapper.getStudentList(id);
            this.getStudents(students, id);
            break;
        case Constants.SUBMITTED:
            students = ddbUserHomeworkMapper.getSubStudentList(id);

            this.getStudents(students, id);
            break;
        default:
            break;
        }
        return students;
    }

    /**
     * 获取学生头像
     */
    private void getStudents(List<Student> students, String id) {
        for (Student student : students) {
            student.setHomeworkId(id);
            if (student.getSubmitDate() != null) {
                String substring = student.getSubmitDate().substring(0, student.getSubmitDate().lastIndexOf("."));
                student.setSubmitDate(substring);
            }
            final List<String> loginIds = new ArrayList<>();
            loginIds.add(student.getLoginId());
            try {
                final List<UserPhoto> listPhotos = CommUtil.listPhotos(loginIds);
                final String photo = listPhotos.get(0).getPhoto();
                student.setPhotoUrl(photo);
            } catch (SdkException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取全部英语书籍
     */
    @Override
    public List<Book> getAllBook() {
        String key = CommUtil.getCacheKey(Constants.CACHE_ENGLISHBOOK_KEY);
        try {
            List<Book> englishBooks = memCacheService.get(key);
            if (englishBooks == null) {
                englishBooks = new ArrayList<>();
                final List<String> bookIds = resourceBookCatalogMapper.getAllBookId();
                for (String bookId : bookIds) {
                    final DdbResourceBook ddbResourceBook = resourceBookMapper.getId(bookId);
                    final Book englishBook = new Book();
                    englishBook.setId(bookId);
                    englishBook.setName(ddbResourceBook.getName());
                    englishBooks.add(englishBook);
                }
                memCacheService.set(key, englishBooks, Constants.DEFAULT_CACHE_EXPIRATION);
            }
            return englishBooks;
        } catch (CacheException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取书籍的内容
     */
    @Override
    public List<HomeworkAfterClass> getBookContentDetail(Book book, UserSession userSession) {
        final String loginId = userSession.getLoginId();
        if (StringUtils.isBlank(loginId)) {
            return null;
        }
        final List<HomeworkAfterClass> homeworkAfterClasses = new ArrayList<>();
        final String[] types = { Constants.CACHE_STUDY_PREFIX, Constants.CACHE_SPOKEN_PREFIX };
        for (String type : types) {
            final HomeworkAfterClass homeworkAfterClass = new HomeworkAfterClass();
            final List<EnglishBookContent> englishBookContents = new ArrayList<>();
            try {
                final List<Unit> unitsList = resourceBookService.getBookContent(book.getId(), type);
                for (Unit unit : unitsList) {
                    final EnglishBookContent englishBookContent = new EnglishBookContent();
                    englishBookContent.unit = unit.getUnit();
                    englishBookContent.model = unit.getModel();
                    englishBookContent.name = unit.getName();
                    englishBookContent.activities = new ArrayList<>();
                    final List<com.mpen.api.bean.Activity> activities = unit.getActivities();
                    if (activities == null || activities.size() == 0) {
                        continue;
                    }
                    for (com.mpen.api.bean.Activity activity : activities) {
                        final com.mpen.api.bean.EnglishBookContent.Activity activity2 = new com.mpen.api.bean.EnglishBookContent.Activity();
                        activity2.id = activity.getId();
                        activity2.sort = activity.getSort();
                        activity2.name = activity.getName();
                        englishBookContent.activities.add(activity2);
                    }
                    englishBookContents.add(englishBookContent);
                }
                homeworkAfterClass.setEnglishBookContents(englishBookContents);
                homeworkAfterClasses.add(homeworkAfterClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return homeworkAfterClasses;
    }

    /**
     * 获取口语考试卷书籍
     */
    @Override
    public ArrayList<DdbOraltestBook> getAllOralBook(UserSession userSession) {
        final String loginId = userSession.getLoginId();
        if (StringUtils.isBlank(loginId)) {
            return null;
        }
        return ddbOralTestBookMapper.getDdbOralTestBooks();
    }

    /**
     * 导学详情与课后作业详情
     */
    @Override
    public Object getReviewDetails(ClassAssignments classAssignments) {
        final DdbUserHomework ddbUserHomework = ddbUserHomeworkMapper.getHomeWork(classAssignments);
        if (ddbUserHomework == null) {
            return null;
        }
        final String contentJson = ddbUserHomework.getContent();
        switch (classAssignments.getType()) {
        case Constants.HOMEWORK_AFTER:
            final HomeWorkDetail classHomeworkContent = new HomeWorkDetail();
            // 得到课前作业内容，查找具体句子
            final ClassLearnContent content = Constants.GSON.fromJson(contentJson, ClassLearnContent.class);
            final List<ClassAfterExamDetail> oralExaminations = this.getOralExaminations(content.getOralExaminations());
            final List<TextBookLearning> textBookLeanings = this.getTextBookLeaning(content.getTextBookLearnings());
            classHomeworkContent.setTextBookLeanings(textBookLeanings);
            classHomeworkContent.setExamResult(oralExaminations);
            return classHomeworkContent;
        case Constants.PREVIEW:
            return new Gson().fromJson(contentJson, PreviewContent.class);
        default:
            break;
        }
        return null;
    }

    /**
     * 获取课后学习英语书籍：课本学习，口语评测，跟读对比内容，精确到每个句子
     */
    private List<TextBookLearning> getTextBookLeaning(List<TextBookLearning> textBookLearningList) {
        // 课本练习
        for (TextBookLearning textBookLearning : textBookLearningList) {
            final String bookId = textBookLearning.getBookId();
            final ArrayList<com.mpen.api.bean.EnglishBookContent.Activity> activities = textBookLearning
                    .getActivities();
            for (com.mpen.api.bean.EnglishBookContent.Activity activity : activities) {
                final List<com.mpen.api.bean.EnglishBookContent.Sentence> sentences = new ArrayList<>();
                final List<DdbResourceCode> list = resourceCodeService.getByCatalogId(bookId, activity.id);
                if (list != null && list.size() > 0) {
                    for (DdbResourceCode code : list) {
                        final com.mpen.api.bean.EnglishBookContent.Sentence sentence = new com.mpen.api.bean.EnglishBookContent.Sentence();
                        sentence.title = code.getText().replace("#1", "'");
                        sentences.add(sentence);
                    }
                }
                activity.sentences = sentences;
            }
        }
        return textBookLearningList;
    }

    /**
     * 获取课后学习口语考试卷子内容:精确到每道小题
     */
    private List<ClassAfterExamDetail> getOralExaminations(List<OralExamination> list) {
        // 口语考试
        final List<ClassAfterExamDetail> examDetails = new ArrayList<>();
        for (OralExamination oralExamination : list) {
            final ClassAfterExamDetail examDetail = new ClassAfterExamDetail();
            examDetail.setContent(oralExamination.getName());
            final String bookId = oralExamination.getBookId();
            examDetail.setBookId(bookId);
            final List<OralTestPaper> examPaper = oralExamination.getExamPaper();
            final List<Exam> exams = new ArrayList<>();
            for (OralTestPaper oralTestPaper : examPaper) {
                final int num = oralTestPaper.num;
                // 解析mp包获取口语考试卷内容
                try {
                    final CodeInfo codeInfo = decodeService.getOralTestCodeInfo(bookId, num);
                    if (codeInfo == null) {
                        continue;
                    }
                    final String json = FUtils
                            .fileToString(FileUtils.getFileSaveRealPath(codeInfo.languageInfos[0].getVoice(), false));
                    // 得到口语考试卷内容
                    final Exam exam = Constants.GSON.fromJson(json, Exam.class);
                    exams.add(exam);
                    // TODO 进行返回
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            examDetail.setExams(exams);
            examDetails.add(examDetail);
        }
        return examDetails;
    }

    /**
     * 保存老师评测学生的信息
     */
    @Override
    public boolean saveStudentHomeWorkDetails(UserSession userSession, Student student) {
        // 判断该作业是否已经评论
        final DdbUserHomeworkState ddbUserHomeworkState = ddbUserHomeworkStateMapper.getHomework(student.getLoginId(),
                student.getHomeworkId());
        if (ddbUserHomeworkState == null) {
            return false;
        }
        ddbUserHomeworkState.setRemark(student.getRemark());
        ddbUserHomeworkState.setRewardNum(student.getFlowerNum());
        ddbUserHomeworkState.setUpdateTime(new Date());
        ddbUserHomeworkState.setAudioRemarkUrls(new Gson().toJson(student.getAudioUrls()));
        Constants.CACHE_THREAD_POOL.execute(() -> {
            // 动态和消息
            StudentMedalAndMsg(student);
        });

        ddbUserHomeworkStateMapper.updateStudentHomeWork(ddbUserHomeworkState);
        return true;
    }

    /**
     * 老师评阅完作业，学生获得勋章，动态
     */
    private void StudentMedalAndMsg(Student student) {
        final DdbUserHomework byId = ddbUserHomeworkMapper.getById(student.getHomeworkId());
        if (student.getFlowerNum() != null) {
            final DdbUserMedalDictionary dictionary = medalDictionaryMapper.getByMedalType(Constants.COMPLETE_EXERCISE);
            final String medalDynamic = dictionary.getMedalDynamic();
            // 保存动态
            ddbUserDynamicRecordService.saveDynamic(student.getLoginId(),
                    Constants.StudentMsgPushType.HOMEWORKDETAIL + "," + byId.getType() + "," + byId.getId(),
                    medalDynamic);
            // 保存勋章
            medalService.saveMedal(student.getLoginId(), Constants.COMPLETE_EXERCISE, 1);
        }
        final ClassLearnContent content = new Gson().fromJson(byId.getContent(), ClassLearnContent.class);
        final String title = content.getTitle();
        final Map<String, String> msg = new HashMap<>();
        msg.put("type", StudentMsgPushType.HOMEWORKDETAIL.toString());
        msg.put("homeWorkType", String.valueOf(byId.getType()));
        ddbUserMessageService.pushMessage(student.getLoginId(), "\"" + title + "\"" + "老师已经批阅完成，快去看看", msg);
    }

    /**
     * 一键催作业
     */
    @Override
    public boolean RushJob(ClassAssignments classAssignments) {
        // 获取学生loginId
        final List<String> fkLoginIds = classAssignments.getLoginIds();
        // 获取作业详情
        final DdbUserHomework homeWork = ddbUserHomeworkMapper.getHomeWork(classAssignments);
        if (homeWork == null) {
            return false;
        }
        final Map<String, String> msg = new HashMap<>();
        msg.put("type", StudentMsgPushType.HOMEWORKDETAIL.toString());
        msg.put("homeWorkId", homeWork.getId());
        msg.put("homeWorkType", String.valueOf(homeWork.getType()));
        fkLoginIds.forEach((fkLoginId) -> {
            try {
                // 全部平台推送
                final JPushUtil.JpushParam jpushParam = new JPushUtil.JpushParam(fkLoginId, Constants.HOMEWORK_MSG, msg,
                        JPushUtil.AppType.VIATON, JPushUtil.PlatformType.ALL);
                JPushUtil.sendPushToUser(jpushParam);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    /**
     * 获取某个学生的课前导学作业
     * 
     * @throws Exception
     */
    @Override
    public HomeWorkDetail getSudentHomeworkDetails(Student student) throws Exception {
        final HomeWorks homeWorks = new HomeWorks();
        homeWorks.setId(student.getHomeworkId());
        final HomeWorkDetail homeWorkDetail = new HomeWorkDetail();
        // 查询出作业内容
        final DdbUserHomework ddbUserHomework = ddbUserHomeworkMapper.getById(homeWorks.getId());
        if (ddbUserHomework == null) {
            return null;
        }
        // 查询学生作业完成情况
        final DdbUserHomeworkState work = ddbUserHomeworkStateMapper.getByWorkId(homeWorks.getId(),
                student.getLoginId());
        homeWorkDetail.setContent(JSONObject.parseObject(ddbUserHomework.getContent(), PreviewContent.class));
        if (work != null) {
            homeWorkDetail.setStatus(getStatus(Integer.valueOf(work.getIsCommit()), ddbUserHomework.getEndDate()));
            homeWorkDetail.setComment(work.getRemark());
            final Long uploadTime = work.getUploadTime() != null ? work.getUploadTime().getTime() : null;
            homeWorkDetail.setUploadTime(uploadTime);
            homeWorkDetail.setIsMarking(work.getIsMarking());
            if (StringUtils.isNotBlank(work.getResourceUrl())) {
                homeWorkDetail.setResourceUrl(Constants.GSON.fromJson(work.getResourceUrl(), ArrayList.class));
            }
            if (StringUtils.isNotBlank(work.getAudioRemarkUrls())) {
                final ArrayList<HomeworkResourceUrl> audioUrls = Constants.GSON.fromJson(work.getAudioRemarkUrls(),
                        new TypeToken<ArrayList<HomeworkResourceUrl>>() {
                        }.getType());
                homeWorkDetail.setAudioUrls(audioUrls);
            }
            homeWorkDetail.setRewardNum(work.getRewardNum());
        }
        homeWorkDetail.setFkClassId(ddbUserHomework.getFkClassId());
        homeWorkDetail.setId(ddbUserHomework.getId());
        return homeWorkDetail;
    }

    /**
     * 学生作业状态
     * 
     * @param isCommit
     * @param endDate
     * @return
     */
    private String getStatus(Integer isCommit, Date endDate) {
        // 默认为""
        String status = "";
        // 判断是否完成
        if (isCommit != null && isCommit.intValue() == 1) {
            status = Constants.STATUS_COMPLETE;
        }
        if (StringUtils.isBlank(status)) {
            // 判断是否已截止
            status = new Date().after(endDate) ? Constants.STATUS_CLOSE : Constants.STATUS_DOING;
        }
        return status;
    }

    /**
     * 获取用户分页作业列表
     * 
     * @param user
     * @param homeWorks
     */
    @Override
    public Page<HomeWorkInfo> pageHomeWork(UserSession user, HomeWorks homeWorks) {
        // 获取当前用户班级
        final DdbUserClass userClass = ddbUserClassMapper.getClassByLoginId(user.getLoginId());

        // 判断是否有老师班级并获取符合条件列表
        if (userClass.getType() != Constants.INT_ONE.intValue()) {
            // 返回有老师班级作业列表信息
            return teacherClassPage(homeWorks, userClass.getId(), user.getLoginId());
        }
        // 获取无老师班级课前作业列表信息
        if (homeWorks.getType().intValue() == Constants.PREVIEW) {
            return noTeacherClassPage(homeWorks);
        }
        return new Page<HomeWorkInfo>();
    }

    /**
     * 有老师班级作业分页列表
     * 
     * @param homeWorks
     * @param classId
     * @param loginId
     * @return
     */
    private Page<HomeWorkInfo> teacherClassPage(HomeWorks homeWorks, String classId, String loginId) {
        final Page<HomeWorkInfo> page = new Page<HomeWorkInfo>();
        homeWorks.setFkClassId(classId);
        // 获取总记录数
        final int totalCount = ddbUserHomeworkMapper.getTotalCount(homeWorks);
        if (totalCount == 0) {
            return page;
        }
        // 获取当前页数的作业集合
        final List<DdbUserHomework> userHomeworks = ddbUserHomeworkMapper.listHomeWorks(homeWorks);
        // 获取作业id集合
        final List<String> workIds = new ArrayList<>();
        for (DdbUserHomework homework : userHomeworks) {
            workIds.add(homework.getId());
        }
        // 获取当前用户完成的作业及老师点评
        List<DdbUserHomeworkState> workStates = new ArrayList<>();
        if (workIds != null && workIds.size() > 0) {
            workStates = ddbUserHomeworkStateMapper.listByLoginId(workIds, loginId);
        }
        for (DdbUserHomework homework : userHomeworks) {
            for (DdbUserHomeworkState workState : workStates) {
                if (homework.getId().equals(workState.getFkHomeworkId())) {
                    homework.setIsCommit(Integer.valueOf(workState.getIsCommit()));
                    homework.setUploadTime(workState.getUpdateTime());
                    homework.setComment(workState.getRemark());
                    break;
                }
            }
        }
        // 声明返回的分页集合
        final List<HomeWorkInfo> works = new ArrayList<>();
        // 处理班级作业,并放入works
        for (DdbUserHomework ddbUserHomework : userHomeworks) {
            dealHomeWorkInfo(homeWorks, ddbUserHomework, works);
        }
        page.setItems(works);
        page.setTotalCount(totalCount);
        page.setPageNo(homeWorks.getPageNo());
        return page;
    }
    
    /**
     * 处理班级作业
     * @param homeWorks
     * @param userHomeworks
     * @param works
     */
    private void dealHomeWorkInfo(HomeWorks homeWorks, DdbUserHomework userHomework, List<HomeWorkInfo> works) {
        // 获取作业内容
        final String content = userHomework.getContent();
        // 作业名称
        String name = null;
        // 获取作业内容标签
        ArrayList<String> submits = new ArrayList<>();
        // 判断作业是否是课前预习
        if (homeWorks.getType().intValue() == 2) {
            // 是课前预习 则获取课前预习作业内容及作业名称,标签
            final PreviewContent previewContent = JSONObject.parseObject(content, PreviewContent.class);
            submits = previewContent.getSubmits();
            name = previewContent.getName();
        } else {
            // 是课后作业 则获取课后作业内容及作业名称
            final ClassLearnContent contentAfter = JSONObject.parseObject(content, ClassLearnContent.class);
            name = contentAfter.getTitle();
            // 处理课后作业,遍历课本学习内容,放入标签0:点读,1:口语评测
            final List<TextBookLearning> leanings = contentAfter.getTextBookLearnings();
            if (leanings != null && leanings.size() > 0) {
                for (TextBookLearning bookLeaning : leanings) {
                    if (2 == submits.size()) {
                        break;
                    }
                    final String bookType = Constants.ZERO.equals(bookLeaning.getType()) ? "课本点读" : "口语评测";
                    if (!submits.contains(bookType)) {
                        submits.add(bookType);
                    }
                }
            }
            // 获取并判断作业是否包含听说训练,是则加入标签"听说训练"
            final List<OralExamination> examinations = contentAfter.getOralExaminations();
            if (examinations != null && examinations.size() > 0) {
                submits.add("听说训练");
            }
        }
        // 构造返回对象数据
        final HomeWorkInfo workInfo = new HomeWorkInfo();
        workInfo.setId(userHomework.getId());
        workInfo.setName(name);
        workInfo.setEndDateInMs(userHomework.getEndDate().getTime());
        workInfo.setTimeInMs(userHomework.getCreateDate().getTime());
        final Integer isCommit = userHomework.getIsCommit();
        final String status = getStatus(isCommit, userHomework.getEndDate());
        workInfo.setStatus(status);
        final Long uploadTime = userHomework.getUploadTime() != null ? userHomework.getUploadTime().getTime() : null;
        workInfo.setCompleteDateInMs(uploadTime);
        workInfo.setComment(userHomework.getComment());
        workInfo.setSubmits(submits);
        works.add(workInfo);
    }

    /**
     * 课前预习无老师班级分页
     * @param homeWorks
     * @return
     */
    private Page<HomeWorkInfo> noTeacherClassPage(HomeWorks homeWorks) {
        // 声明返回的分页对象
        final Page<HomeWorkInfo> page = new Page<HomeWorkInfo>();
        // 获取书籍名称
        final String bookAlias = homeWorks.getBookAlias();
        // 获取书籍model
        final String model = homeWorks.getModel();
        // 根据书籍名称获取bookId
        final String bookId = NoClassBookSource.getBookId(bookAlias);
        // 根据书籍Id与model获取对应名师微课与教材动画视屏
        final List<DdbResourceVideo> videos = videoMapper.getNoTeacherClassVideo(model, bookId);
        // 获取获取视屏中的unitName与activityId集合
        final List<String> unitNames = new ArrayList<>();
        final List<String> activityIds = new ArrayList<>();
        for (DdbResourceVideo video : videos) {
            if (!unitNames.contains(video.getUnitName())) {
                unitNames.add(video.getUnitName());
            }
            if (!activityIds.contains(video.getActivityId())) {
                activityIds.add(video.getActivityId());
            }
        }
        // key(单元名称)与value(对应的ActivityId集合)放入map
        final Map<String, List<String>> unitNameActivityIdsMap = new HashMap<>();
        for (String string : unitNames) {
            final List<String> unitNameActivityIds = new ArrayList<>();
            for (DdbResourceVideo video : videos) {
                final String activityId = video.getActivityId();
                if (string.equals(video.getUnitName()) && !unitNameActivityIds.contains(activityId)) {
                    unitNameActivityIds.add(activityId);
                }
            }
            unitNameActivityIdsMap.put(string, unitNameActivityIds);
        }
        // key(ActivityId)与value(对应的视屏集合)放入videoMap
        final Map<String, List<DdbResourceVideo>> videoMap = new HashMap<>();
        for (String activityId : activityIds) {
            final List<DdbResourceVideo> resourceVideos = new ArrayList<>();
            for (DdbResourceVideo video : videos) {
                if (activityId.equals(video.getActivityId())) {
                    resourceVideos.add(video);
                }
            }
            videoMap.put(activityId, resourceVideos);
        }
        // 处理返回的infos
        final List<HomeWorkInfo> infos = new ArrayList<>();
        for (String unitName : unitNames) {
            final HomeWorkInfo workInfo = new HomeWorkInfo();
            workInfo.setName(unitName);
            final List<ActivityVideo> activityVideos = new ArrayList<>();
            final List<String> unitNameActivityIds = unitNameActivityIdsMap.get(unitName);
            for (String unitNameActivityId : unitNameActivityIds) {
                final ActivityVideo activityVideo = new ActivityVideo();
                final List<DdbResourceVideo> list = videoMap.get(unitNameActivityId);
                activityVideo.setItem(list.get(0).getItem());
                activityVideo.setVideos(list);
                activityVideos.add(activityVideo);
            }
            workInfo.setActivityVideos(activityVideos);
            infos.add(workInfo);
        }
        page.setItems(infos);
        page.setPageNo(homeWorks.getPageNo());
        return page;
    }

    /**
     * 获取作业详情
     */
    @Override
    public HomeWorkDetail getHomeWorkDetail(UserSession userSession, HomeWorks homeWorks) throws Exception {
        final HomeWorkDetail homeWorkDetail = new HomeWorkDetail();
        final DdbUserHomework ddbUserHomework = ddbUserHomeworkMapper.getById(homeWorks.getId());
        // 获取当前用户作业状态详情
        final DdbUserHomeworkState work = ddbUserHomeworkStateMapper.getByWorkId(homeWorks.getId(), userSession.getLoginId());
        if (work != null) {
            ddbUserHomework.setIsCommit(Integer.valueOf(work.getIsCommit()));
            ddbUserHomework.setIsMarking(work.getIsMarking());
            ddbUserHomework.setComment(work.getRemark());
            ddbUserHomework.setResourceUrl(work.getResourceUrl());
            ddbUserHomework.setRewardNum(work.getRewardNum());
            ddbUserHomework.setUploadTime(work.getUpdateTime());
        }
        // 获取作业内容
        final String content = ddbUserHomework.getContent();
        // 判断课前导学与课后作业,分别处理作业内容详情
        switch (ddbUserHomework.getType()) {
        case Constants.PREVIEW:
            final PreviewContent previewContent = JSONObject.parseObject(content, PreviewContent.class);
            homeWorkDetail.setContent(previewContent);
            break;
        case Constants.HOMEWORK_AFTER:
            homeWorkAfterDetail(content, ddbUserHomework, userSession.getLoginId(), homeWorkDetail);
            break;
        default:
            break;
        }
        // set作业状态
        homeWorkDetail.setStatus(getStatus(ddbUserHomework.getIsCommit(), ddbUserHomework.getEndDate()));
        // set教师评论
        homeWorkDetail.setComment(ddbUserHomework.getComment());
        // 完成时间
        final Long uploadTime = ddbUserHomework.getUploadTime() != null ? ddbUserHomework.getUploadTime().getTime()
                : null;
        homeWorkDetail.setUploadTime(uploadTime);
        // 批阅状态
        homeWorkDetail.setIsMarking(ddbUserHomework.getIsMarking());
        // 作业资源路径(音频等...)
        @SuppressWarnings("unchecked")
        final List<HomeworkResourceUrl> fromJson = new Gson().fromJson(ddbUserHomework.getResourceUrl(), List.class);
        homeWorkDetail.setResourceUrl(fromJson);
        // 学生上传的资源路径
        final String audioRemarkUrls = work != null ? work.getAudioRemarkUrls() : null;
        if (StringUtils.isNotBlank(audioRemarkUrls)) {
            final ArrayList<HomeworkResourceUrl> audioUrls = Constants.GSON.fromJson(audioRemarkUrls,
                    new TypeToken<ArrayList<HomeworkResourceUrl>>() {
                    }.getType());
            homeWorkDetail.setAudioUrls(audioUrls);
        }
        // 奖励红花个数
        homeWorkDetail.setRewardNum(ddbUserHomework.getRewardNum());
        homeWorkDetail.setFkClassId(ddbUserHomework.getFkClassId());
        homeWorkDetail.setId(ddbUserHomework.getId());
        return homeWorkDetail;
    }

    /**
     * 获取课后作业详情
     * 
     * @param content
     * @param ddbUserHomework
     * @param loginId
     * @param homeWorkDetail
     * @throws Exception
     */
    private void homeWorkAfterDetail(String content, DdbUserHomework ddbUserHomework, String loginId,
            HomeWorkDetail homeWorkDetail) throws Exception {
        final ClassLearnContent contentAfter = JSONObject.parseObject(content, ClassLearnContent.class);
        homeWorkDetail.setTitle(contentAfter.getTitle());

        final List<TextBookLearning> leanings = contentAfter.getTextBookLearnings();
        final List<OralExamination> examinations = contentAfter.getOralExaminations();

        if (leanings != null && leanings.size() > 0) {
            // 获取点读,评测详情
            final List<ClassAfterLearnDetail> learnDetails = listLearnDetail(ddbUserHomework, leanings, loginId);
            homeWorkDetail.setLearnDetails(learnDetails);
        }
        if (examinations != null && examinations.size() > 0) {
            // 口语考试卷书籍集合
            final List<ClassAfterExamDetail> examDetails = new ArrayList<>();
            // 获取试卷详情
            for (OralExamination oralExamination : examinations) {
                // 口语考试卷书籍
                final ClassAfterExamDetail examDetail = new ClassAfterExamDetail();
                final String bookId = oralExamination.getBookId();
                examDetail.setBookId(bookId);
                examDetail.setContent(oralExamination.getName());
                // 口语考试卷卷子集合
                final List<OralTestPaper> examPaper = oralExamination.getExamPaper();
                final List<ExamPaper> examPapers = new ArrayList<>();
                for (OralTestPaper oralTestPaper : examPaper) {
                    final int num = oralTestPaper.num;
                    final ExamPaper paper = new ExamPaper();
                    paper.setBookId(bookId);
                    paper.setPageNum(num);
                    paper.setName(oralTestPaper.name);
                    final String startTime = CommUtil.parseTimeToString(ddbUserHomework.getCreateDate());
                    final String endTime = CommUtil.parseTimeToString(ddbUserHomework.getEndDate());
                    // 获取试卷状态 0 未做   1 已做
                    final int count = testDetailMapper.getHomeWorkOralTestPaperCount(loginId, bookId, num, Constants.AIENGINE_YZS, startTime, endTime);
                    paper.setStatus(count);
                    examPapers.add(paper);
                }
                examDetail.setExamPapers(examPapers);
                examDetails.add(examDetail);
            }
            homeWorkDetail.setExamResult(examDetails);
        }
    }

    /**
     * 获取课后学习内容
     * 
     * @param ddbUserHomework
     * @param leanings
     * @param loginId
     * @return
     */
    private List<ClassAfterLearnDetail> listLearnDetail(DdbUserHomework ddbUserHomework,
            List<TextBookLearning> leanings, String loginId) {
        // 获取作业bookId集合type集合
        final List<String> bookIds = new ArrayList<>();
        final List<String> types = new ArrayList<>();
        for (TextBookLearning bookLeaning : leanings) {
            if (!bookIds.contains(bookLeaning.getBookId())) {
                bookIds.add(bookLeaning.getBookId());
            }
            if (!types.contains(bookLeaning.getType())) {
                types.add(bookLeaning.getType());
            }
        }
        // 获取截止日期
        final String startDate = CommUtil.parseTimeToString(ddbUserHomework.getCreateDate());
        final String endDate = getHomeWorkEndDate(ddbUserHomework);
        // 获取书籍评测详情
        final List<ActivityStudyDetail> spokenDetails = recordUserBookMapper.getSpokenDetailByLoginIdAndBookIds(loginId,
                bookIds, startDate, endDate, CommUtil.getRecordTableName(loginId));
        final Map<String, ActivityStudyDetail> tempSpokenDetailMap = new HashMap<String, ActivityStudyDetail>();
        for (ActivityStudyDetail obj : spokenDetails) {
            tempSpokenDetailMap.put(obj.getText().replace("#1", "'"), obj);
        }
        // 获取书籍点读详情
        final List<ActivityStudyDetail> studyDetails = recordUserBookMapper.getStudyDetailByLoginIdAndBookIds(loginId,
                bookIds, startDate, endDate, CommUtil.getRecordTableName(loginId));
        final Map<String, ActivityStudyDetail> tempStudyDetailMap = new HashMap<String, ActivityStudyDetail>();
        for (ActivityStudyDetail obj : studyDetails) {
            tempStudyDetailMap.put(obj.getFkActivityId(), obj);
        }
        // key(作业类型)与value(课本学习作业TextBookLearning)放入typeBookLeaningMap
        final Map<String, TextBookLearning> typeBookLeaningMap = new HashMap<>();
        for (String type : types) {
            for (TextBookLearning bookLeaning : leanings) {
                typeBookLeaningMap.put(type, bookLeaning);
            }
        }
        // 处理课本学习作业详情
        final List<ClassAfterLearnDetail> learnDetails = new ArrayList<>();
        for (String type : types) {
            final TextBookLearning bookLearning = typeBookLeaningMap.get(type);
            final ClassAfterLearnDetail learnDetail = dealLearnDetail(type, bookLearning, tempSpokenDetailMap, tempStudyDetailMap);
            learnDetails.add(learnDetail);
        }
        return learnDetails;
    }
    
    /**
     * 处理课本学习详情
     * @param type
     * @param bookLearning
     * @param tempSpokenDetailMap
     * @param tempStudyDetailMap
     * @return
     */
    private ClassAfterLearnDetail dealLearnDetail(String type, TextBookLearning bookLearning, 
            Map<String, ActivityStudyDetail> tempSpokenDetailMap, 
            Map<String, ActivityStudyDetail> tempStudyDetailMap) {
        final ClassAfterLearnDetail learnDetail = new ClassAfterLearnDetail();
        // 作业类型
        learnDetail.setType(type);
        // 作业对应书籍名称
        learnDetail.setTextbookName(bookLearning.getBookName());
        // 作业
        final List<BookWork> bookWorks = new ArrayList<>();
        for (com.mpen.api.bean.EnglishBookContent.Activity activity : bookLearning.getActivities()) {
            final BookWork bookWork = new BookWork();
            bookWork.setModel(bookLearning.getModel());
            bookWork.setUnit(bookLearning.getUnit());
            bookWork.setActivityName(activity.sort);
            // 根据type判断点读或口语评测(0:点读,1:口语评测)
            final List<DdbResourceCode> list = resourceCodeService.getByCatalogId(bookLearning.getBookId(),
                    activity.id);
            if (list != null && list.size() > 0) {
                if (Constants.ZERO.equals(type)) {
                    final ActivityStudyDetail activityStudyDetail = tempStudyDetailMap.get(activity.id);
                    bookWork.setNumber(activityStudyDetail != null ? activityStudyDetail.getCountTimes() : Constants.INT_ZERO);
                    final DecimalFormat df = new DecimalFormat("0.0");
                    double minute = activityStudyDetail != null ? activityStudyDetail.getTime() / 60 : 0;
                    bookWork.setMinute(df.format(minute));
                } else {
                    final List<Sentence> textList = new ArrayList<Sentence>();
                    for (DdbResourceCode code : list) {
                        final String title = code.getText().replace("#1", "'");
                        final ActivityStudyDetail activityStudyDetail = tempSpokenDetailMap.get(title);
                        final Sentence sentence = new Sentence();
                        // 获取点读次数
                        sentence.setTitle(title);
                        sentence.setNumber(activityStudyDetail != null ? activityStudyDetail.getCountTimes() : Constants.INT_ZERO);
                        sentence.setScore(activityStudyDetail != null ? activityStudyDetail.getScore() : 0);
                        textList.add(sentence);
                    }
                    bookWork.setSentences(textList);
                }
            }
            bookWorks.add(bookWork);
        }
        learnDetail.setBookWorks(bookWorks);
        return  learnDetail;
    }

    // 获取作业截止日期
    private String getHomeWorkEndDate(DdbUserHomework ddbUserHomework) {
        String endDate = null;
        switch (getStatus(ddbUserHomework.getIsCommit(), ddbUserHomework.getEndDate())) {
        case Constants.STATUS_COMPLETE:
            endDate = CommUtil.parseTimeToString(ddbUserHomework.getUploadTime());
            break;
        case Constants.STATUS_CLOSE:
            endDate = CommUtil.parseTimeToString(ddbUserHomework.getEndDate());
            break;
        case Constants.STATUS_DOING:
        default:
            endDate = CommUtil.parseTimeToString(new Date());
            break;
        }
        return endDate;
    }

    /**
     * 获取书籍视频推送列表
     */
    @Override
    public List<String> getPushBookVideo() {
        final Map<String, String> vedioBookMap = Constants.vedioBookMap;
        final List<String> lists = new ArrayList<>();
        // 判断vedioBookMap是否为空,为nul返回
        if (vedioBookMap.isEmpty()) {
            return lists;
        }
        Set<String> keySet = vedioBookMap.keySet();
        for (String key : keySet) {
            lists.add(key);
        }
        Comparator<String> comparator = new Comparator<String>() {
            public int compare(String o1, String o2) {
                return CommUtil.CNNumerCompare(o1, o2);
            }
        };
        lists.sort(comparator);
        return lists;
    }
}
