package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbRecordExamDetail;

@Mapper
public interface RecordExamDetailMapper {

    /**
     * 保存阅读分级考试详情
     * 
     * @param ddbRecordExamDetail
     */
    @Insert("INSERT INTO DDB_RECORD_EXAM_DETAIL (EXAM_ID,LOGIN_ID,CREATE_TIME,"
		        + "DURATION,LEVEL) VALUES (#{examId},#{loginId},#{createTime},#{duration},#{level})")
    void save(DdbRecordExamDetail ddbRecordExamDetail);
    
    @Select("SELECT * FROM DDB_RECORD_EXAM_DETAIL WHERE LOGIN_ID=#{loginId} ORDER BY CREATE_TIME ASC")
    List<DdbRecordExamDetail> getExamDetail(@Param("loginId") String loginId);
    
    @Delete("DELETE FROM DDB_RECORD_EXAM_DETAIL WHERE EXAM_ID=#{examId}")
    void deleteExamDetailByID(String examId);
    
    /**
     * 获取最大等级
     * 
     * @author hzy
     * @param loginId
     * @return
     */
    @Select("SELECT MAX(LEVEl) FROM DDB_RECORD_EXAM_DETAIL WHERE LOGIN_ID=#{loginId}")
    Integer getReadLevel(@Param("loginId") String loginId);
}
