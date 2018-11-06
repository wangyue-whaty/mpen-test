package com.mpen.api.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mpen.api.bean.ClassInfo;
import com.mpen.api.bean.Student;
import com.mpen.api.bean.UserPhoto;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.InviteCodeTools;
import com.mpen.api.domain.DdbUserClass;
import com.mpen.api.domain.DdbUserClassRela;
import com.mpen.api.domain.SsoUser;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.DdbUserClassMapper;
import com.mpen.api.mapper.DdbUserClassRelaMapper;
import com.mpen.api.mapper.DdbUserIntegralRecordMapper;
import com.mpen.api.mapper.DdbUserPraiseRelationshipMapper;
import com.mpen.api.mapper.RecordExamDetailMapper;
import com.mpen.api.service.DdbUserClassRelaService;
import com.mpen.api.service.DdbUserClassService;
import com.mpen.api.util.CommUtil;

/**
 * 班级管理服务层
 * 涉及：教师端以及app2.0涉及班级管理接口
 * 
 * @author hzy
 * @since 2018-07-03
 */
@Component
public class DdbUserClassServiceImpl implements DdbUserClassService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DdbUserClassServiceImpl.class);
    @Autowired
    private DdbUserClassMapper userClassMapper;
    @Autowired
    private DdbUserClassRelaMapper ddbUserClassRelaMapper;
    @Autowired
    private DdbUserClassMapper ddbUserClassMapper;
    @Autowired
    private RecordExamDetailMapper recordExamDetailMapper;
    @Autowired
    private DdbUserIntegralRecordMapper ddbUserIntegralRecordMapper;
    @Autowired
    private DdbUserPraiseRelationshipMapper ddbUserPraiseRelationshipMapper;
    @Autowired
    private DdbUserClassRelaService userClassRelaService;

    /**
     * 教师新增班级
     * 
     * @throws CacheException
     */
    @Override
    public boolean saveClassInfo(UserSession userSession, ClassInfo classInfo) {
        // 校验用户Id
        final String loginId = userSession.getLoginId();
        if (StringUtils.isBlank(loginId)) {
            return false;
        }
        // 校验该班级是否该用户已经注册存在
        final List<String> classId = ddbUserClassMapper.getClassId(loginId, classInfo.getClassName());
        if (classId.size() != 0) {
            return false;
        }
        final DdbUserClass ddbUserClass = new DdbUserClass();
        ddbUserClass.setId(CommUtil.genRecordKey());
        // 根据用户ID查询sso_user表查找用户名字，学校，年级，
        final SsoUser user = userSession.getSsoUser();
        ddbUserClass.setFkLoginId(loginId);
        ddbUserClass.setEnglishTeacher(user.getTrueName());
        // TODO App端没有相应界面字段，暂时不设，后续添加
        ddbUserClass.setSchool(null);
        ddbUserClass.setClassName(classInfo.getClassName());
        final Date curDate = new Date();
        ddbUserClass.setCreateTime(curDate);
        ddbUserClass.setUpdateTime(curDate);
        // 班级编号
        final String classNumberStr = userClassMapper.getClassNumber();
        final int classNumber = StringUtils.isNoneBlank(classNumberStr) ? (Integer.parseInt(classNumberStr) + 1) : 1;
        ddbUserClass.setClassNumber(String.format("%06d", classNumber));
        // 邀请码
        final String inviteCode = InviteCodeTools.getInviteCode();
        ddbUserClass.setInvitationCode(inviteCode);
        // 保存用户数据
        userClassMapper.saveClass(ddbUserClass);
        return true;
    }

    /**
     * 教师更改班级信息
     */
    @Override
    public boolean updateClassInfo(ClassInfo classInfo) {
        userClassMapper.updateClassInfo(classInfo.getId(), classInfo.getClassName());
        return true;
    }

    /**
     * 查询班级列表
     */
    @Override
    public List<ClassInfo> getClassLists(UserSession userSession) {
        final List<ClassInfo> classInfos = new ArrayList<>();
        final List<DdbUserClass> ddbUserClasses = userClassMapper.getByLoginId(userSession.getLoginId());
        if (ddbUserClasses == null) {
            return null;
        }
        for (DdbUserClass ddbUserClass : ddbUserClasses) {
            final ClassInfo classInfo = new ClassInfo();
            classInfo.setId(ddbUserClass.getId());
            // 班级编号
            classInfo.setClassNumber(ddbUserClass.getClassNumber());
            // 班级邀请码
            classInfo.setInvitationCode(ddbUserClass.getInvitationCode());
            classInfo.setClassName(ddbUserClass.getClassName());
            final int studentNums = ddbUserClassRelaMapper.getStudentNums(ddbUserClass.getId());
            classInfo.setStudentNums(studentNums);
            classInfos.add(classInfo);
        }
        return classInfos;
    }

    /**
     * 删除班级
     */
    @Override
    public boolean deleteClassInfo(ClassInfo classInfo) {
        final DdbUserClass ddbUserClass = ddbUserClassMapper.getByClassId(classInfo.getId());
        if (ddbUserClass == null) {
            return false;
        }
        // 根据ID删除班级
        ddbUserClassMapper.delete(classInfo.getId());
        return true;
    }

    /**
     * 根据班级id获取班级学生
     */
    @Override
    public List<Student> getStudentLists(UserSession userSession, ClassInfo classInfo) {
        // 根据班级外键查询出loginId，然后查询用户中心
        final List<String> loginIds = ddbUserClassRelaMapper.getByClassId(classInfo.getId());
        List<DdbUserClassRela> userClassRelas = ddbUserClassRelaMapper.getDdbUserClassRelaByClassId(classInfo.getId());
        if (userClassRelas == null) {
            return null;
        }
        // 请求用户中心获取头像集合
        List<UserPhoto> listPhotos = null;
        try {
            // 用户中心请求用户头像
            listPhotos = CommUtil.listPhotos(loginIds);
        } catch (SdkException e) {
            e.printStackTrace();
        }
        List<Student> students = new ArrayList<>();
        for (DdbUserClassRela classRela : userClassRelas) {
            if (listPhotos == null) {
                continue;
            }
            for (UserPhoto userPhoto : listPhotos) {
                if (classRela.getFkLoginId().equals(userPhoto.getLoginId())) {
                    Student student = new Student();
                    student.setId(classRela.getId());
                    student.setLoginId(classRela.getFkLoginId());
                    student.setName(classRela.getTrueName());
                    student.setPhoto(userPhoto.getPhoto());
                    student.setClassId(classRela.getFkClassId());
                    students.add(student);
                }
            }

        }

        return students;
    }

    /**
     * 删除学生
     */
    @Override
    public boolean deleteStudentInfo(UserSession userSession, Student student) {
        // 根据学生id来定位这条数据
        ddbUserClassRelaMapper.deleteById(student.getId());
        return true;
    }
    
    /**
     * 获取阅读等级或积分排行
     */
    @Override
    public List<Student> getLevelOrRanking(ClassInfo classInfo, UserSession userSession) {
        final String loginId = userSession.getLoginId();
        final List<String> classIds = ddbUserClassMapper.getClassId(loginId, classInfo.getClassName());
        final List<Student> students = new ArrayList<>();
        // 根据班级ids查询班级表
        if (classIds.size() == 0) {
            return null;
        }
        final List<DdbUserClassRela> ddbUserClassRelas = ddbUserClassRelaMapper.getDdbUserClassRela(classIds);
        if (ddbUserClassRelas == null) {
            return null;
        }
        final ArrayList<String> loginIds = new ArrayList<>();
        for (DdbUserClassRela ddbUserClassRela : ddbUserClassRelas) {
            loginIds.add(ddbUserClassRela.getFkLoginId());
        }
        // 请求用户中心获取头像集合
        List<UserPhoto> listPhotos = null;
        try {
            // 用户中心请求用户头像
            listPhotos = CommUtil.listPhotos(loginIds);
        } catch (SdkException e) {
            e.printStackTrace();
        }
        for (DdbUserClassRela ddbUserClassRela : ddbUserClassRelas) {
            for (UserPhoto userPhoto : listPhotos) {
                if (ddbUserClassRela.getFkLoginId().equals(userPhoto.getLoginId())) {
                    final Student student = new Student();
                    student.setLoginId(ddbUserClassRela.getFkLoginId());
                    student.setName(ddbUserClassRela.getTrueName());
                    student.setPhoto(userPhoto.getPhoto());
                    switch (classInfo.getType()) {
                    case Constants.READINGLEVEL:
                        final Integer readLevel = recordExamDetailMapper.getReadLevel(ddbUserClassRela.getFkLoginId());
                        student.setReadRating(readLevel == null ? 0 : readLevel);
                        break;
                    case Constants.INTEGRALRANKING:
                        final Map<String, String> day = CommUtil.getDay();
                        final String todayDate = day.get("todayDate");
                        final String beforeDate = day.get("beforeDate");
                        // 积分排行查询出每个学生的积分
                        final int userIntegral = ddbUserIntegralRecordMapper.getUserScore(loginId, beforeDate,
                                todayDate);
                        student.setIntegral(userIntegral);
                        // 查询出每个学生的点赞数量
                        final int praiseNum = ddbUserPraiseRelationshipMapper.getPraiseCount(loginId, beforeDate,
                                todayDate);
                        student.setPariseNum(praiseNum);
                        break;
                    default:
                        break;
                    }

                    students.add(student);
                }
            }
        }
        
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                /*
                 *  readRating(阅读等级)，integral(积分排行)两个字段其中必有一个字段为null，根据是否为null来进行降序排序
                 *  以下if else只是校验那个字段为null,如果readRating字段不为null,o1.getReadRating()和o2.getReadRating()
                 *  必定不为null，已经设置默认值0;如果integral字段不为null,o1.getIntegral()和o2.getIntegral() 必定不为null,
                 *  已经设置默认值0
                 */
                if (o1.getReadRating() != null && o2.getReadRating() != null) {
                    // 根据阅读等级进行排序 正值（大于），负值（小于），0（相等）
                    return o2.getReadRating() - o1.getReadRating();
                } else if (o1.getIntegral() != null && o2.getIntegral() != null) {
                    // 根据积分进行排序 正值（大于），负值（小于），0（相等）
                    return o2.getIntegral() - o1.getIntegral();
                }
                return 0;
            }
        });
        return students;
    }
    
    /**
     * 获取班级信息
     */
    @Override
    public DdbUserClass getClassByLoginId(String loginId) {
        final DdbUserClass userClass = userClassMapper.getClassByLoginId(loginId);
        if (userClass != null) {
            final Integer classSzie = ddbUserClassRelaMapper.getClassSizeByClassId(userClass.getId());
            int size = classSzie != null ? classSzie.intValue() : 0;
            userClass.setClassSize(size);
        }
        return userClass;
    }

    /**
     * 根据班级编号获取班级信息
     */
    @Override
    public DdbUserClass getClassByClassNumber(String classNumber) {
        return userClassMapper.getClassByClassNumber(classNumber);
    }

    /**
     * 编辑班级
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DdbUserClass editClass(ClassInfo classInfo, UserSession user) {
        // 获取用户班级
        final DdbUserClass ddbUserClass = userClassMapper.getClassByLoginId(user.getLoginId());
        // 判断用户班级是否为null
        if (ddbUserClass != null) {
            // 不为null,则判断是否为无老师班级
            if (ddbUserClass.getType() == 1) {
                // 是则重新编辑修改
                ddbUserClass.setClassName(classInfo.getClassName());
                ddbUserClass.setSchool(classInfo.getSchool());
                ddbUserClass.setTextbookEdition(classInfo.getTextbookEdition());
                userClassMapper.update(ddbUserClass);
                return ddbUserClass;
            }
            // 若为无老师班级则直接返回,不允许修改
            return null;
        }
        // 用户班级为空则创建对应无老师班级
        final DdbUserClass userClass = new DdbUserClass();
        BeanUtils.copyProperties(classInfo, userClass);
        final String genRecordKey = CommUtil.genRecordKey();
        userClass.setId(genRecordKey);
        userClass.setType(1);
        userClass.setFkLoginId(user.getLoginId());
        final Date curDate = new Date();
        userClass.setCreateTime(curDate);
        userClass.setUpdateTime(curDate);
        userClassMapper.create(userClass);
        classInfo.setId(genRecordKey);
        userClassRelaService.save(classInfo, user);
        return userClass;
    }

    /**
     * 学生退出班级
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitClass(String LoginId, String classId) {
        ddbUserClassRelaMapper.deleteByLoginIdClassId(LoginId, classId);
        return true;
    }
}