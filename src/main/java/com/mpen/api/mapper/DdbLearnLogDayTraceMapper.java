package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbLearnLogDayTrace;

/**
 * 用户学情每日轨迹统计Mapper
 * 涉及：pipeline统计用户学情每日学习时间轨迹信息sql查询,提供给app端只有查询语句，保存和删除sql只用于单元测试
 */
@Mapper
public interface DdbLearnLogDayTraceMapper {
    
    @Select("SELECT COUNT(*) FROM DDB_LEARN_LOG_DAY_TRACE WHERE FK_LOGIN_ID=#{loginId}")
    Integer getCountByLoginId(String loginId);
    
    @Select("SELECT * FROM DDB_LEARN_LOG_DAY_TRACE WHERE FK_LOGIN_ID=#{loginId} AND MONTH(STUDY_DATE)=#{month} AND YEAR(STUDY_DATE)=#{year}")
    List<DdbLearnLogDayTrace> getByLoginAndDate(@Param("loginId")String loginId, @Param("year")String year,@Param("month")String month );
    
    @Insert("INSERT INTO DDB_LEARN_LOG_DAY_TRACE(ID,FK_LOGIN_ID,STUDY_DATE,COUNT_TIME,book_study_time,SPOKEN_TEST_TIME,EXERCISES_TIME,READ_TIME,OTHER_TIME,CREATE_DATE,UPDATE_DATE) VALUES (#{id},#{fkLoginId},#{studyDate},#{countTime},#{bookStudyTime},#{spokenTestTime},#{exercisesTime},#{readTime},#{otherTime},#{createDate},#{updateDate})")
    int saveDdbLearnLogDayTrace(DdbLearnLogDayTrace ddbLearnLogDayTrace);
    
    @Delete("DELETE FROM DDB_LEARN_LOG_DAY_TRACE WHERE ID=#{id}")
    void delete(String id);
    
    @Insert({"<script>",
        "INSERT INTO ddb_learn_log_day_trace(id,fk_login_id,study_date,count_time,book_study_time,spoken_test_time,exercises_time,read_time,other_time,create_date,update_date) ",
        "VALUES ",
        "<when test='logDayTraces!=null'>",
        "<foreach collection='logDayTraces' item='logDayTrace' separator=','>",
            "(#{logDayTrace.id},#{logDayTrace.fkLoginId},#{logDayTrace.studyDate},#{logDayTrace.countTime},#{logDayTrace.bookStudyTime},#{logDayTrace.spokenTestTime},#{logDayTrace.exercisesTime},#{logDayTrace.readTime},#{logDayTrace.otherTime},#{logDayTrace.createDate},#{logDayTrace.updateDate})",
        "</foreach>",
        "</when>",
        "ON DUPLICATE KEY UPDATE ", 
        "count_time = count_time + VALUES(count_time),",
        "book_study_time = book_study_time + VALUES(book_study_time),",
        "spoken_test_time = spoken_test_time + VALUES(spoken_test_time),",
        "exercises_time = exercises_time + VALUES(exercises_time),",
        "read_time = read_time + VALUES(read_time),",
        "other_time = other_time + VALUES(other_time),",
        "update_date = VALUES(update_date)",
        "</script>"})
    void batchUpdate(@Param("logDayTraces") List<DdbLearnLogDayTrace> logDayTraces);
}
