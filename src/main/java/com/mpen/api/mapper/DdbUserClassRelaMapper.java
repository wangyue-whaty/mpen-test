package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.domain.DdbUserClassRela;

/**
 * 该mapper主要操作班级-学生关联表，为教师端，app2.0共用的一些新增，查询，更新，删除一些sql语句 
 * @author hzy
 * @since 2018-07-03
 */
@Mapper
public interface DdbUserClassRelaMapper{

    @Insert("INSERT INTO DDB_USER_CLASS_RELA(ID,FK_CLASS_ID,FK_LOGIN_ID,TRUE_NAME,CREATE_TIME,UPDATE_TIME) "
            + "values (#{Id},#{fkClassId},#{fkLoginId},#{trueName},#{createTime},#{updateTime})")
    int create(DdbUserClassRela userClassRela);

   
    @Select("SELECT COUNT(FK_CLASS_ID) FROM DDB_USER_CLASS_RELA WHERE FK_CLASS_ID=#{classId} ")
    int getStudentNums(String classId);
    
    @Select("SELECT FK_LOGIN_ID FROM DDB_USER_CLASS_RELA WHERE FK_CLASS_ID=#{classId} ")
    List<String> getByClassId(String classId);
    
    @Update("DELETE FROM DDB_USER_CLASS_RELA  WHERE ID=#{id} ")
    int deleteById(@Param("id") String id);
    
    @Select("select * from DDB_USER_CLASS_RELA where fk_login_id=#{fkLoginId} ")
    DdbUserClassRela getByLoginId(String fkLoginId);
    
    @Select({"<script>",
        " select * from DDB_USER_CLASS_RELA where "
        + ""," <when test='classIds!=null'>","  FK_CLASS_ID in ","<foreach item='item' index='index' collection='classIds' ",
        " open='(' separator=',' close=')'>","#{item}","</foreach>","</when>",
        " </script>"})
    List<DdbUserClassRela> getDdbUserClassRela(@Param("classIds") List<String> classIds);
    
    @Select("SELECT * FROM DDB_USER_CLASS_RELA WHERE FK_CLASS_ID=#{classId}")
    List<DdbUserClassRela> getDdbUserClassRelaByClassId(@Param("classId") String classId);

    @Delete("DELETE FROM DDB_USER_CLASS_RELA  WHERE FK_CLASS_ID=#{classId} AND fk_login_id=#{loginId}"  )
    void deleteByLoginIdClassId(@Param("loginId")String loginId, @Param("classId")String classId);

    @Select("SELECT COUNT(ID) FROM DDB_USER_CLASS_RELA WHERE FK_CLASS_ID=#{classId}")
    Integer getClassSizeByClassId(@Param("classId") String classId);

}