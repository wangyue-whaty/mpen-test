package com.mpen.api.controller;

import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mp.shared.common.NetworkResult;
import com.mpen.api.bean.ClassInfo;
import com.mpen.api.bean.HomeWorks;
import com.mpen.api.bean.Student;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.domain.DdbUserClass;
import com.mpen.api.domain.DdbUserClassRela;
import com.mpen.api.service.DdbUserClassRelaService;
import com.mpen.api.service.DdbUserClassService;
import com.mpen.api.service.DdbUserHomeworkStateService;
import com.mpen.api.service.HomeWorkService;
import com.mpen.api.service.UserSessionService;

/**
 * 外研通2.0班级信息API服务类 涉及：教师端管理班级接口以及App2.0涉及作业，班级情况相关接口
 * 
 * @author hzy
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_CLASS)
public class ClassManageController {
    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private DdbUserClassService userClassService;

    @Autowired
    private HomeWorkService homeWorkService;
    
    @Autowired
    private DdbUserClassRelaService userClassRelaService;
    
    @Autowired
    private DdbUserHomeworkStateService homeworkStateService;
    /**
     * 用户班级管理
     * 
     * @param book
     * @param request
     * @param response
     * @return
     */
    @PostMapping(Uris.CLASSINFO)
    public @ResponseBody Callable<NetworkResult<Object>> getClassInfo(@RequestBody final ClassInfo classInfo,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession user = userSessionService.getUser(request, response);
                NetworkResult<Object> networkResult;
                switch (classInfo.getAction()) {
                // 获取用户班级信息(返回300需完善班级信息，200返回已加入班级信息)
                case Constants.USER_CLASS:
                    // 查询所在班级
                    final DdbUserClass userClass = userClassService.getClassByLoginId(user.getLoginId());
                    if (userClass != null) {
                        networkResult = RsHelper.success(userClass);
                    } else {
                        networkResult = RsHelper.error("300", "未加入班级!");
                    }
                    break;
                // 根据班级编号获取班级信息
                case Constants.CLASS_INFO:
                    // 获取邀请码
                    final String classNumber = classInfo.getClassNumber();
                    if (StringUtils.isBlank(classNumber)) {
                        networkResult = RsHelper.error(Constants.INVALID_PARAMRTER_MESSAGE);
                    }
                    // 根据邀请码查询班级信息
                    final DdbUserClass ddbUserClass = userClassService.getClassByClassNumber(classNumber);
                    if (ddbUserClass != null) {
                        networkResult = RsHelper.success(ddbUserClass);
                    } else {
                        networkResult = RsHelper.error(Constants.BAD_REQUEST_ERROR_CODE);
                    }
                    break;
                // 加入班级
                case Constants.JOIN_CLASS:
                    // 保存学生班级信息
                    final boolean flag = userClassRelaService.save(classInfo, user);
                    if (flag) {
                        // 获取班级所有学生loginId
                        final List<DdbUserClassRela> userClassRelas = userClassRelaService.listByClassId(classInfo.getId());
                        networkResult = RsHelper.success(userClassRelaService.listPhotos(userClassRelas));
                    } else {
                        networkResult = RsHelper.error(Constants.BAD_REQUEST_ERROR_CODE, "false");
                    }
                    break;
                // 编辑班级
                case Constants.EDIT_CLASS:
                    final DdbUserClass editClass = userClassService.editClass(classInfo, user);
                    if (editClass == null) {
                        networkResult = RsHelper.error(Constants.BAD_REQUEST_ERROR_CODE);
                    } else {
                        networkResult = RsHelper.success(editClass);
                    }
                    break;
                // 退出班级
                case Constants.QUIT_CLASS:
                    networkResult = RsHelper.success(userClassService.quitClass(user.getLoginId(), classInfo.getId()));
                    break;
                // 教师端保存班级信息
                case Constants.SAVE_CLASS_INFO:
                    return RsHelper.success(userClassService.saveClassInfo(user, classInfo));
                // 教师端更改班级信息
                case Constants.UPDATE_CLASS_INFO:
                    return RsHelper.success(userClassService.updateClassInfo(classInfo));
                default:
                    networkResult = RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                    break;
                }
                return networkResult;
            }
        };
    }
    /**
     * 用户班级作业
     * 
     * @param homeWorks
     * @param request
     * @param response
     * @return
     */
    @PostMapping(Uris.CLASSTASK)
    public @ResponseBody Callable<NetworkResult<Object>> classTask(@RequestBody HomeWorks homeWorks,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession user = userSessionService.getUser(request, response);
                NetworkResult<Object> networkResult;
                switch (homeWorks.getAction()) {
                case Constants.CLASS_PREVIEW:
                    homeWorks.setType(Constants.PREVIEW);
                    networkResult = RsHelper.success(homeWorkService.pageHomeWork(user, homeWorks));
                    break;
                case Constants.CLASS_PREVIEW_DETAIL:
                    networkResult = RsHelper.success(homeWorkService.getHomeWorkDetail(user, homeWorks));
                    break;
                case Constants.SUBMIT_JOB:
                    networkResult = RsHelper.success(homeworkStateService.save(homeWorks, user));
                    break;
                case Constants.CLASS_AFTER:
                    homeWorks.setType(Constants.HOMEWORK_AFTER);
                    networkResult = RsHelper.success(homeWorkService.pageHomeWork(user, homeWorks));
                    break;
                case Constants.CLASS_AFTER_DETAIL:
                    networkResult = RsHelper.success(homeWorkService.getHomeWorkDetail(user, homeWorks));
                    break;
                case Constants.GET_PUSHBOOK_LIST:
                    networkResult = RsHelper.success(homeWorkService.getPushBookVideo());
                    break;
                default:
                    networkResult = RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                    break;
                }
                return networkResult;
            }
        };
    }

    /**
     * 教师管理班级信息
     */
    @GetMapping(Uris.CLASSINFO)
    public @ResponseBody Callable<NetworkResult<Object>> getClassInfos(final ClassInfo classInfo,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession userSession = userSessionService.getUser(request, response);
                switch (classInfo.getAction()) {
                
                // 获取班级列表
                case Constants.GET_CLASS_LISTS:
                    return RsHelper.success(userClassService.getClassLists(userSession));
                // 删除班级
                case Constants.DELETE_CLASS_INFO:
                    return RsHelper.success(userClassService.deleteClassInfo(classInfo));
                // 获取班级学生列表信息
                case Constants.GET_STUDENT_LISTS:
                    return RsHelper.success(userClassService.getStudentLists(userSession, classInfo));
                // 班级积分排行，阅读评级
                case Constants.GET_LEVEL_OR_RANKING:
                    return RsHelper.success(userClassService.getLevelOrRanking(classInfo, userSession));
             
                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }

    /**
     * 教师删除学生信息
     */
    @GetMapping(Uris.STUDENT)
    public @ResponseBody Callable<NetworkResult<Object>> deleteStudent(final Student student,
            final HttpServletRequest request, final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final UserSession userSession = userSessionService.getUser(request, response);
                switch (student.getAction()) {
                // 教师删除学生信息
                case Constants.DELETE_STUDENT_INFO:
                    return RsHelper.success(userClassService.deleteStudentInfo(userSession, student));

                default:
                    return RsHelper.error(Constants.NO_MACHING_ERROR_CODE);
                }
            }
        };
    }
}
