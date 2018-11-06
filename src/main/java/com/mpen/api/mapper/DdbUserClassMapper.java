package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.domain.DdbUserClass;

/**
 * 该mapper层为教师端和app2.0共用mapper
 * 主要涉及教师，学生对班级表的新增：教师新增为有编号的班级，学生为无编号班级；
 * 以及根据条件的查询，更新，删除等sql语句。
 * @author hzy
 * @since 2018-07-03
 */
@Mapper
public interface DdbUserClassMapper {

    @Select({ " <script>", 
            " SELECT ID FROM DDB_USER_CLASS", " WHERE 1=1", 
            " <when test='loginId!=null'>","AND FK_LOGIN_ID = #{loginId}", "</when>", 
            " <when test='className!=null'>"," AND CLASS_NAME = #{className}", "</when>", 
            " AND CLASS_NUMBER IS NOT NULL </script>" })
    List<String> getClassId(@Param("loginId") String loginId, @Param("className") String className);

    @Select("SELECT DUC.* FROM DDB_USER_CLASS DUC LEFT JOIN DDB_USER_CLASS_RELA DUCR ON (DUCR.FK_CLASS_ID = DUC.ID) WHERE DUCR.FK_LOGIN_ID = #{loginId}")
    DdbUserClass getClassByLoginId(String loginId);

    @Select("SELECT * FROM DDB_USER_CLASS WHERE CLASS_NUMBER = #{classNumber}")
    DdbUserClass getClassByClassNumber(String classNumber);

    @Insert("INSERT INTO DDB_USER_CLASS (ID,CLASS_NAME,ENGLISH_TEACHER,INVITATION_CODE,SCHOOL,FK_LOGIN_ID,CREATE_TIME,UPDATE_TIME,CLASS_NUMBER) VALUES(#{id},#{className},#{englishTeacher},#{invitationCode},#{school},#{fkLoginId},#{createTime},#{updateTime},#{classNumber})")
    int saveClass(DdbUserClass ddbUserClass);

    @Delete("DELETE FROM DDB_USER_CLASS WHERE ID=#{id}")
    int delete(String id);

    @Select("SELECT * FROM DDB_USER_CLASS WHERE FK_LOGIN_ID=#{loginId} and TYPE=0")
    List<DdbUserClass> getByLoginId(String loginId);

    @Select("SELECT ID FROM DDB_USER_CLASS WHERE FK_LOGIN_ID=#{loginId} AND CLASS_NAME=#{className}  ")
    String getFkClassId(@Param("loginId") String loginId, @Param("className") String className);

    @Insert("INSERT INTO DDB_USER_CLASS (ID,CLASS_NAME,ENGLISH_TEACHER,TEXTBOOK_EDITION,INVITATION_CODE,SCHOOL,FK_LOGIN_ID,TYPE,CREATE_TIME,UPDATE_TIME) "
            + "VALUES(#{id},#{className},#{englishTeacher},#{textbookEdition},#{invitationCode},#{school},#{fkLoginId},#{type},#{createTime},#{updateTime})")
    int create(DdbUserClass userClass);

    @Select("SELECT * FROM DDB_USER_CLASS WHERE ID=#{classId}")
    DdbUserClass getByClassId(String classId);

    @Select("SELECT MAX(CLASS_NUMBER) FROM DDB_USER_CLASS ")
    String getClassNumber();

    @Update("UPDATE DDB_USER_CLASS SET CLASS_NAME=#{className} WHERE ID=#{id} ")
    int updateClassInfo(@Param("id") String id, @Param("className") String className);

    @Update("UPDATE DDB_USER_CLASS SET CLASS_NAME=#{className},TEXTBOOK_EDITION=#{textbookEdition},SCHOOL=#{school} WHERE ID=#{id}")
    void update(DdbUserClass ddbUserClass);

}