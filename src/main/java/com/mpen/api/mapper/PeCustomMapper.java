/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.domain.DdbPeCustom;

/**
 * PeCustom Mapper对象.
 *
 * @author wangkai05
 *
 */
@Mapper
public interface PeCustomMapper {

    @Select("SELECT * FROM DDB_PE_CUSTOM WHERE LOGIN_ID=#{loginId} LIMIT 1")
    DdbPeCustom getByLoginId(String loginId);

    @Insert("INSERT INTO DDB_PE_CUSTOM(ID,LOGIN_ID,FK_USER_ID,WORK_UNIT,QQ,CARD_NO,"
        + "TRUE_NAME,MOBILEPHONE,REG_NO,FK_LABEL_TWO_ID,FK_LABEL_THREE_ID,"
        + "NICK_NAME,REGISTRATION_DATE,POST,FK_LABEL_ID,FK_LABEL_ONE_ID,"
        + "BRITHDAY,EMAIL,PHONE,ADDRESS,FLAG_GENDER,ZIP_ADDRESS,AGE,SCHOOL) "
        + "values(#{id},#{loginId},#{fkUserId},#{workUnit},#{qq},#{cardNo},"
        + "#{trueName},#{mobilephone},#{regNo},#{fkLabelTwoId},#{fkLabelThreeId},"
        + "#{nickName},#{registrationDate},#{post},#{fkLabelId},#{fkLabelOneId},"
        + "#{brithday},#{email},#{phone},#{address},#{flagGender},#{zipAddress}," + "#{age},#{school});")
    void create(DdbPeCustom peCustom);

    @Select("SELECT * FROM DDB_PE_CUSTOM WHERE ID=#{id} LIMIT 1")
    DdbPeCustom getById(String id);

    @Select("SELECT * FROM DDB_PE_CUSTOM WHERE FK_USER_ID=#{id} LIMIT 1")
    DdbPeCustom getBySsoUserId(String id);

    @Select("DELETE FROM DDB_PE_CUSTOM WHERE ID=#{id}")
    void delete(String id);

    @Update("UPDATE DDB_PE_CUSTOM SET NICK_NAME=#{nickName},FK_LABEL_ID=#{grade},FLAG_GENDER=#{sex},"
        + "SCHOOL=#{school},AGE=#{age} WHERE ID=#{id}")
    void update(@Param("id") String id, @Param("nickName") String nickName, @Param("school") String school,
        @Param("grade") String grade, @Param("age") int age, @Param("sex") String sex);

    @Select("SELECT * FROM DDB_PE_CUSTOM")
    List<DdbPeCustom> get();

    @Update("UPDATE DDB_PE_CUSTOM SET ADDRESS=#{address} WHERE ID=#{id}")
    void saveAddress(@Param("address") String address, @Param("id") String id);
    
    @Select("SELECT * FROM DDB_PE_CUSTOM ORDER BY LOGIN_ID DESC LIMIT #{pageIndex},#{pageSize} ")
    List<DdbPeCustom> getPeCustom(@Param("pageIndex")int pageIndex, @Param("pageSize")Integer pageSize);

    @Select({ "<script>", 
        "SELECT * FROM DDB_PE_CUSTOM ", 
           "<where>",
               "<when test='loginIds!=null'>",
                    " LOGIN_ID in ", "<foreach item='item' index='index' collection='loginIds'",
                    "open='(' separator=',' close=')'>", "#{item}", "</foreach>", 
              "</when>",
          " </where>",
        "</script>" })
    List<DdbPeCustom> listByLoginIds(@Param("loginIds") List<String> loginIds);
    
    @Update("UPDATE DDB_PE_CUSTOM SET COVER=#{cover} WHERE LOGIN_ID=#{loginId}")
    int updateCoverByLoginId(@Param("loginId") String loginId, @Param("cover") String cover);
   
}
