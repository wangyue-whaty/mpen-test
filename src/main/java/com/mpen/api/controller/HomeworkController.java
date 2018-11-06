package com.mpen.api.controller;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mp.shared.common.NetworkResult;
import com.mpen.api.bean.Book;
import com.mpen.api.bean.ClassAssignments;
import com.mpen.api.bean.ClassInfo;
import com.mpen.api.bean.FileParam;
import com.mpen.api.bean.HomeworkAfterClass;
import com.mpen.api.bean.PreviewBeforeClass;
import com.mpen.api.bean.Student;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.service.FileService;
import com.mpen.api.service.HomeWorkService;
import com.mpen.api.service.ResourceBookService;
import com.mpen.api.service.UserSessionService;
import com.mpen.api.util.FileUtils;

/**
 * 作业相关API
 * 涉及：教师端布置作业，批阅作业，查看班级作业情况，学生个人作业情况、资源上传等相关接口
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_HOMEWORK)
public class HomeworkController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeworkController.class);
    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private HomeWorkService homeWorkService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ResourceBookService resourceBookService;
  
    /**
     * 教师布置课后作业
     */
    @PostMapping(Uris.HOMEWORK)
    public @ResponseBody Callable<NetworkResult<Object>> saveHomework(
            @RequestBody final HomeworkAfterClass homeworkAfterClass, final HttpServletRequest request,
            final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession userSession = userSessionService.getUser(request, response);
                switch (homeworkAfterClass.getAction()) {
                // 布置课后作业
                case Constants.SAVEN_HOMEWORK:
                    return RsHelper
                            .success(homeWorkService.savePreviewOrHomework(userSession, null, homeworkAfterClass));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }

    /**
     * 教师布置课前导学
     */
    @PostMapping(Uris.PREVIEW)
    public @ResponseBody Callable<NetworkResult<Object>> savePreviewBeforeClass(
            @RequestBody final PreviewBeforeClass previewBeforeClass, final HttpServletRequest request,
            final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession userSession = userSessionService.getUser(request, response);
                switch (previewBeforeClass.getAction()) {
                // 布置课前导学
                case Constants.SAVE_PREVIEW:
                    return RsHelper
                            .success(homeWorkService.savePreviewOrHomework(userSession, previewBeforeClass, null));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
    
    /**
     * 班级作业列表 班级作业详情
     */
    @GetMapping(Uris.ASSIGNMENTS)
    public @ResponseBody Callable<NetworkResult<Object>> getHomeWorkList(final ClassInfo classInfo,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession userSession = userSessionService.getUser(request, response);
                switch (classInfo.getAction()) {
                // 获取班级作业情况
                case Constants.GET_CLASSHOMEWORK_LIST:
                    return RsHelper.success(homeWorkService.getHomeWorkList(classInfo,null,Constants.COLLECTIVE));
                // 获取批阅历史作业
                case Constants.GET_HOMEWORK_LIST:
                    return RsHelper.success(homeWorkService.getAssignments(userSession, classInfo));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
    /**
     * 教师获取评语
     */
    @GetMapping(Uris.COMMENTS)
    public @ResponseBody Callable<NetworkResult<Object>> getComments(final Book book, final HttpServletRequest request,
            final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (book.getAction()) {
                // 获取固定字典评论
                case Constants.GET_ALL_COMMENTS:
                    return RsHelper.success(homeWorkService.getAllComments());
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
    /**
     * 教师查看未批阅，已经批阅，未提交,导学详情
     * 
     */
    @GetMapping(Uris.CHECK_ASSIGNMENTS)
    public @ResponseBody Callable<NetworkResult<Object>> getReviewNumber(final ClassAssignments classAssignments,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession userSession = userSessionService.getUser(request, response);
                switch (classAssignments.getAction()) {
                // 查询已批阅、未批阅，已提交，未提交人数
                case Constants.GET_NUMBER:
                    return RsHelper.success(homeWorkService.getNumber(classAssignments.getId(), userSession));
                // 查询已批阅、未批阅，未提交或作业情况接口
                case Constants.GET_MEMBER:
                    return RsHelper.success(homeWorkService.getReviewMember(classAssignments.getId(),
                            classAssignments.getReviewType(), userSession));
                // 获取导学详情或课后作业内容
                case Constants.GET_DETAILS:
                    return RsHelper.success(homeWorkService.getReviewDetails(classAssignments));

                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
    
    /**
     * 获取书籍信息
     */
    @GetMapping(Uris.TEXTBOOK)
    public @ResponseBody Callable<NetworkResult<Object>> getUserInfo(final Book book, final HttpServletRequest request,
            final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession userSession = userSessionService.getUser(request, response);
                switch (book.getAction()) {
                // 获取全部课本学习书籍
                case Constants.GET_ALL_ENGLISHBOOK:
                    return RsHelper.success(homeWorkService.getAllBook());
                // 获取课本学习内容
                case Constants.GET_BOOK_CONTENT_DETAIL:
                    return RsHelper.success(homeWorkService.getBookContentDetail(book, userSession));
                // 获取全部口语考试卷书籍
                case Constants.GET_ALL_ORALTEST:
                    return RsHelper.success(homeWorkService.getAllOralBook(userSession));
                // 获取口语考试卷
                case Constants.GET_ORALTEST_CONTENT:
                    return RsHelper.success(resourceBookService.getOralBookContent(book.getId()));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
    /**
     * 教师/学生上传教学作业音频，图片资源.
     * 
     */
    @PostMapping(Uris.UPLOAD)
    public @ResponseBody Callable<NetworkResult<Object>> uploadFile(final FileParam fileParam ,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (fileParam.getAction()) {
                // 上传教师端资源文件
                case Constants.UPLOAD_TEACHER_FILE:
                    return RsHelper.success(fileService.saveFiles(fileParam, FileUtils.TEACHER_FILES));
                // 上传app2.0资源
                case Constants.UPLOAD_STUDENT_FILE:
                    return RsHelper.success(fileService.saveFiles(fileParam, FileUtils.STUDENT_FILES));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
    /**
     * 教师获取某个学生的作业详情
     */
    @GetMapping(Uris.HOMEWROK_DETAIL)
    public @ResponseBody Callable<NetworkResult<Object>> getStudentDetails(final Student student,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (student.getAction()) {
                // 获取某个学生课前导学情况
                case Constants.GET_HOMEWROK_DETAILS:
                    return RsHelper.success(homeWorkService.getSudentHomeworkDetails(student));
                // 获取某个学生课后作业情况
                case Constants.GET_STUDENT_DETAILS:
                    return RsHelper.success(homeWorkService.getHomeWorkList(null,student,Constants.PERSONAL));
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
    /**
     * 教师评测某个学生
     */
    @PostMapping(Uris.HOMEWROK_DETAIL)
    public @ResponseBody Callable<NetworkResult<Object>> saveStudentDetails(@RequestBody final Student student,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession userSession = userSessionService.getUser(request, response);
                switch (student.getAction()) {
                // 教师评测学生
                case Constants.SAVE_HOMEWORK_DETAILS:
                    return RsHelper.success(homeWorkService.saveStudentHomeWorkDetails(userSession, student));
               
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
    /**
     * 催作业
     */
    @PostMapping(Uris.CHECK_ASSIGNMENTS)
    public @ResponseBody Callable<NetworkResult<Object>> saveRushJob(@RequestBody final ClassAssignments classAssignments,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (classAssignments.getAction()) {
                // 催作业
                case Constants.RUSH_JOB:
                    return RsHelper.success(homeWorkService.RushJob(classAssignments));
               
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
    
    
}
