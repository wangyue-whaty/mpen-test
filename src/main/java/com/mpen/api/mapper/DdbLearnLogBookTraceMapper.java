package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.bean.StudyCountAssist;
import com.mpen.api.domain.DdbLearnLogBookTrace;

/**
 * 某本书日学习时间轨迹Mapper
 * 涉及：pipeline统计某本书日学情的学习时间轨迹信息sql查询，提供给app端只有查询语句，保存和删除sql只用于单元测试
 */
@Mapper
public interface DdbLearnLogBookTraceMapper {

    @Select("SELECT * FROM DDB_LEARN_LOG_BOOK_TRACE WHERE FK_LOGIN_ID=#{loginId} AND FK_BOOK_ID=#{bookId} ORDER BY STUDY_DATE DESC  ")
    List<DdbLearnLogBookTrace> getByLoginId(@Param("loginId") String loginId, @Param("bookId") String bookId);

    @Insert("INSERT INTO DDB_LEARN_LOG_BOOK_TRACE(ID,FK_LOGIN_ID,FK_BOOK_ID,STUDY_DATE,BOOK_STUDY_TIME,SPOKEN_TEST_TIME,LEARN_PAGE,SPEAK_PAGE,CREATE_DATE,UPDATE_DATE) VALUES (#{id},#{fkLoginId},#{fkBookId},#{studyDate},#{bookStudyTime},#{spokenTestTime},#{learnPage},#{speakPage},#{createDate},#{updateDate})")
    int saveDdbLearnLogBookTrace(DdbLearnLogBookTrace ddbLearnLogBookTrace);

    @Delete("DELETE FROM DDB_LEARN_LOG_BOOK_TRACE WHERE ID=#{id}")
    void delete(String id);
    
    @Insert({"<script>",
        "INSERT INTO ddb_learn_log_book_trace(id,fk_login_id,fk_book_id,study_date,book_study_time,spoken_test_time,learn_page,speak_page,create_date,update_date) ",
        "VALUES ",
        "<when test='logBookTraces!=null'>",
        "<foreach collection='logBookTraces' item='logBookTrace' separator=','>",
            "(#{logBookTrace.id},#{logBookTrace.fkLoginId},#{logBookTrace.fkBookId},#{logBookTrace.studyDate},#{logBookTrace.bookStudyTime},#{logBookTrace.spokenTestTime},#{logBookTrace.learnPage},#{logBookTrace.speakPage},#{logBookTrace.createDate},#{logBookTrace.updateDate})",
        "</foreach>",
        "</when>",
        "ON DUPLICATE KEY UPDATE ", 
        "book_study_time = book_study_time + VALUES(book_study_time),",
        "spoken_test_time = spoken_test_time + VALUES(spoken_test_time),",
        "learn_page =  + VALUES(learn_page),",
        "speak_page =  + VALUES(speak_page),",
        "update_date = VALUES(update_date)",
        "</script>"})
    void batchUpdate(@Param("logBookTraces") List<DdbLearnLogBookTrace> logBookTraces);

    @Select("SELECT learn_page,speak_page FROM ddb_learn_log_book_trace WHERE fk_book_id=#{fkBookId} and study_date=#{studyDate} and fk_login_id=#{fkLoginId} LIMIT 1")
    DdbLearnLogBookTrace getByLoginIdBookIdStudyDate(StudyCountAssist studyCountAssist);

}