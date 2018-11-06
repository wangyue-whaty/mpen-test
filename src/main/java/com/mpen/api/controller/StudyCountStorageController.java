package com.mpen.api.controller;

import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mp.shared.common.NetworkResult;
import com.mpen.api.bean.StudyCountAssist;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.domain.DdbLearnLogBookDetailTrace;
import com.mpen.api.domain.DdbLearnLogBookSumTrace;
import com.mpen.api.domain.DdbLearnLogBookTrace;
import com.mpen.api.domain.DdbLearnLogDayTrace;
import com.mpen.api.domain.DdbResourceBook;
import com.mpen.api.service.DdbLearnLogBookDetailTraceService;
import com.mpen.api.service.DdbLearnLogBookSumTraceService;
import com.mpen.api.service.DdbLearnLogBookTraceService;
import com.mpen.api.service.DdbLearnLogDayTraceService;
import com.mpen.api.service.ResourceBookService;

/**
 * 学情日志统计存储接口
 * TODO 与数据服务一起拆分出去
 * @author hzy
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_STUDY_COUNT)
public class StudyCountStorageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeworkController.class);
    @Autowired
    private DdbLearnLogDayTraceService learnLogDayTraceService;
    @Autowired
    private DdbLearnLogBookDetailTraceService learnLogBookDetailTraceService;
    @Autowired
    private DdbLearnLogBookSumTraceService learnLogBookSumTraceService;
    @Autowired
    private DdbLearnLogBookTraceService learnLogBookTraceService; 
    @Autowired
    private ResourceBookService bookService;
    
    /**
     * 保存用户每日学习详情统计
     * @param logDayTraces
     * @param request
     * @param response
     * @return
     */
    @PostMapping(Uris.LEARNLOG_DAYTRACE)
    public @ResponseBody Callable<NetworkResult<Object>> learnLogDayTrace(
            @RequestBody final List<DdbLearnLogDayTrace> logDayTraces, final HttpServletRequest request,
            final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                return RsHelper.success(learnLogDayTraceService.batchUpdate(logDayTraces));
            }
        };
    }
    
    /**
     * 保存用户学习书籍内容详情统计
     * @param logBookDetailTraces
     * @param request
     * @param response
     * @return
     */
    @PostMapping(Uris.LEARNLOG_BOOKDETAILTRACE)
    public @ResponseBody Callable<NetworkResult<Object>> learnLogBookDetailTrace(
            @RequestBody final List<DdbLearnLogBookDetailTrace> logBookDetailTraces, final HttpServletRequest request,
            final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                return RsHelper.success(learnLogBookDetailTraceService.batchUpdate(logBookDetailTraces));
            }
        };
    }
    
    /**
     * 保存用户学习书籍列表统计
     * @param logBookSumTraces
     * @param request
     * @param response
     * @return
     */
    @PostMapping(Uris.LEARNLOG_BOOKSUMTRACE)
    public @ResponseBody Callable<NetworkResult<Object>> learnLogBookSumTrace(
            @RequestBody final List<DdbLearnLogBookSumTrace> logBookSumTraces, final HttpServletRequest request,
            final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                return RsHelper.success(learnLogBookSumTraceService.batchUpdate(logBookSumTraces));
            }
        };
    }
    
    /**
     * 保存用户书籍学习轨迹统计
     * @param logBookTraces
     * @param request
     * @param response
     * @return
     */
    @PostMapping(Uris.LEARNLOG_BOOKTRACE)
    public @ResponseBody Callable<NetworkResult<Object>> learnLogBookTrace(
            @RequestBody final List<DdbLearnLogBookTrace> logBookTraces, final HttpServletRequest request,
            final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                return RsHelper.success(learnLogBookTraceService.batchUpdate(logBookTraces));
            }
        };
    }
    
    /**
     * 获取书籍类型
     * @param studyCountAssist
     * @param request
     * @param response
     * @return
     */
    @PostMapping(Uris.BOOK_TYPE)
    public @ResponseBody Callable<NetworkResult<Object>> bookType(
            @RequestBody final StudyCountAssist studyCountAssist, final HttpServletRequest request,
            final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                final DdbResourceBook resourceBook = bookService.getById(studyCountAssist.getFkBookId());
                return RsHelper.success(resourceBook.getType());
            }
        };
    }
    
    /**
     * 获取书籍学习轨迹
     * @param studyCountAssist
     * @param request
     * @param response
     * @return
     */
    @PostMapping(Uris.STUDY_PAGE)
    public @ResponseBody Callable<NetworkResult<Object>> studyPage(
            @RequestBody final StudyCountAssist studyCountAssist, final HttpServletRequest request,
            final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                return RsHelper.success(learnLogBookTraceService.getByLoginIdBookIdStudyDate(studyCountAssist));
            }
        };
    }
}
