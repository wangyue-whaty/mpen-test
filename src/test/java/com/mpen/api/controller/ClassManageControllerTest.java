package com.mpen.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.bean.ClassInfo;
import com.mpen.api.bean.HomeWorks;
import com.mpen.api.bean.HomeworkResourceUrl;

/**
 * 班级测试类
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ClassManageControllerTest extends TestBase {
    private static final String CLASSINFO_URL = "/v1/class/classInfo/";
    private static final String CLASSTASK_URL = "/v1/class/classTask/";
    private static final String STUDENT_URL = "/v1/class/student/";
    private static final String userName = "18931334240";
    private static final String password = "5455001765033-1540449365985-088c7c75d9bcd8854450390aa9d206df";

    /**
     * 班级管理controller测试
     */
    @Test
    public void testClassManagerInfo() {
        ClassInfo classInfo = getClassInfo();
        // 教师保存班级信息测试
        this.postControllerTest(userName, password, classInfo, CLASSINFO_URL);
        classInfo.setClassName("六年级一班");
        classInfo.setAction("updateClassInfo");
        // 教师更改班级信息
        this.postControllerTest(userName, password, classInfo, CLASSINFO_URL);
        // 教师获取班级列表测试
        this.getControllerTest(userName, password, CLASSINFO_URL + "?action=getClassLists");
        // 根据班级获取学生信息测试
        this.getControllerTest(userName, password,
                CLASSINFO_URL + "?action=getStudentLists&id=1283c1cfb635448bb3b4818dbf33c3d4");
        // 根据学生id和班级id删除某个班级学生测试
        this.getControllerTest(userName, password,
                STUDENT_URL + "?action=deleteStudentInfo&classId=1283c1cfb635448bb3b4818dbf33c3d4&id=1");
        // 教师删除班级信息测试
        this.getControllerTest(userName, password,
                CLASSINFO_URL + "?action=deleteClassInfo&id=dc9825f6437c4cea90618dbd287aefd5");
        // 获取阅读等级、积分排行测试
        this.getControllerTest(userName, password, CLASSINFO_URL + "?action=getlevelOrRanking&type=0");
        classInfo.setAction("classInfo");
        classInfo.setInvitationCode("11111");
        // 根据邀请码获取班级
        this.postControllerTest(userName, password, classInfo, CLASSINFO_URL);
        classInfo.setAction("joinClass");
        classInfo.setId("1283c1cfb635448bb3b4818dbf33c3d4");
        classInfo.setTrueName("测试");
        // 加入班级
        this.postControllerTest(userName, password, classInfo, CLASSINFO_URL);
        classInfo.setAction("editClass");
        classInfo.setSchool("学校");
        classInfo.setClassName("幼儿园一班");
        classInfo.setTextbookEdition("教材版本");
        // 编辑班级
        this.postControllerTest(userName, password, classInfo, CLASSINFO_URL);
        classInfo.setAction("userClass");
        // 获取个人班级信息(学生已加入班级)
        this.postControllerTest(userName, password, classInfo, CLASSINFO_URL);
        classInfo.setAction("quitClass");
        classInfo.setId("1283c1cfb635448bb3b4818dbf33c3d4");
        // 退出班级
        this.postControllerTest(userName, password, classInfo, CLASSINFO_URL);
        // 获取课前预习列表
        HomeWorks homeWorks = new HomeWorks();
        homeWorks.setAction("classPreview");
        this.postControllerTest(userName, password, homeWorks, CLASSTASK_URL);
        // 获取课后作业列表
        homeWorks.setAction("classAfter");
        this.postControllerTest(userName, password, homeWorks, CLASSTASK_URL);
        homeWorks.setAction("classAfterDetail");
        homeWorks.setId("6d22edcf182844a2bebe398d319e8376");
        // 获取课后作业详情
        this.postControllerTest(userName, password, homeWorks, CLASSTASK_URL);
        homeWorks.setAction("submitJob");
        homeWorks.setId("6d22edcf182844a2bebe398d319e8376");
        homeWorks.setFkClassId("1283c1cfb635448bb3b4818dbf33c3d4");
        List<HomeworkResourceUrl> resourceUrls = new ArrayList<>();
        HomeworkResourceUrl resourceUrl = new HomeworkResourceUrl();
        resourceUrl.setType("mp3");
        resourceUrl.setUrl("11.mp3");
        homeWorks.setResourceUrl(resourceUrls);
        // 提交作业
        this.postControllerTest(userName, password, homeWorks, CLASSTASK_URL);
    }
}
