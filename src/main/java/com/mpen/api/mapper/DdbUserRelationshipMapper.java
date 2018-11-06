
package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.domain.DdbUserRelationship;

/**
 * 操作好友表Mapper
 * 涉及： APP2.0好友模块相关
 */
@Mapper
public interface DdbUserRelationshipMapper {
	
    @Select("SELECT ID,FRIEND_LOGIN_ID,ALIAS_USER FROM DDB_USER_RELATIONSHIP WHERE USER_LOGIN_ID = #{loginId} AND REL_STATUS = '1' UNION SELECT ID,USER_LOGIN_ID, ALIAS_FRIEND FROM DDB_USER_RELATIONSHIP WHERE FRIEND_LOGIN_ID = #{loginId} AND REL_STATUS = '1' ")
    public List<DdbUserRelationship> getFriends(String loginId);
    // 两人是否是好友
    @Select("SELECT ID,FRIEND_LOGIN_ID,ALIAS_USER FROM DDB_USER_RELATIONSHIP WHERE USER_LOGIN_ID = #{loginId} AND REL_STATUS = '1' AND FRIEND_LOGIN_ID = #{loginId2} UNION SELECT ID,USER_LOGIN_ID, ALIAS_FRIEND FROM DDB_USER_RELATIONSHIP WHERE FRIEND_LOGIN_ID = #{loginId} AND USER_LOGIN_ID = #{loginId2} AND REL_STATUS = '1' ")
    public DdbUserRelationship getFriend(@Param("loginId") String loginId, @Param("loginId2") String loginId2);

    @Insert("INSERT INTO DDB_USER_RELATIONSHIP(id,USER_LOGIN_ID,FRIEND_LOGIN_ID,REL_STATUS,ALIAS_USER,REQ_MSG,create_time) VALUES(#{id},#{userLoginId},#{friendLoginId},0,#{aliasUser},#{reqMsg},#{createTime}) ")
    public int saveFriend(DdbUserRelationship ddbUserRelationship);
    
    // 两人是否互相申请过
    @Select("SELECT ID,FRIEND_LOGIN_ID,ALIAS_USER FROM DDB_USER_RELATIONSHIP WHERE USER_LOGIN_ID = #{loginId} AND FRIEND_LOGIN_ID = #{loginId2} AND REL_STATUS=0   LIMIT 1  UNION SELECT ID,USER_LOGIN_ID, ALIAS_FRIEND FROM DDB_USER_RELATIONSHIP WHERE FRIEND_LOGIN_ID = #{loginId} AND USER_LOGIN_ID = #{loginId2} AND REL_STATUS=0 LIMIT 1")
    public DdbUserRelationship getIsFriend(@Param("loginId") String loginId, @Param("loginId2") String loginId2);
    
    @Update("UPDATE DDB_USER_RELATIONSHIP SET ALIAS_USER = #{remark} WHERE ID = #{id} AND REL_STATUS = '1'")
    public int updateAliasUser(@Param("id") String id, @Param("remark") String remark);
    
    @Update("UPDATE DDB_USER_RELATIONSHIP SET ALIAS_FRIEND = #{remark} WHERE ID = #{id}  AND REL_STATUS = '1'")
    public int updateAliasFriend(@Param("remark") String remark, @Param("id") String id);
    
    @Select("SELECT * FROM DDB_USER_RELATIONSHIP WHERE ID=#{id}")
    public DdbUserRelationship getFriendById(String id);
    
    @Select("SELECT * FROM DDB_USER_RELATIONSHIP WHERE FRIEND_LOGIN_ID=#{loginId} and IS_DEL = 0")
    public List<DdbUserRelationship> getByFriendLoginId(String loginId);
    
    @Select("SELECT COUNT(ID) FROM DDB_USER_RELATIONSHIP WHERE FRIEND_LOGIN_ID = #{loginId} AND REL_STATUS = '0'")
    public int getApplyNum(String loginId);
    
    @Delete("DELETE FROM DDB_USER_RELATIONSHIP WHERE ID = #{id}")
    public int deleteById(String id);
    
    @Select("SELECT * FROM (SELECT FRIEND_LOGIN_ID,ALIAS_USER FROM DDB_USER_RELATIONSHIP WHERE USER_LOGIN_ID = #{loginId} AND REL_STATUS = '1' UNION SELECT USER_LOGIN_ID, ALIAS_FRIEND FROM DDB_USER_RELATIONSHIP WHERE FRIEND_LOGIN_ID = #{loginId} AND REL_STATUS = '1') d  limit #{pageIndex},#{pageSize}")
    public List<DdbUserRelationship> getFriendsPage(@Param("loginId")String loginId,@Param("pageIndex")int pageIndex,@Param("pageSize")int pageSize);
    
    @Update("UPDATE DDB_USER_RELATIONSHIP SET REL_STATUS='1' WHERE ID = #{id}")
    public int passFriend(String id);
    
    @Update("UPDATE DDB_USER_RELATIONSHIP SET IS_DEL= 1 WHERE ID = #{id}")
    public int updateNewFriendById(String id);
    
    @Update("UPDATE DDB_USER_RELATIONSHIP SET ALIAS_USER=#{remark} , REQ_MSG=#{validationMes} WHERE ID=#{id}")
    public int updateUserApplyMsg(@Param("id")String id, @Param("remark")String remark, @Param("validationMes")String validationMes);
}
