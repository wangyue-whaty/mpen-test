package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbUserDynamicRecord;

/**
 * 用户动态记录Mapper
 * 涉及：教师端以及app2.0相关
 * @author hzy
 * @since 2018-08-09
 */
@Mapper
public interface DdbUserDynamicRecordMapper {

    @Select({ "<script>", "SELECT COUNT(ID) FROM DDB_USER_DYNAMIC_RECORD",
            "<where> ", 
            "<when test='loginIds!=null'> ",
            "AND fk_login_id in ", 
            "<foreach item='item' index='index' collection='loginIds' ",
            "open='(' separator=',' close=')'>", "#{item}", "</foreach>", "</when> ",
            " </where> ", 
            "</script>" })
    int getTotalCount(@Param("loginIds") List<String> loginIds);

    @Select({ "<script>", "SELECT * FROM DDB_USER_DYNAMIC_RECORD ",
            "<where>",
            "<when test='loginIds!=null'> ",
            "AND fk_login_id in ", "<foreach item='item' index='index' collection='loginIds' ",
            "open='(' separator=',' close=')'>", "#{item}", "</foreach> ", 
            "</when> ",
            " ORDER BY CREATE_TIME DESC limit #{pageIndex},#{pageSize} "
          + " </where>", 
            " </script>" })
    List<DdbUserDynamicRecord> listDynamics(@Param("pageIndex") int pageIndex, @Param("pageSize") Integer pageSize,
            @Param("loginIds") List<String> loginIds);

    @Insert("INSERT INTO ddb_user_dynamic_record (id, fk_login_id, create_time, update_time, dynamic_content) "
            + "VALUES (#{id}, #{fkLoginId}, #{createTime}, #{updateTime}, #{dynamicContent})")
    void save(DdbUserDynamicRecord dynamicRecord);

    @Select("SELECT ID,FK_LOGIN_ID,CREATE_TIME,UPDATE_TIME,DYNAMIC_CONTENT FROM DDB_USER_DYNAMIC_RECORD WHERE ID = #{id}")
    DdbUserDynamicRecord getById(String id);
}