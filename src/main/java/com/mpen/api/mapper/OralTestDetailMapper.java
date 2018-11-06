/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.domain.OralTestDetail;

/**
 * AppMapper对象.
 * 
 * @author zyt
 *
 */
@Mapper
public interface OralTestDetailMapper {
    @Insert("INSERT INTO ORAL_TEST_DETAIL (LOGIN_ID,PEN_ID,FK_BOOK_ID,NUM,UPLOAD_TIME,RECORDING_URL,ANSWER_PEN_TIME,IS_DEAL,SHARD_NUM,ASSESSMENT_TYPE) VALUES "
        + "(#{loginId},#{penId},#{fkBookId},#{num},#{uploadTime},#{recordingUrl},#{answerPenTime},#{isDeal},#{shardNum},#{assessmentType})")
    void save(OralTestDetail detail);

    @Update("UPDATE ORAL_TEST_DETAIL SET RECOGNIZE_TXT=#{recognizeTxt},SCORE=#{score},IS_DEAL=#{isDeal}, FLUENCY=#{fluency},INTEGRITY=#{integrity},PRONUNCIATION=#{pronunciation} WHERE ID=#{id}")
    void update(OralTestDetail detail);

    @Select("SELECT * FROM ORAL_TEST_DETAIL WHERE LOGIN_ID=#{loginId} AND FK_BOOK_ID=#{bookId} AND NUM=#{num} AND ASSESSMENT_TYPE=#{assessmentType} ORDER BY NUM DESC LIMIT 1")
    OralTestDetail get(@Param("loginId") String loginId, @Param("bookId") String bookId, @Param("num") int num,@Param("assessmentType") int assessmentType);

    @Select("SELECT * FROM ORAL_TEST_DETAIL WHERE SHARD_NUM=#{shardNum} AND IS_DEAL=0 AND TIMES<=3 AND ASSESSMENT_TYPE=#{assessmentType}")
    List<OralTestDetail> getNotDeal(@Param("shardNum") int shardNum,@Param("assessmentType") int assessmentType);
    
    @Insert("UPDATE ORAL_TEST_DETAIL SET TIMES=#{times} WHERE ID=#{id} ")
    void updateTimes(@Param("times") int times,@Param("id") int id);
    
    // sum(score)有可能null ,解决:IFNULL(SUM(SCORE), 0.0)
    @Select("SELECT  IFNULL(SUM(SCORE), 0.0) FROM ORAL_TEST_DETAIL WHERE LOGIN_ID=#{loginId}  AND FK_BOOK_ID=#{bookId} AND ASSESSMENT_TYPE=#{assessmentType} ")
    double getExamSumByLoginIdAndBookId(@Param("loginId") String loginId, @Param("bookId") String bookId,@Param("assessmentType") int assessmentType);
    
    @Select("SELECT COUNT(*) FROM ORAL_TEST_DETAIL WHERE LOGIN_ID=#{loginId}  AND FK_BOOK_ID=#{bookId} AND SERIAL_NUMBER=#{serialNumber} AND ASSESSMENT_TYPE=#{assessmentType} ")
    int getOralTestPaperCount(@Param("loginId") String loginId, @Param("bookId") String bookId,@Param("serialNumber") int serialNumber,@Param("assessmentType") int assessmentType);
     
    @Select("SELECT COUNT(*) FROM ORAL_TEST_DETAIL WHERE LOGIN_ID=#{loginId}  AND FK_BOOK_ID=#{bookId} AND SERIAL_NUMBER=#{serialNumber} AND ASSESSMENT_TYPE=#{assessmentType} and UPLOAD_TIME>#{startTime} and UPLOAD_TIME<#{endTime}")
    int getHomeWorkOralTestPaperCount(@Param("loginId") String loginId, @Param("bookId") String bookId,@Param("serialNumber") int serialNumber,@Param("assessmentType") int assessmentType, @Param("startTime") String startTime, @Param("endTime") String endTime);
    
    // sum(score)有可能null ,解决:IFNULL(SUM(SCORE), 0.0)
    @Select("SELECT IFNULL(SUM(SCORE), 0.0) FROM ORAL_TEST_DETAIL WHERE LOGIN_ID=#{loginId}  AND FK_BOOK_ID=#{bookId} AND SERIAL_NUMBER=#{serialNumber} AND ASSESSMENT_TYPE=#{assessmentType} ")
    double getOralTestPaperSum(@Param("loginId") String loginId, @Param("bookId") String bookId,@Param("serialNumber") int serialNumber,@Param("assessmentType") int assessmentType);
   
    @Delete("DELETE FROM ORAL_TEST_DETAIL WHERE LOGIN_ID=#{loginId}  AND FK_BOOK_ID=#{bookId} AND NUM=#{num} AND SERIAL_NUMBER=1 AND ASSESSMENT_TYPE=#{assessmentType}")
    int deleteData(@Param("loginId") String loginId, @Param("bookId") String bookId,@Param("num") int num,@Param("assessmentType") int assessmentType);
    
    @Select("SELECT COUNT(NUM) from ((SELECT DISTINCT(SERIAL_NUMBER) AS NUM FROM ORAL_TEST_DETAIL WHERE LOGIN_ID=#{loginId}  AND FK_BOOK_ID=#{bookId} AND ASSESSMENT_TYPE=#{assessmentType} GROUP BY SERIAL_NUMBER) as EXAMNUM)")
    int getExamCountByLoginIdAndBookId(@Param("loginId") String loginld,@Param("bookId") String bookId,@Param("assessmentType") int assessmentType);
    
    @Select({"<script>",
            " select ID,LOGIN_ID,PEN_ID,FK_BOOK_ID,NUM,UPLOAD_TIME,RECORDING_URL,SCORE,ANSWER_PEN_TIME,SHARD_NUM,IS_DEAL,"
          + " TIMES,SERIAL_NUMBER,ASSESSMENT_TYPE,max(score) maxScore,min(score) minScore,avg(score) avgScore ,avg(FLUENCY) avgFluency,avg(INTEGRITY) avgIntegrity,avg(PRONUNCIATION) avgPronunciation  "
          + " from ORAL_TEST_DETAIL where ",
            " FK_BOOK_ID=#{bookId} AND NUM=#{num} AND ASSESSMENT_TYPE=#{assessmentType} "
          + " <when test='loginIds!=null'>"," AND  LOGIN_ID in ",
            " <foreach item='item' index='index' collection='loginIds' open='(' separator=',' close=')'>","#{item}","</foreach>",
            " </when> "
          + " ORDER BY NUM DESC LIMIT 1"
          + " </script>"})
    OralTestDetail getDetail(@Param("loginIds")  List<String> loginIds, @Param("bookId") String bookId, @Param("num") int num,@Param("assessmentType")  int assessmentType);
}
