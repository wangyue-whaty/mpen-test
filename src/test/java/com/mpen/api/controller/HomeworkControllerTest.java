package com.mpen.api.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;

/**
 * 作业测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HomeworkControllerTest extends TestBase {
    @Autowired
    private static final String SAVE_HOMEWORK_URI = "/v1/homework/homework";
    private static final String SAVE_PREVIEWBEFORECLASS_URI = "/v1/homework/preview";
    private static final String ASSIGNMENTS_URl = "/v1/homework/assignments";
    private static final String CHECK_ASSINGMENTS_URL = "/v1/homework/checkAssignments";
    private static final String GET_COMMENTS_URl = "/v1/homework/comments";
    private static final String GET_TEXTBOOK_URI = "/v1/homework/textbook";
    private static final String CHECK_ASSIGNMENTS_URL = "/v1/homework/checkAssignments";
    private static final String HOMEWORK_DETAIL_URL="/v1/homework/details";

    private static final String userName = "18931334240";
    private static final String password = "2329403848055-1536284387332-59dba675cb32945849ffb6ff29e7ddbc";

    /**
     * 作业测试
     */
    @Test
    public void testHomeWorkSuccess() {
        // 教师布置课后作业测试
        this.postControllerTest(userName, password, getHomeworkAfterClass(), SAVE_HOMEWORK_URI);
        // 教师布置课后导学测试
        this.postControllerTest(userName, password, getPreviewBeforeClass(), SAVE_PREVIEWBEFORECLASS_URI);
        // 获取批阅历史作业测试
        this.getControllerTest(userName, password, ASSIGNMENTS_URl + "?action=getHomeWorkList&state=0&className=一年级一班");
        // 获取班级作业情况测试
        this.getControllerTest(userName, password,
                ASSIGNMENTS_URl + "?action=getClassHomeWorkList&homeWorkId=55e80a0087bf451089274d155adc9cff");
        // 获取固定字典评论测试
        this.getControllerTest(userName, password, GET_COMMENTS_URl + "?action=getAllComments");
        // 查询已批阅、未批阅，已提交，未提交人数测试
        this.getControllerTest(userName, password,
                CHECK_ASSINGMENTS_URL + "?action=getNumber&id=839f556256fd43d59a1c1a31982aa4a2");
        // 查询已批阅、未批阅，未提交或作业情况测试
        this.getControllerTest(userName, password,
                CHECK_ASSINGMENTS_URL + "?action=getMember&id=0eee0cc72b4041acb0f6460b1ce07df4&reviewType=2");
        // 获取英语课本书籍测试
        this.getControllerTest(userName, password, GET_TEXTBOOK_URI + "?action=getAllBooks");
        // 获取课本学习内容测试
        this.getControllerTest(userName, password, GET_TEXTBOOK_URI+"?action=getBookContent&id=ff808081567761c201569201cfde06ad");
        // 获取书籍内容测试测试
        this.getControllerTest(userName, password,
                GET_TEXTBOOK_URI + "?action=getBookContentDetail&id=ff808081567761c201569201cfde06ad");
        // 获取全部口语考试卷书籍测试
        this.getControllerTest(userName, password, GET_TEXTBOOK_URI + "?action=getAllOraltest");
        // 教师评测学生测试
        this.postControllerTest(userName, password, getHomeworkAfterClass(), SAVE_HOMEWORK_URI);
        // 一键催作业
        this.postControllerTest(userName, password, getClassAssignments(), CHECK_ASSIGNMENTS_URL);
        // 获取学生课前导学内容
        this.getControllerTest(userName, password, HOMEWORK_DETAIL_URL
                + "?action=getDetails&homeworkId=484db7b69c6f480baceaa56ad04bc8c8&loginId=13554433829");
    }
}