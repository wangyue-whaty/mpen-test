package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbLearnLogBookSumTrace;

/**
 * 用户某本书书籍学情学习总时间，最近点读时间 Mapper
 * 涉及：pipeline统计某本书学情的学习总时间，最近点读时间信息sql查询，提供给app端只有查询语句，保存和删除sql只用于单元测试
 */
@Mapper
public interface DdbLearnLogBookSumTraceMapper {

    @Select("SELECT * FROM DDB_LEARN_LOG_BOOK_SUM_TRACE WHERE FK_LOGIN_ID=#{loginId}")
    List<DdbLearnLogBookSumTrace> getByloginId(String loginId);

    @Select("SELECT * FROM DDB_LEARN_LOG_BOOK_SUM_TRACE WHERE FK_LOGIN_ID=#{loginId} AND FK_BOOK_ID=#{bookId}")
    DdbLearnLogBookSumTrace getByloginIdAndBookId(@Param("loginId") String loginId, @Param("bookId") String bookId);

    @Insert("INSERT INTO DDB_LEARN_LOG_BOOK_SUM_TRACE(ID,FK_LOGIN_ID,FK_BOOK_ID,SUM_TIME,SPOKEN_TEST_TIME,LATEST_DATE,CREATE_DATE,UPDATE_DATE) VALUES (#{id},#{fkLoginId},#{fkBookId},#{sumTime},#{spokenTestTime},#{latestDate},#{createDate},#{updateDate})")
    int savedDdbLearnLogBookSumTrace(DdbLearnLogBookSumTrace ddbLearnLogBookSumTrace);

    @Delete("DELETE FROM DDB_LEARN_LOG_BOOK_SUM_TRACE WHERE ID=#{id}")
    void delete(String id);
    
    @Insert({"<script>",
        "INSERT INTO ddb_learn_log_book_sum_trace(id,fk_login_id,fk_book_id,sum_time,spoken_test_time,latest_date,create_date,update_date) ",
        "VALUES ",
        "<when test='logBookSumTraces!=null'>",
        "<foreach collection='logBookSumTraces' item='logBookSumTrace' separator=','>",
            "(#{logBookSumTrace.id}, #{logBookSumTrace.fkLoginId}, #{logBookSumTrace.fkBookId}, #{logBookSumTrace.sumTime}, #{logBookSumTrace.spokenTestTime}, #{logBookSumTrace.latestDate}, #{logBookSumTrace.createDate}, #{logBookSumTrace.updateDate})",
        "</foreach>",
        "</when>",
        "ON DUPLICATE KEY UPDATE ", 
        "sum_time = sum_time + VALUES(sum_time),",
        "spoken_test_time = spoken_test_time + VALUES(spoken_test_time),",
        "latest_date = CASE WHEN latest_date &lt; VALUES(latest_date) THEN VALUES(latest_date) ELSE latest_date END,",
        "update_date = VALUES(update_date)",
        "</script>"})
    void batchUpdate(@Param("logBookSumTraces") List<DdbLearnLogBookSumTrace> logBookSumTraces);

}