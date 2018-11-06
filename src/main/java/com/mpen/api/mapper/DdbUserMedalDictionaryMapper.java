package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbUserMedalDictionary;

/**
 * 勋章字典表操作 
 * 涉及：教师端以及app2.0相关接口
 */
@Mapper
public interface DdbUserMedalDictionaryMapper {
    
    @Select("select * from ddb_user_medal_dictionary ")
    List<DdbUserMedalDictionary> getAll();

    @Select("SELECT * FROM DDB_USER_MEDAL_DICTIONARY WHERE MEDAL_TYPE = #{medalType} ")
    DdbUserMedalDictionary getByMedalType(String medalType);
    
    @Insert("INSERT INTO DDB_USER_MEDAL_DICTIONARY (ID, MEDAL_TYPE, MEDAL_NAME, MEDAL_DYNAMIC, CREATE_TIME, UPDATE_TIME,SLOGAN, MEDAL_RULE) "
            + "VALUES (#{id}, #{medalType}, #{medalName}, #{medalDynamic}, #{createTime}, #{updateTime},#{slogan}, #{medalRule})")
    int save(DdbUserMedalDictionary ddbUserMedalDictionary);
    
    @Delete("DELETE FROM DDB_USER_MEDAL_DICTIONARY WHERE ID=#{id}")
    int delete(DdbUserMedalDictionary ddbUserMedalDictionary);
}
