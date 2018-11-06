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

import com.mpen.api.common.Constants.PenType;
import com.mpen.api.domain.DdbPrPenCustom;

/**
 * PrPenCustom Mapper对象.
 * 
 * @author zyt
 *
 */
@Mapper
public interface PrPenCustomMapper {
    @Insert("INSERT INTO DDB_PR_PEN_CUSTOM (ID,FK_PEN_ID,FK_CUSTOM_ID,"
        + "CREATE_DATETIME,IS_VALID) VALUES (#{id},#{fkPenId},#{fkCustomId},#{createDatetime},#{isValid})")
    void create(DdbPrPenCustom prPenCustom);

    @Select("SELECT * FROM DDB_PR_PEN_CUSTOM WHERE FK_PEN_ID=#{penId} AND FK_CUSTOM_ID=#{userId} AND IS_VALID=1  LIMIT 1")
    DdbPrPenCustom getByPenIdAndUserId(@Param("penId") String penId, @Param("userId") String userId);

    @Select("SELECT * FROM DDB_PR_PEN_CUSTOM WHERE FK_CUSTOM_ID=#{userId} AND IS_VALID=1 LIMIT 1")
    DdbPrPenCustom getByUserId(String userId);

    @Update("UPDATE DDB_PR_PEN_CUSTOM SET IS_VALID=0, DELETE_TIME=#{deleteTime} WHERE FK_CUSTOM_ID=#{fkCustomId} AND FK_PEN_ID=#{fkPenId}")
    int deleteByCustomIdAndPenId(DdbPrPenCustom prPenCustom);
    
    /**
     * 根据笔id查询绑定关系
     * @param penId
     * @return
     */
    @Select("SELECT * FROM DDB_PR_PEN_CUSTOM WHERE FK_PEN_ID=#{penId} AND IS_VALID=1 ")
    DdbPrPenCustom getByPenId(String penId);
    /**
     * 查询绑定笔类型的历史记录
     * @param userId
     * @return
     */
    @Select("SELECT TYPE FROM DDB_PE_PEN WHERE ID in (SELECT FK_PEN_ID FROM DDB_PR_PEN_CUSTOM WHERE "
            + "FK_CUSTOM_ID=#{userId}) ")
    List<PenType> getHasBoundPenType(String userId);
    
    @Select("SELECT COUNT(DISTINCT FK_PEN_ID) FROM DDB_PR_PEN_CUSTOM WHERE FK_CUSTOM_ID=#{userId} AND IS_VALID=1")
    int getBindPenNum(String userId);
    
}
