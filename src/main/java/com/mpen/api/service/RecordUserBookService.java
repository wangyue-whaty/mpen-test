/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.mp.shared.record.TaskRecord;
import com.mpen.api.bean.Book;
import com.mpen.api.bean.BookLearningInfo;
import com.mpen.api.bean.BookStudyInfo;
import com.mpen.api.bean.DateStudyTime;
import com.mpen.api.bean.ExamDetail;
import com.mpen.api.bean.FileParam;
import com.mpen.api.bean.Unit;
import com.mpen.api.bean.UserSession;
import com.mpen.api.bean.UserStudyDetail;
import com.mpen.api.bean.WeeklyDetail;
import com.mpen.api.bean.WeeklyParam;
import com.mpen.api.bean.Weeklys;
import com.mpen.api.domain.DdbRecordExamDetail;
import com.mpen.api.domain.DdbRecordUserBook;
import com.mpen.api.domain.OralTestDetail;
import com.mpen.api.domain.ReadingLevelResource;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;

public interface RecordUserBookService {
    /**
     * 保存日志信息.
     * 
     */
    Boolean save(TaskRecord taskRecord, UserSession userSession, String penId, List<DdbRecordUserBook> recordUserBooks) throws SdkException;

    /**
     * 获取数据点击次数信息.
     * 
     */
    Integer getPointTimes(String bookId) throws CacheException;

    /**
     * 获取用户完整学习信息.
     * 
     * @param dataType
     * 
     */
    UserStudyDetail getCompleteUserStudyInfo(UserSession userSession, HttpServletRequest request, int type)
            throws SdkException, CacheException;

    /**
     * 获取用户一本书学习信息.
     * 
     * @param dataType
     * 
     */
    BookStudyInfo getBookStudyInfo(UserSession session, String bookId, int type) throws CacheException, SdkException;

    /**
     * 获取用户一本书学习详情.
     * 
     */
    List<Unit> getBookContentStudyDetailByBookId(UserSession session, String bookId, int type)
            throws SdkException, CacheException;

    /**
     * 获取用户一本书口语评测详情.
     * 
     */
    List<Unit> getBookContentSpokenDetailByBookId(UserSession userSession, String bookId, int type)
            throws SdkException, CacheException;

    Map<String, Object> getUserStydyMap(String loginId) throws CacheException;

    void dealRecordUserBook(DdbRecordUserBook nodeDeatil, float totalTime, Map<String, DateStudyTime> dateMap,
        List<DdbRecordUserBook> list, Map<String, Object> map, Set<String> days, List<BookLearningInfo> bookList)
        throws CacheException;

    List<Weeklys> getWeeklyList(UserSession userSession);

    WeeklyDetail getWeekly(WeeklyParam weeklyParam, UserSession userSession) throws CacheException, ParseException;

    List<DateStudyTime> getDateStudyTime(UserSession userSession, Book book) throws CacheException;

    boolean saveOralTest(HttpServletRequest request, String filePath, FileParam fileParam);

    
    /**
     * 保存阅读分级考试详情
     * @param userSession
     * @param examDetail
     */
    ReadingLevelResource saveExamDetail(UserSession userSession, ExamDetail examDetail);
    
    List<DdbRecordExamDetail> getExamDetail(UserSession userSession);
    /**
     * 根据id删除一条记录
     * @param id
     */
    void deleteExamDetailByID(String id);

    void initOralTest();

    void oralEvaluate(OralTestDetail detail);
    
    /**
     * 获取阅读分级对应资源信息
     * @param level
     * @return
     */
    ReadingLevelResource getReadingLevelResource(int level);

    List<DateStudyTime> getDateStudyTimeStat(String loginId, String date) throws CacheException;

    
}
