package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.domain.DdbUserMedalRecord;

/**
 * 用户勋章Mapper层操作 
 * 涉及：教师端批阅作业以及app2.0相关涉及勋章接口
 *
 */
@Mapper
public interface DdbUserMedalRecordMapper {
    @Select("SELECT * FROM DDB_USER_MEDAL_RECORD WHERE FK_LOGIN_ID=#{loginId}")
    List<DdbUserMedalRecord> getUserMedalRecord(String loginId);

    @Select("SELECT  IFNULL(SUM(MEDAL_NUM),0) FROM DDB_USER_MEDAL_RECORD WHERE FK_LOGIN_ID=#{loginId}")
    int getMedalSum(String loginId);

    @Select("SELECT * FROM DDB_USER_MEDAL_RECORD WHERE FK_LOGIN_ID=#{loginId} AND MEDAL_STATE=2")
    DdbUserMedalRecord getMedalWear(String loginId);

    @Update("UPDATE DDB_USER_MEDAL_RECORD SET MEDAL_STATE=1 WHERE ID=#{id}")
    int updateMedal(DdbUserMedalRecord ddbUserMedalRecod);

    @Update("UPDATE DDB_USER_MEDAL_RECORD SET MEDAL_STATE=2 WHERE FK_LOGIN_ID=#{loginId} AND FK_MEDAL_DIC_ID=#{medalId} ")
    int updateMedalWear(@Param("loginId") String loginId, @Param("medalId") String medalId);

    @Update("UPDATE DDB_USER_MEDAL_RECORD SET MEDAL_STATE=1 WHERE FK_LOGIN_ID=#{loginId} AND FK_MEDAL_DIC_ID=#{medalId} ")
    int RemoveMedal(@Param("loginId") String loginId, @Param("medalId") String medalId);

    @Select("SELECT * FROM DDB_USER_MEDAL_RECORD WHERE FK_LOGIN_ID=#{loginId} AND MEDAL_STATE=1 AND WEAR_STATE=0 ORDER BY CREATE_TIME DESC LIMIT 1")
    DdbUserMedalRecord getRecMedalRecord(String loginId);

    @Select("SELECT * FROM DDB_USER_MEDAL_RECORD WHERE FK_LOGIN_ID=#{loginId} AND FK_MEDAL_DIC_ID=#{medalId} ")
    DdbUserMedalRecord getByLoginIdMedalId(@Param("loginId") String loginId, @Param("medalId") String medalId);

    @Update("UPDATE DDB_USER_MEDAL_RECORD SET MEDAL_NUM = #{medalNum} WHERE ID = #{id}")
    int updateMedalNum(DdbUserMedalRecord ddbUserMedalRecod);

    @Insert("INSERT INTO DDB_USER_MEDAL_RECORD (ID, MEDAL_STATE, CREATE_TIME, FK_LOGIN_ID, WEAR_STATE, FK_MEDAL_DIC_ID, MEDAL_NUM) "
            + "VALUES (#{id}, #{medalState}, #{createTime}, #{fkLoginId}, #{wearState}, #{fkMedalDicId}, #{medalNum})")
    int save(DdbUserMedalRecord medalRecord);

    @Delete("DELETE FROM DDB_USER_MEDAL_RECORD WHERE ID=#{id}")
    int delete(DdbUserMedalRecord ddbUserMedalRecod);

    @Select("SELECT * FROM DDB_USER_MEDAL_RECORD WHERE FK_LOGIN_ID=#{loginId} AND  MEDAL_STATE=2 LIMIT 1 ")
    DdbUserMedalRecord getUserMedal(String loginId);

}
