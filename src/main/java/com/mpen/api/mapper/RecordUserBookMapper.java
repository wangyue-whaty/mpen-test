/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.EnumTypeHandler;

import com.mpen.api.bean.ActivityStudyDetail;
import com.mpen.api.bean.DataAnalysisResult.BookRanking;
import com.mpen.api.bean.GoodsInfo;
import com.mpen.api.domain.DdbRecordUserBook;
import com.mpen.api.mapper.db.ResourceBookTypeHandler;

/**
 * AppMapper对象.
 * 
 * @author zyt
 *
 */
@Mapper
public interface RecordUserBookMapper {
    
    /*
     * RecordUserBookMapper这个类操作两张表，分别是ddb_record_user_book和ddb_record_user_book_second表。ddb_record_user_book表为旧表，
     * 当做口语评测细化接口时，需要保存笔端传过来的云知声原始评测数据，因这张表数据量太大，无法再增加表字段，因此添加一张新的表，名字为ddb_record_user_book_second表。
     * 新表的新增字段为USER_RECOGNIZE_BYTES，该字段保存的是云知声口语评测原始数据经过zip压缩过的byte[]数组。该字段保存需要压缩，调用CompressionTools.zip(userRecognizeTxt)方法，
     * 解压使用CompressionTools.unzip(userRecognizeBytes)方法。
     * 特别注意：当需要保存数据时，往ddb_record_user_book_second保存数据，当查询数据时，用sql union来同时查询两张表的数据。
     */
    
    //使用该方法往数据库中保存数据，该方法是保存到ddb_record_user_book_second新表中。
    @Result(column = "CODE_TYPE", property = "codeType", typeHandler = EnumTypeHandler.class)
    @Insert("INSERT INTO DDB_RECORD_USER_BOOK_SECOND (ID,LOGIN_ID,FK_BOOK_ID,CODE,CLICK_TIME,TYPE,END_TIME,VOICE_TYPE,"
        + "FUNCTION,FK_ACTIVITY_ID,PAGE,TEXT,SCORE,TIME,CODE_TYPE,USER_RECOGNIZE_BYTES) VALUES(#{id},#{loginId},#{fkBookId},#{code},"
        + "#{clickTime},#{type},#{endTime},#{voiceType},#{function},#{fkActivityId},#{page},#{text},#{score},#{time},"
        + "#{codeType},#{userRecognizeBytes})")
    void save(DdbRecordUserBook userBook);
    
    //该方法是往ddb_record_user_book旧表保存数据，此方法弃用。
    @Result(column = "CODE_TYPE", property = "codeType", typeHandler = EnumTypeHandler.class)
    @Insert("INSERT INTO DDB_RECORD_USER_BOOK (ID,LOGIN_ID,FK_BOOK_ID,CODE,CLICK_TIME,TYPE,END_TIME,VOICE_TYPE,"
        + "FUNCTION,FK_ACTIVITY_ID,PAGE,TEXT,SCORE,TIME,CODE_TYPE) VALUES(#{id},#{loginId},#{fkBookId},#{code},"
        + "#{clickTime},#{type},#{endTime},#{voiceType},#{function},#{fkActivityId},#{page},#{text},#{score},#{time},"
        + "#{codeType})")
    void saveOldTable(DdbRecordUserBook userBook);
    
    // 往新增的4张表中插入数据 table名字为动态获取通过 ${}
    @Result(column = "CODE_TYPE", property = "codeType", typeHandler = EnumTypeHandler.class)
    @Insert("INSERT INTO ${tableName} (ID,LOGIN_ID,FK_BOOK_ID,CODE,CLICK_TIME,TYPE,END_TIME,VOICE_TYPE,"
        + "FUNCTION,FK_ACTIVITY_ID,PAGE,TEXT,SCORE,TIME,CODE_TYPE,USER_RECOGNIZE_BYTES) VALUES(#{userBook.id},#{userBook.loginId},#{userBook.fkBookId},#{userBook.code},"
        + "#{userBook.clickTime},#{userBook.type},#{userBook.endTime},#{userBook.voiceType},#{userBook.function},#{userBook.fkActivityId},#{userBook.page},#{userBook.text},#{userBook.score},#{userBook.time},"
        + "#{userBook.codeType},#{userBook.userRecognizeBytes})")
    int saveToShardTable(@Param("userBook") DdbRecordUserBook userBook,@Param("tableName")String tableName);
    
    // TODO此方法因表数据量大，查询不出数据，要在新的数据pipeline中能够处理，查询该数据。
    @Result(column = "CODE_TYPE", property = "codeType", typeHandler = EnumTypeHandler.class)
    @Select("SELECT FK_BOOK_ID BOOK_ID,COUNT(DISTINCT(LOGIN_ID)) READNUM  FROM  "
            +"(SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION, "
            + "FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE, USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK_SECOND UNION  "
            + "SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION,"
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE, NUll as USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK)"
            + "AS DDB_RECORD_USER_BOOK "
            + " GROUP BY "
        + "FK_BOOK_ID")
    List<GoodsInfo> getBookCustomTimes();

    @Result(column = "CODE_TYPE", property = "codeType", typeHandler = EnumTypeHandler.class)
    @Select("SELECT * FROM ("
            + " SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION,"
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE, USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK_SECOND  "
            + " WHERE LOGIN_ID=#{loginId} UNION "
            + " SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION,"
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE, NUll as USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK  "
            + " WHERE LOGIN_ID=#{loginId} UNION "
            + " SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION, "
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE, USER_RECOGNIZE_BYTES FROM ${tableName} "
            + " WHERE LOGIN_ID=#{loginId} "
            + ") AS DDB_RECORD_USER_BOOK ORDER BY CLICK_TIME DESC")
    List<DdbRecordUserBook> getByLoginId(@Param("loginId") String loginId,@Param("tableName")String tableName);

    @Result(column = "CODE_TYPE", property = "codeType", typeHandler = EnumTypeHandler.class)
    @Select("SELECT * FROM ( "
            + " SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION, "
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE, USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK_SECOND  "
            + " WHERE LOGIN_ID=#{loginId} AND CLICK_TIME>=#{date} UNION "
            + " SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION,"
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE, NUll as USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK "
            + " WHERE LOGIN_ID=#{loginId} AND CLICK_TIME>=#{date} UNION "
            + " SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION, "
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE, USER_RECOGNIZE_BYTES FROM ${tableName} "
            + " WHERE LOGIN_ID=#{loginId} AND CLICK_TIME>=#{date}"
            + ") AS DDB_RECORD_USER_BOOK "
            + "ORDER BY CLICK_TIME DESC ")
    List<DdbRecordUserBook> getByLoginIdAndDate(@Param("loginId") String loginId, @Param("date") Date date,@Param("tableName")String tableName);

    @Result(column = "CODE_TYPE", property = "codeType", typeHandler = EnumTypeHandler.class)
    @Select("SELECT * FROM ( "
            + " SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION, "
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE, USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK_SECOND "
            + " WHERE LOGIN_ID=#{loginId} AND FK_BOOK_ID=#{bookId} UNION  "
            + " SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION,"
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE, NUll as USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK "
            + " WHERE LOGIN_ID=#{loginId} AND FK_BOOK_ID=#{bookId} UNION "
            + " SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION, "
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE,  USER_RECOGNIZE_BYTES FROM ${tableName}  "
            + " WHERE LOGIN_ID=#{loginId} AND FK_BOOK_ID=#{bookId} "
            + " ) AS DDB_RECORD_USER_BOOK ORDER BY "
            + " CLICK_TIME DESC")
    List<DdbRecordUserBook> getByLoginIdAndBookId(@Param("loginId") String loginId, @Param("bookId") String bookId,@Param("tableName")String tableName);

    @Result(column = "CODE_TYPE", property = "codeType", typeHandler = EnumTypeHandler.class)
    @Select("SELECT SUM(COUNT_TIMES) COUNT_TIMES,MAX(DATE) DATE,FK_ACTIVITY_ID,SUM(TIME) TIME FROM( "
            + " SELECT  COUNT(*) COUNT_TIMES,MAX(CLICK_TIME) DATE,FK_ACTIVITY_ID,SUM(TIME) TIME, USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK_SECOND "
            + " WHERE LOGIN_ID=#{loginId} AND FK_BOOK_ID=#{bookId} GROUP BY FK_ACTIVITY_ID "
            + " UNION  SELECT  COUNT(*) COUNT_TIMES,MAX(CLICK_TIME) DATE,FK_ACTIVITY_ID,SUM(TIME) TIME,NULL as USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK "
            + " WHERE LOGIN_ID=#{loginId} AND FK_BOOK_ID=#{bookId} GROUP BY FK_ACTIVITY_ID"
            + " UNION SELECT  COUNT(*) COUNT_TIMES,MAX(CLICK_TIME) DATE,FK_ACTIVITY_ID,SUM(TIME) TIME, USER_RECOGNIZE_BYTES FROM ${tableName} "
            + " WHERE LOGIN_ID=#{loginId} AND FK_BOOK_ID=#{bookId} GROUP BY FK_ACTIVITY_ID"
            + " )AS DDB_RECORD_USER_BOOK GROUP BY FK_ACTIVITY_ID ")
    List<ActivityStudyDetail> getStudyDetailByLoginIdAndBookId(@Param("loginId") String loginId,
            @Param("bookId") String bookId,@Param("tableName")String tableName);

    @Result(column = "CODE_TYPE", property = "codeType", typeHandler = EnumTypeHandler.class)
    @Select("SELECT SUM(COUNT_TIMES) COUNT_TIMES ,MAX(DATE) DATE,TEXT,MAX(SCORE) SCORE,  USER_RECOGNIZE_BYTES FROM ("
            + " SELECT  COUNT(*) COUNT_TIMES,MAX(CLICK_TIME) DATE,TEXT,MAX(SCORE) SCORE,  USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK_SECOND "
            + " WHERE LOGIN_ID=#{loginId} AND FK_BOOK_ID=#{bookId} AND FUNCTION='2' GROUP BY TEXT "
            + " UNION  SELECT  COUNT(*) COUNT_TIMES,MAX(CLICK_TIME) DATE,TEXT,MAX(SCORE) SCORE, NULL as USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK "
            + " WHERE LOGIN_ID=#{loginId} AND FK_BOOK_ID=#{bookId} AND FUNCTION='2' GROUP BY TEXT"
            + " UNION  SELECT  COUNT(*) COUNT_TIMES,MAX(CLICK_TIME) DATE,TEXT,MAX(SCORE) SCORE, USER_RECOGNIZE_BYTES FROM ${tableName}  "
            + " WHERE LOGIN_ID=#{loginId} AND FK_BOOK_ID=#{bookId} AND FUNCTION='2' GROUP BY TEXT"
            + ") AS  DDB_RECORD_USER_BOOK GROUP BY TEXT")
    List<ActivityStudyDetail> getSpokenDetailByLoginIdAndBookId(@Param("loginId") String loginId,
            @Param("bookId") String bookId,@Param("tableName")String tableName);

    @Result(column = "CODE_TYPE", property = "codeType", typeHandler = EnumTypeHandler.class)
    @Select("SELECT * FROM ("
            + " SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION, "
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE, USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK_SECOND "
            + " WHERE LOGIN_ID=#{loginId} AND CLICK_TIME>=#{startDate} AND CLICK_TIME<#{endDate}  "
            + " UNION SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION,"
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE, NUll as USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK "
            + " WHERE LOGIN_ID=#{loginId} AND CLICK_TIME>=#{startDate} AND CLICK_TIME<#{endDate} "
            + " UNION SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION,"
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE,  USER_RECOGNIZE_BYTES FROM "
            + " ${tableName} WHERE LOGIN_ID=#{loginId} AND CLICK_TIME>=#{startDate} AND CLICK_TIME<#{endDate} "
            + " )AS DDB_RECORD_USER_BOOK  ORDER BY CLICK_TIME DESC")
    List<DdbRecordUserBook> getWeeklyRecord(@Param("loginId") String loginId, @Param("startDate") Date startDate,
        @Param("endDate") Date endDate,@Param("tableName")String tableName);
    
    // TODO此方法因表数据量大，查询不出数据，要在新的数据pipeline中能够处理，查询该数据。
    @Select("SELECT A.NAME NAME,COUNT(*) NUMBER FROM (SELECT B.NAME NAME,U.LOGIN_ID LOGINID FROM "
            + " (SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION, "
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE FROM DDB_RECORD_USER_BOOK_SECOND UNION  "
            + " SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION,"
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE FROM DDB_RECORD_USER_BOOK) U "
        + " JOIN DDB_RESOURCE_BOOK B WHERE U.FK_BOOK_ID=B.ID AND U.CLICK_TIME>=#{startDate} AND U.CLICK_TIME<#{endDate} "
        + " GROUP BY B.NAME,U.LOGIN_ID) AS A GROUP BY A.NAME ORDER BY COUNT(*) DESC LIMIT 10")
    List<BookRanking> getBookRanding(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // TODO此方法因表数据量大，查询不出数据，要在新的数据pipeline中能够处理，查询该数据。
    @Result(column = "CODE_TYPE", property = "codeType", typeHandler = EnumTypeHandler.class)
    @Select("SELECT * FROM (SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION, "
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE, USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK_SECOND "
            + " WHERE CLICK_TIME>=#{startDate} AND CLICK_TIME<#{endDate} UNION "
            + " SELECT  ID, LOGIN_ID, FK_BOOK_ID, CODE, CLICK_TIME, TYPE, IS_READ, END_TIME, VOICE_TYPE, FUNCTION,"
            + " FK_ACTIVITY_ID, PAGE, TEXT, SCORE, TIME, CODE_TYPE, NUll as USER_RECOGNIZE_BYTES FROM DDB_RECORD_USER_BOOK "
            + " WHERE CLICK_TIME>=#{startDate} AND CLICK_TIME<#{endDate} "
            + " )AS DDB_RECORD_USER_BOOK  ORDER BY CLICK_TIME DESC  ")
    List<DdbRecordUserBook> getDailyRecord(@Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);
    //根据loginId删除ddb_record_user_book表数据
    @Result(column = "TYPE", property = "type", typeHandler = ResourceBookTypeHandler.class)
    @Delete("DELETE FROM DDB_RECORD_USER_BOOK WHERE LOGIN_ID=#{loginId} ")
    void deleteOldTable(String loginId);
    //根据loginId删除ddb_record_user_book_second新表中数据
    @Result(column = "TYPE", property = "type", typeHandler = ResourceBookTypeHandler.class)
    @Delete("DELETE FROM DDB_RECORD_USER_BOOK_SECOND WHERE LOGIN_ID=#{loginId} ")
    void delete(String loginId);
    //根据loginId删除ddb_new_record_user_book_shard_i(i=0,1,2,3)新表中数据
    @Result(column = "TYPE", property = "type", typeHandler = ResourceBookTypeHandler.class)
    @Delete("DELETE FROM ${tableName} WHERE LOGIN_ID=#{loginId} ")
    void deleteShard(@Param("loginId")String loginId,@Param("tableName")String tableName);
    
    @Select("SELECT MAX(SCORE) FROM  ${tableName} WHERE LOGIN_ID=#{loginId} AND FK_ACTIVITY_ID=#{activityId} AND TEXT=#{text} AND FUNCTION ='2' AND CLICK_TIME >= #{startDate} AND CLICK_TIME <#{endDate} ")
    Double getMaxScore(@Param("loginId") String loginId, @Param("activityId") String activityId,@Param("text") String text, @Param("startDate") String startDate,
            @Param("endDate") String endDate,@Param("tableName")String tableName);
    
    @Select("SELECT COUNT(*) COUNT_TIMES FROM ${tableName}  WHERE LOGIN_ID=#{loginId} AND FK_ACTIVITY_ID=#{activityId}  AND CLICK_TIME >= #{startDate} AND CLICK_TIME <#{endDate}  ")
    int getCountTimes(@Param("loginId") String loginId, @Param("activityId") String activityId, @Param("startDate") String startDate,
            @Param("endDate") String endDate,@Param("tableName")String tableName);
    
    /**
     * 根据句子text分组查询某人(loginId)某段时间内(startDate-endDate)某些书(bookIds)的口语评测句子数据
     * 查询结果: 句子口语评测次数, 句子最新评测日期, 句子text, 句子最高分数, 句子评测数据详情
     * @param loginId
     * @param bookIds
     * @param startDate
     * @param endDate
     * @param tableName
     * @return
     */
    @Result(column = "CODE_TYPE", property = "codeType", typeHandler = EnumTypeHandler.class)
    @Select({"<script>",
                "SELECT SUM(COUNT_TIMES) COUNT_TIMES ,MAX(DATE) DATE,TEXT,MAX(SCORE) SCORE,  USER_RECOGNIZE_BYTES FROM",
                " ( ",
                    "SELECT  COUNT(*) COUNT_TIMES,MAX(CLICK_TIME) DATE,TEXT,MAX(SCORE) SCORE,  USER_RECOGNIZE_BYTES FROM  ${tableName} ",
                    "WHERE LOGIN_ID=#{loginId} ",
                    "<when test='bookIds!=null'>",
                        " AND FK_BOOK_ID in ",
                        "<foreach item='item' index='index' collection='bookIds'","open='(' separator=',' close=')'>",
                            "#{item}",
                        "</foreach>",
                    "</when>",
                    " AND FUNCTION='2' AND CLICK_TIME &gt;= #{startDate} AND CLICK_TIME &lt; #{endDate} GROUP BY TEXT", 
                " ) ",
                "AS  DDB_RECORD_USER_BOOK GROUP BY TEXT ",
            "</script>"})
    List<ActivityStudyDetail> getSpokenDetailByLoginIdAndBookIds(@Param("loginId")String loginId, @Param("bookIds")List<String> bookIds, @Param("startDate")String startDate,
            @Param("endDate")String endDate,@Param("tableName")String tableName);
    
    /**
     * 根据ActivityId分组查询某人(loginId)某段时间内(startDate-endDate)某些书(bookIds)的课本学习ActivityId的数据
     * 查询结果: ActivityId学习次数, ActivityId学习最新学习日期, ActivityId, 句子TEXT, 总时长 
     * @param loginId
     * @param bookIds
     * @param startDate
     * @param endDate
     * @param tableName
     * @return
     */
    @Result(column = "CODE_TYPE", property = "codeType", typeHandler = EnumTypeHandler.class)
    @Select({"<script>",
                "SELECT SUM(COUNT_TIMES) COUNT_TIMES,MAX(DATE) DATE,FK_ACTIVITY_ID,TEXT,SUM(TIME) TIME FROM",
                " ( ",
                    "SELECT  COUNT(*) COUNT_TIMES,MAX(CLICK_TIME) DATE,FK_ACTIVITY_ID,TEXT,SUM(TIME) TIME, USER_RECOGNIZE_BYTES FROM  ${tableName} ",
                    "WHERE LOGIN_ID=#{loginId} ",
                    "<when test='bookIds!=null'>",
                        "AND FK_BOOK_ID in ",
                        "<foreach item='item' index='index' collection='bookIds' open='(' separator=',' close=')'>",
                            "#{item}",
                        "</foreach>",
                    "</when>",
                    " AND CLICK_TIME &gt;= #{startDate} AND CLICK_TIME &lt; #{endDate} GROUP BY FK_ACTIVITY_ID",
                " ) ",
                "AS DDB_RECORD_USER_BOOK GROUP BY FK_ACTIVITY_ID ",
            "</script>"})
    List<ActivityStudyDetail> getStudyDetailByLoginIdAndBookIds(@Param("loginId")String loginId, @Param("bookIds")List<String> bookIds, @Param("startDate")String startDate,
            @Param("endDate")String endDate,@Param("tableName")String tableName);
}
