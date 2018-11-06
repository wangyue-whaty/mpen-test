package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.domain.DdbUserMessage;

/**
 * 消息相关Mapper 涉及： App2.0消息相关模块
 */
@Mapper
public interface DdbUserMessageMapper {

    @Insert("INSERT INTO DDB_USER_MESSAGE(ID,LOGIN_ID,TYPE,CONTENT,IS_READ,IS_DEL,CREATE_TIME,UPDATE_TIME,TYPE_DETAIL) "
            + "VALUES(#{id},#{loginId},#{type},#{content},#{isRead},#{isDel},#{createTime},#{updateTime},#{typeDetail})")
    void saveDdbUserMessage(DdbUserMessage ddbUserMessage);

    // 根据分页信息，用户ID查询当页的未删除的消息列表，没有根据创建时间进行排序
    @Select("SELECT * FROM DDB_USER_MESSAGE WHERE LOGIN_ID = #{loginId} AND IS_DEL = 0 ORDER BY CREATE_TIME DESC  LIMIT #{pageIndex},#{pageSize} ")
    List<DdbUserMessage> findMessageListByLoginId(@Param("loginId") String loginId, @Param("pageIndex") int pageIndex,
            @Param("pageSize") Integer pageSize);

    // 获取用户消息的总数量
    @Select("SELECT COUNT(ID) FROM DDB_USER_MESSAGE WHERE LOGIN_ID = #{loginId}")
    int getTotalCount(@Param("loginId") String loginId);

    // 根据消息ID获取指定消息
    @Select("SELECT * FROM DDB_USER_MESSAGE WHERE  ID = #{messageId}")
    DdbUserMessage findMessageByMessageId(@Param("messageId") String messageId);

    // 根据消息ID更新用户消息是否读取状态
    @Update("UPDATE DDB_USER_MESSAGE SET IS_READ = #{isRead} WHERE ID = #{messageId}")
    int updateUserMessageIsRead(@Param("messageId") String messageId, @Param("isRead") int isRead);

    // 根据消息ID更新用户消息是否删除状态
    @Update("UPDATE DDB_USER_MESSAGE SET IS_DEL = #{isDel} WHERE ID = #{messageId}")
    int updateUserMessageIsDel(@Param("messageId") String messageId, @Param("isDel") int isDel);
}
