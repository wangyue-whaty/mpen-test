package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.domain.DdbUserIntegral;
import com.mpen.api.domain.DdbUserIntegralRecord;

/**
 * 用户积分记录Mapper层 
 * 涉及：教师端积分排行以及app2.0积分相关
 */
@Mapper
public interface DdbUserIntegralRecordMapper {
    // 获取全部积分用户积分
    @Select("SELECT * FROM DDB_USER_INTEGRAL_RECORD WHERE FK_LOGIN_ID=#{loginId} ORDER BY CREATE_TIME DESC LIMIT #{pageIndex},#{pageSize}")
    public List<DdbUserIntegralRecord> getAll(@Param("loginId") String loginId, @Param("pageIndex") int pageIndex,
            @Param("pageSize") int pageSize);

    @Select("SELECT C.FK_LOGIN_ID,  C.SCORE,(@ranknum :=@ranknum + 1) AS RANK FROM (SELECT SUM(INTEGRAL) AS SCORE,FK_LOGIN_ID FROM DDB_USER_INTEGRAL_RECORD WHERE CREATE_TIME>#{beforeDate} AND CREATE_TIME<#{todayDate} GROUP BY FK_LOGIN_ID LIMIT 100) C, (SELECT(@ranknum := 0)) B ORDER BY SCORE DESC LIMIT #{pageIndex},#{pageSize}")
    public List<DdbUserIntegral> getAllUserRanking(@Param("beforeDate") String beforeDate,
            @Param("todayDate") String todayDate, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    @Select("SELECT * FROM (SELECT C.FK_LOGIN_ID,  C.SCORE,(@ranknum :=@ranknum + 1) AS RANK FROM (SELECT SUM(INTEGRAL) AS SCORE,FK_LOGIN_ID FROM DDB_USER_INTEGRAL_RECORD WHERE CREATE_TIME>#{beforeDate} AND CREATE_TIME<#{todayDate} GROUP BY FK_LOGIN_ID ) C, (SELECT(@ranknum := 0)) B ORDER BY SCORE DESC) D WHERE FK_LOGIN_ID=#{loginId}")
    public DdbUserIntegral getUserIntegral(@Param("loginId") String loginId, @Param("beforeDate") String beforeDate,
            @Param("todayDate") String todayDate);

    @Select("SELECT COALESCE(SUM(INTEGRAL),0)   FROM DDB_USER_INTEGRAL_RECORD WHERE FK_LOGIN_ID=#{loginId} AND CREATE_TIME>#{beforeDate} AND CREATE_TIME<#{todayDate}")
    public int getUserScore(@Param("loginId") String loginId, @Param("beforeDate") String beforeDate,
            @Param("todayDate") String todayDate);

    @Select("SELECT * FROM (SELECT C.FK_LOGIN_ID,  C.SCORE,(@ranknum :=@ranknum + 1) AS RANK FROM (SELECT SUM(CASE WHEN FK_LOGIN_ID = #{loginId} THEN INTEGRAL+#{numb} ELSE INTEGRAL END) AS SCORE,FK_LOGIN_ID FROM DDB_USER_INTEGRAL_RECORD WHERE CREATE_TIME>#{beforeDate} AND CREATE_TIME<#{todayDate} GROUP BY FK_LOGIN_ID ) C, (SELECT(@ranknum := 0)) B ORDER BY SCORE DESC) D WHERE FK_LOGIN_ID=#{loginId}")
    public DdbUserIntegral getUserNewIntegral(@Param("loginId") String loginId, @Param("beforeDate") String beforeDate,
            @Param("todayDate") String todayDate, @Param("numb") int numb);

    @Select("SELECT COUNT(*) FROM  (SELECT COUNT(*) FROM DDB_USER_INTEGRAL_RECORD  GROUP BY FK_LOGIN_ID) C")
    public int getNumberSum(@Param("beforeDate") String beforeDate, @Param("todayDate") String todayDate);

    @Select({ "<script>",
            "SELECT C.FK_LOGIN_ID,  C.SCORE,(@RANKNUM :=@RANKNUM + 1) AS RANK "
          + "FROM (SELECT SUM(INTEGRAL) AS SCORE,FK_LOGIN_ID " 
          + "FROM DDB_USER_INTEGRAL_RECORD WHERE 1=1",
            "<when test='loginIds!=null'> ", "AND FK_LOGIN_ID IN ",
            "<foreach item='item' index='index' collection='loginIds'", "open='(' separator=',' close=')'>", "#{item}",
            "</foreach>",
            "</when> "
          + "and CREATE_TIME &gt; #{beforeDate} AND CREATE_TIME &lt; #{todayDate} GROUP BY FK_LOGIN_ID LIMIT 100) C, (SELECT(@RANKNUM := 0)) B ORDER BY SCORE DESC limit #{pageIndex},#{pageSize}",
            "</script>" })
    public List<DdbUserIntegral> getFriendRanking(@Param("loginIds") List<String> loginIds,
            @Param("beforeDate") String beforeDate, @Param("todayDate") String todayDate,
            @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    @Select("SELECT IFNULL(SUM(INTEGRAL),0) AS SCORE,FK_LOGIN_ID FROM DDB_USER_INTEGRAL_RECORD WHERE CREATE_TIME>#{beforeDate} AND CREATE_TIME<#{todayDate} AND FK_LOGIN_ID=#{loginId} ")
    public int getIntegralSum(@Param("loginId") String loginId, @Param("todayDate") String todayDate,
            @Param("beforeDate") String beforeDate);

    @Select("SELECT COUNT(*) FROM DDB_USER_INTEGRAL_RECORD WHERE FK_LOGIN_ID=#{loginId} ")
    public int getCount(String loginId);

    @Select({ "<script>",
            "SELECT count(*) " + "FROM (SELECT SUM(INTEGRAL) AS SCORE,FK_LOGIN_ID "
          + "FROM DDB_USER_INTEGRAL_RECORD WHERE 1=1",
            "<when test='loginIds!=null'> ", "AND FK_LOGIN_ID IN ",
            "<foreach item='item' index='index' collection='loginIds'", "open='(' separator=',' close=')'>", "#{item}",
            "</foreach>",
            "</when> "
          + "and CREATE_TIME &gt; #{beforeDate} AND CREATE_TIME &lt; #{todayDate} GROUP BY FK_LOGIN_ID LIMIT 100) C, (SELECT(@RANKNUM := 0)) B ORDER BY SCORE DESC ",
            "</script>" })
    public int getFriendCount(@Param("loginIds") List<String> loginIds, @Param("beforeDate") String beforeDate,
            @Param("todayDate") String todayDate);

    @Select("SELECT * FROM DDB_USER_INTEGRAL_RECORD WHERE FK_LOGIN_ID=#{loginId} AND CREATE_TIME>#{beforeDate} AND CREATE_TIME<#{todayDate} AND INTEGRAL_TYPE=#{integralType}")
    public DdbUserIntegralRecord getUserTodayIntegral(@Param("loginId") String loginId,
            @Param("beforeDate") String beforeDate, @Param("todayDate") String todayDate,
            @Param("integralType") String integralType);

    @Insert("INSERT INTO DDB_USER_INTEGRAL_RECORD(ID,INTEGRAL,CREATE_TIME,FK_LOGIN_ID,INTEGRAL_TYPE,UPDATE_TIME) VALUES(#{id},#{integral},#{createTime},#{fkLoginId},#{integralType},#{updateTime})")
    public int save(DdbUserIntegralRecord ddbUserIntegralRecord);

    @Update("UPDATE DDB_USER_INTEGRAL_RECORD SET INTEGRAL=#{integral} WHERE ID=#{id}")
    public void update(@Param("id") String id, @Param("integral") int integral);

    @Select("SELECT C.FK_LOGIN_ID,  C.SCORE,(@RANKNUM :=@RANKNUM + 1) AS RANK FROM (SELECT SUM(INTEGRAL) AS SCORE,FK_LOGIN_ID FROM DDB_USER_INTEGRAL_RECORD WHERE CREATE_TIME>#{beforeDate} AND CREATE_TIME<#{todayDate} GROUP BY FK_LOGIN_ID LIMIT 100) c, (SELECT(@ranknum := 0)) b ORDER BY SCORE DESC LIMIT 1")
    public DdbUserIntegral getAllRankTop(@Param("beforeDate") String beforeDate, @Param("todayDate") String todayDate);

    @Select("SELECT * FROM (SELECT C.FK_LOGIN_ID,  C.SCORE,(@ranknum :=@ranknum + 1) AS RANK FROM (SELECT SUM(INTEGRAL) AS SCORE,FK_LOGIN_ID FROM DDB_USER_INTEGRAL_RECORD WHERE CREATE_TIME>#{beforeDate} AND CREATE_TIME<#{todayDate} GROUP BY FK_LOGIN_ID LIMIT 100) C, (SELECT(@ranknum := 0)) B ORDER BY SCORE DESC) D WHERE FK_LOGIN_ID=#{loginId} LIMIT 1")
    public DdbUserIntegral getFriendRankTop(@Param("loginId") String loginId, @Param("beforeDate") String beforeDate,
            @Param("todayDate") String todayDate);

    @Select({ "<script>",
            "SELECT * FROM (SELECT C.FK_LOGIN_ID,  C.SCORE,(@ranknum :=@ranknum + 1) AS RANK FROM (SELECT SUM(INTEGRAL) AS SCORE,FK_LOGIN_ID FROM DDB_USER_INTEGRAL_RECORD WHERE 1=1",
            "<when test='loginIds!=null'> ", "AND FK_LOGIN_ID IN ",
            "<foreach item='item' index='index' collection='loginIds'", "open='(' separator=',' close=')'>", "#{item}",
            "</foreach>",
            "</when> "
          + "and CREATE_TIME &gt; #{beforeDate} AND CREATE_TIME &lt; #{todayDate} GROUP BY FK_LOGIN_ID LIMIT 100) C, (SELECT(@ranknum := 0)) B ORDER BY SCORE DESC) D WHERE FK_LOGIN_ID=#{loginId} LIMIT 1"
          + "</script>" })
    public DdbUserIntegral getInFriendIntegral(@Param("loginId") String loginId,
            @Param("loginIds") List<String> loginIds, @Param("beforeDate") String beforeDate,
            @Param("todayDate") String todayDate);

    @Delete("DELETE FROM DDB_USER_INTEGRAL_RECORD WHERE FK_LOGIN_ID=#{loginId}")
    public int deleteUserAllIntegral(String loginId);

    @Select("SELECT * FROM (SELECT C.FK_LOGIN_ID,  C.SCORE,(@ranknum :=@ranknum + 1) AS RANK FROM (SELECT SUM(INTEGRAL) AS SCORE,FK_LOGIN_ID FROM DDB_USER_INTEGRAL_RECORD  GROUP BY FK_LOGIN_ID ) C, (SELECT(@ranknum := 0)) B ORDER BY SCORE DESC) D WHERE FK_LOGIN_ID=#{loginId}")
    public DdbUserIntegral getUserAllIntegral(@Param("loginId") String loginId);
    
    @Select("SELECT IFNULL(sum(integral),0) FROM DDB_USER_INTEGRAL_RECORD WHERE FK_LOGIN_ID=#{loginId} AND MONTH(CREATE_TIME)=#{month} AND YEAR(CREATE_TIME)=#{year} ")
    public int getMonthIntegralSum(@Param("loginId") String loginId, @Param("month") String month,
            @Param("year") String year);
    
    @Select("SELECT * FROM DDB_USER_INTEGRAL_RECORD where FK_LOGIN_ID=#{loginId} AND INTEGRAL_TYPE=#{integralType} LIMIT 1")
    public DdbUserIntegralRecord getUserIntegralByloginIdAndType(@Param("loginId") String loginId,@Param("integralType") String integralType);

}
