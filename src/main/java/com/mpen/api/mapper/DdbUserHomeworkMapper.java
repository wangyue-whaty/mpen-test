package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.bean.ClassAssignments;
import com.mpen.api.bean.HomeWorks;
import com.mpen.api.bean.Student;
import com.mpen.api.domain.DdbUserHomework;
/**
 * 该mapper操作学生作业表，主要教师端，app2.0共用的一些sql新增，查询，更新，删除；
 * 另外，与学生作业状态表进行关联，查询出学生作业的学习信息。
 */
@Mapper
public interface DdbUserHomeworkMapper {
    @Insert("INSERT INTO DDB_USER_HOMEWORK (ID,TYPE,CONTENT,FK_CLASS_ID,END_DATE,CREATE_DATE,REMARK,FK_LOGIN_ID) "
            + " VALUES(#{id},#{type},#{content},#{fkClassId},#{endDate},#{createDate},#{remark},#{fkLoginId})")
    public int save(DdbUserHomework ddbUserHomework);
    
    @Select({"<script>","SELECT duh.*FROM DDB_USER_HOMEWORK duh ","WHERE 1=1",
            "<when test='type!=null'>","AND duh.type = #{type}","</when>",
            "<when test='fkClassId!=null'>","AND duh.FK_CLASS_ID = #{fkClassId}","</when>",
            "<when test='endDate!=null'>","AND duh.CREATE_DATE &lt;= #{endDate}","</when>",
            "<when test='startDate!=null'>","AND duh.CREATE_DATE &lt;= #{startDate}","</when>",
            " ORDER BY duh.CREATE_DATE DESC limit #{pageIndex},#{pageSize} </script>"})
    public List<DdbUserHomework> listHomeWorks(HomeWorks homeWorks);

    @Select({"<script>","SELECT count(ID) FROM DDB_USER_HOMEWORK","WHERE 1=1",
            "<when test='type!=null'>","AND type = #{type}","</when>",
            "<when test='fkClassId!=null'>","AND FK_CLASS_ID = #{fkClassId}","</when>",
            "<when test='endDate!=null'>","AND CREATE_DATE &lt;= #{endDate}","</when>",
            "<when test='startDate!=null'>","AND CREATE_DATE &lt;= #{startDate}","</when>",
            " ORDER BY CREATE_DATE </script>"})
    public int getTotalCount(HomeWorks homeWorks);

    @Select("SELECT * FROM DDB_USER_HOMEWORK WHERE ID = #{id}")
    public DdbUserHomework getById(@Param("id") String id);

    @Select("SELECT * FROM DDB_USER_HOMEWORK WHERE FK_LOGIN_ID=#{loginId} AND FK_CLASS_ID=#{classId} ORDER BY CREATE_DATE DESC")
    public List<DdbUserHomework> getHomeWorks(@Param("loginId") String loginId, @Param("classId") String classId);

    @Select("SELECT * FROM DDB_USER_HOMEWORK WHERE ID=#{id}  ")
    public DdbUserHomework getHomeWork(ClassAssignments classAssignments);

    @Select("SELECT DUCR.FK_LOGIN_ID LOGIN_ID , DUCR.TRUE_NAME AS NAME FROM DDB_USER_CLASS_RELA DUCR LEFT "
            + "JOIN DDB_USER_HOMEWORK DUH ON ( DUH.FK_CLASS_ID = DUCR.FK_CLASS_ID) LEFT "
            + "JOIN DDB_USER_HOMEWORK_STATE DUHS ON (DUHS.FK_HOMEWORK_ID = DUH.ID AND "
            + "DUHS.FK_CLASS_ID = DUCR.FK_CLASS_ID AND DUCR.FK_LOGIN_ID = DUHS.FK_LOGIN_ID ) "
            + "WHERE DUH.ID = #{id} AND DUHS.ID IS NULL")
    List<Student> getStudentList(String id);

    @Select("SELECT DUCR.FK_LOGIN_ID LOGIN_ID , DUCR.TRUE_NAME AS NAME,DUHS.CREATE_TIME AS SUBMITDATE FROM DDB_USER_CLASS_RELA DUCR LEFT "
            + "JOIN DDB_USER_HOMEWORK DUH ON ( DUH.FK_CLASS_ID = DUCR.FK_CLASS_ID) LEFT "
            + "JOIN DDB_USER_HOMEWORK_STATE DUHS ON (DUHS.FK_HOMEWORK_ID = DUH.ID AND "
            + "DUHS.FK_CLASS_ID = DUCR.FK_CLASS_ID AND DUCR.FK_LOGIN_ID = DUHS.FK_LOGIN_ID ) "
            + "WHERE DUH.ID = #{id} AND DUHS.ID IS NOT NULL")
    List<Student> getSubStudentList(String id);
   
    @Delete(value = { "DELETE FROM  DDB_USER_HOMEWORK WHERE ID=#{id}" })
    int delete(String id);
}
