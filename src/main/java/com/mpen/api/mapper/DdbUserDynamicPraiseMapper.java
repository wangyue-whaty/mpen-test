package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbUserDynamicPraise;

/**
 * Mapper接口 涉及：动态点赞
 * 
 * @author hzy
 * @since 2018-08-09
 */
@Mapper
public interface DdbUserDynamicPraiseMapper {

    @Select({ "<script>", "SELECT * FROM DDB_USER_DYNAMIC_PRAISE", 
               "<where> ", 
                 "<when test='dynamicIds!=null'>",
                    " fk_dynamic_id in ", 
                         "<foreach item='item' index='index' collection='dynamicIds'","open='(' separator=',' close=')'>", 
                         "#{item}", 
                         "</foreach>", 
                 "</when>", 
             " </where>",
            "</script>" })
    List<DdbUserDynamicPraise> listByDynamicIds(@Param("dynamicIds") List<String> dynamicIds);

    @Select("SELECT * FROM DDB_USER_DYNAMIC_PRAISE WHERE fk_login_id = #{loginId} AND fk_dynamic_id = #{dynamicId}")
    DdbUserDynamicPraise getByDynamicIdLoginId(@Param("loginId") String loginId, @Param("dynamicId") String dynamicId);

    @Insert("INSERT INTO DDB_USER_DYNAMIC_PRAISE (id, fk_dynamic_id, fk_login_id, create_Time_In_Ms, update_Time_In_Ms) "
            + "VALUES (#{id}, #{fkDynamicId}, #{fkLoginId}, #{createTimeInMs}, #{updateTimeInMs})")
    int save(DdbUserDynamicPraise dynamicPraise);

    @Delete("DELETE FROM  DDB_USER_DYNAMIC_PRAISE WHERE FK_DYNAMIC_ID = #{dynamicId}")
    int delete(String dynamicId);

}