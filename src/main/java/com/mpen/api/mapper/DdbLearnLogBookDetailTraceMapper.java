package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbLearnLogBookDetailTrace;

/**
 * 某本书学习内容详情页表轨迹 Mapper
 * 涉及：pipeline统计某本书学情的口语评测，课本点读信息sql查询，提供给app端只有查询语句，保存和删除sql只用于单元测试
 * 
 */
@Mapper
public interface DdbLearnLogBookDetailTraceMapper {
    
    @Select("SELECT * FROM DDB_LEARN_LOG_BOOK_DETAIL_TRACE WHERE FK_LOGIN_ID=#{loginId} AND FK_BOOK_ID=#{bookId} AND TYPE=2")
    List<DdbLearnLogBookDetailTrace> getOralLearnDetail(@Param("loginId")String loginId, @Param("bookId")String bookId);
    
    @Select("SELECT * FROM DDB_LEARN_LOG_BOOK_DETAIL_TRACE WHERE FK_LOGIN_ID=#{loginId} AND FK_BOOK_ID=#{bookId} AND TYPE=0")
    List<DdbLearnLogBookDetailTrace> getReadLearnDetail(@Param("loginId")String loginId, @Param("bookId")String bookId);
    
    @Insert("INSERT INTO DDB_LEARN_LOG_BOOK_DETAIL_TRACE(ID,FK_LOGIN_ID,FK_BOOK_ID,FK_ACTIVITY_ID,TEXT_MD5,TYPE,TEXT,TIME,NUMBER,SCORE,USER_RECOGNIZE_BYTES,LATEST_DATE,CREATE_DATE,UPDATE_DATE) VALUES (#{id},#{fkLoginId},#{fkBookId},#{fkActivityId},#{textMd5},#{type},#{text},#{time},#{number},#{score},#{userRecognizeBytes},#{latestDate},#{createDate},#{updateDate})")
    int saveDdbLearnLogBookDetailTrace(DdbLearnLogBookDetailTrace ddbLearnLogBookDetailTrace);
    
    @Delete("DELETE FROM DDB_LEARN_LOG_BOOK_DETAIL_TRACE where ID=#{id}")
    void  deleteById(String id);
    
    @Insert({"<script>",
        "INSERT INTO ddb_learn_log_book_detail_trace(id,fk_login_id,fk_book_id,fk_activity_id,text_md5,type,text,time,number,score,USER_RECOGNIZE_BYTES,latest_date,create_date,update_date) ",
        "VALUES ",
        "<when test='logBookDetailTraces!=null'>",
        "<foreach collection='logBookDetailTraces' item='logBookDetailTrace' separator=','>",
            "(#{logBookDetailTrace.id},#{logBookDetailTrace.fkLoginId},#{logBookDetailTrace.fkBookId},#{logBookDetailTrace.fkActivityId},#{logBookDetailTrace.textMd5},#{logBookDetailTrace.type},#{logBookDetailTrace.text},#{logBookDetailTrace.time},#{logBookDetailTrace.number},#{logBookDetailTrace.score},#{logBookDetailTrace.userRecognizeBytes},#{logBookDetailTrace.latestDate},#{logBookDetailTrace.createDate},#{logBookDetailTrace.updateDate})",
        "</foreach>",
        "</when>",
        "ON DUPLICATE KEY UPDATE ", 
        "time = time + VALUES(time),",
        "number = number + VALUES(number),",
        "USER_RECOGNIZE_BYTES = CASE WHEN score &lt; VALUES(score) THEN VALUES(USER_RECOGNIZE_BYTES) ELSE USER_RECOGNIZE_BYTES END,",
        "score = CASE WHEN score &lt; VALUES(score) THEN VALUES(score) ELSE score END,",
        "latest_date = CASE WHEN latest_date &lt; VALUES(latest_date) THEN VALUES(latest_date) ELSE latest_date END,",
        "update_date = VALUES(update_date)",
        "</script>"})
    void batchUpdate(@Param("logBookDetailTraces") List<DdbLearnLogBookDetailTrace> logBookDetailTraces);
    
}
