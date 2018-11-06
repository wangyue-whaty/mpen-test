package com.mpen.api.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbUserPraiseRelationship;

/**
 * 用户点赞记录Mapper 
 * 涉及 ：教师端积分排行以及app2.0积分相关模块
 *
 */
@Mapper
public interface DdbUserPraiseRelationshipMapper {
    
    @Select("SELECT COUNT(*) FROM DDB_USER_PRAISE_RELATIONSHIP WHERE BY_PRAISE_LOGIN_ID=#{fkLoginId} AND CREATE_TIME>#{beforeDate} AND CREATE_TIME<#{todayDate}")
    int getPraiseCount(@Param("fkLoginId") String fkLoginId, @Param("beforeDate") String beforeDate,
            @Param("todayDate") String todayDate);

    @Insert("INSERT INTO  DDB_USER_PRAISE_RELATIONSHIP(ID,PRAISE_LOGIN_ID,BY_PRAISE_LOGIN_ID,CREATE_TIME) VALUES(#{id},#{praiseLoginId},#{byPraiseLoginId},#{createTime})")
    int save(DdbUserPraiseRelationship ddbUserPraiseRelationship);

    @Select("SELECT COUNT(*) FROM DDB_USER_PRAISE_RELATIONSHIP WHERE PRAISE_LOGIN_ID=#{fkLoginId} AND BY_PRAISE_LOGIN_ID=#{byPraiseLoginId} AND CREATE_TIME>#{beforeDate} AND CREATE_TIME<#{todayDate}")
    int getPraisesState(@Param("fkLoginId") String fkLoginId,@Param("byPraiseLoginId") String byPraiseLoginId, @Param("beforeDate") String beforeDate,
            @Param("todayDate") String todayDate);
    
    @Select("SELECT COUNT(*) FROM DDB_USER_PRAISE_RELATIONSHIP WHERE PRAISE_LOGIN_ID=#{praiseLoginId} AND BY_PRAISE_LOGIN_ID=#{byPraiseLoginId} AND CREATE_TIME>#{beforeDate} AND CREATE_TIME<#{todayDate}")
    int getPraiseCountByCurrent(@Param("praiseLoginId")String praiseLoginId, @Param("byPraiseLoginId")String byPraiseLoginId, @Param("beforeDate")String beforeDate, @Param("todayDate")String todayDate);

}
