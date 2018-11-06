package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.bean.Student;
import com.mpen.api.domain.DdbUserHomeworkState;

/**
 * 该mapper操作学生作业状态表，为教师端和app2.0共用的新增，查询，更新，删除的一些sql，
 * 主要对学生作业状态，老师批阅状态的一些sql操作语句。
 * @author hzy
 * @since 2018-07-05
 */
@Mapper
public interface DdbUserHomeworkStateMapper{

    @Insert("INSERT INTO DDB_USER_HOMEWORK_STATE(ID,FK_CLASS_ID,FK_LOGIN_ID,FK_HOMEWORK_ID,CONTENT,IS_COMMIT,"
            + "IS_MARKING,UPLOAD_TIME,REMARK,RESOURCE_URL,REWARD_NUM,CREATE_TIME,UPDATE_TIME) VALUES (#{id},"
            + "#{fkClassId},#{fkLoginId},#{fkHomeworkId},#{content},#{isCommit},#{isMarking},#{uploadTime},"
            + "#{remark},#{resourceUrl},#{rewardNum},#{createTime},#{updateTime})")
    int save(DdbUserHomeworkState homeworkState);
    @Select("SELECT COUNT(ID) FROM DDB_USER_HOMEWORK_STATE WHERE FK_HOMEWORK_ID=#{homeWorkId} AND IS_MARKING=1")
    int getHasReviewNum(String homeWorkId);

    @Select("SELECT COUNT(ID) FROM DDB_USER_HOMEWORK_STATE WHERE FK_HOMEWORK_ID=#{homeWorkId} AND IS_MARKING=0 ")
    int getHasNoReviewNum(String homeWorkId);

    @Select("SELECT COUNT(DUCR.FK_LOGIN_ID) FROM DDB_USER_CLASS_RELA DUCR LEFT "
            + "JOIN DDB_USER_HOMEWORK DUH ON ( DUH.FK_CLASS_ID = DUCR.FK_CLASS_ID) LEFT "
            + "JOIN DDB_USER_HOMEWORK_STATE DUHS ON (DUHS.FK_HOMEWORK_ID = DUH.ID AND "
            + "DUHS.FK_CLASS_ID = DUCR.FK_CLASS_ID AND DUCR.FK_LOGIN_ID = DUHS.FK_LOGIN_ID ) "
            + "WHERE DUH.ID = #{homeWorkId} AND DUHS.ID IS NULL")
    int getNoSubmitNum(String homeWorkId);

    @Select("SELECT * FROM DDB_USER_HOMEWORK_STATE WHERE FK_HOMEWORK_ID=#{id} and IS_MARKING=#{type} ORDER BY CREATE_TIME ")
    List<DdbUserHomeworkState> getByBookIdAndType(@Param("id") String id, @Param("type") String reviewType);

    @Update("update DDB_USER_HOMEWORK_STATE set REMARK=#{remark},REWARD_NUM=#{rewardNum},"
            + "IS_MARKING=1,AUDIO_REMARK_URLS=#{audioRemarkUrls},UPDATE_TIME=#{updateTime} where FK_LOGIN_ID=#{fkLoginId} and "
            + "FK_HOMEWORK_ID=#{fkHomeworkId} ")
    int updateStudentHomeWork(DdbUserHomeworkState ddbUserHomeworkState);

    @Select("SELECT * FROM DDB_USER_HOMEWORK_STATE WHERE FK_LOGIN_ID=#{loginId} AND FK_HOMEWORK_ID=#{homeworkId} ")
    DdbUserHomeworkState getSudentHomeworkDetails(@Param("loginId") String loginId,@Param("homeworkId") String homeworkId);
    
    @Select("SELECT * FROM DDB_USER_HOMEWORK_STATE WHERE FK_LOGIN_ID=#{loginId} AND FK_HOMEWORK_ID=#{homeworkId} and IS_MARKING=0   ")
    DdbUserHomeworkState getHomework(@Param("loginId") String loginId,@Param("homeworkId") String homeworkId);
    
    @Select("SELECT FK_LOGIN_ID FROM DDB_USER_HOMEWORK_STATE WHERE FK_HOMEWORK_ID=#{homeworkId}")
    List<String> getLoginId(String homeWorkId);
    
    @Select("SELECT COUNT(ID) FROM DDB_USER_HOMEWORK_STATE WHERE FK_HOMEWORK_ID=#{homeWorkId}")
    int getSubmitNum(String id);
    
    @Select({"<script>",
        " SELECT * FROM DDB_USER_HOMEWORK_STATE WHERE 1=1",
        " <when test='workIds!=null'>","AND FK_HOMEWORK_ID IN ","<foreach item='item' index='index' collection='workIds'",
        "open='(' separator=',' close=')'>","#{item}","</foreach>","</when>"," "
         + " AND FK_LOGIN_ID=#{loginId}","</script>"})
    List<DdbUserHomeworkState> listByLoginId(@Param("workIds") List<String> workIds, @Param("loginId") String loginId);
    
    @Select("SELECT * FROM DDB_USER_HOMEWORK_STATE WHERE FK_HOMEWORK_ID=#{workId} AND FK_LOGIN_ID=#{loginId}")
    DdbUserHomeworkState getByWorkId(@Param("workId") String workId, @Param("loginId") String loginId);
    
    @Select("SELECT duhs.* FROM DDB_USER_HOMEWORK_STATE duhs left join ddb_user_homework duh on (duhs.FK_HOMEWORK_ID = duh.ID)"
            + "WHERE duhs.ID!=#{id} AND duhs.FK_LOGIN_ID=#{loginId} AND duh.TYPE=#{type} AND UPLOAD_TIME LIKE CONCAT(#{uploadTime},'%') LIMIT 1")
    DdbUserHomeworkState getByLoginIdDay(@Param("loginId")String loginId, @Param("type")int type, @Param("uploadTime") String uploadTime,@Param("id") String id);

}