/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.domain.DdbPenReadingLimit;

/**
 * MobileAppMapper对象.
 * 
 * @author zyt
 *
 */
@Mapper
public interface PenReadingLimitMapper {
    @Select("SELECT * FROM DDB_PEN_READING_LIMIT WHERE FK_PEN_ID=#{penId} LIMIT 1")
    DdbPenReadingLimit getByPenId(String penId);

    @Insert("INSERT INTO DDB_PEN_READING_LIMIT (ID,FK_PEN_ID,TIMES,EDIT_DATE) VALUES (#{id},#{fkPenId},#{times},#{editDate})")
    void save(DdbPenReadingLimit penLimit);

    @Update("UPDATE DDB_PEN_READING_LIMIT SET TIMES=#{times},EDIT_DATE=#{editDate} WHERE ID=#{id}")
    void updateTimesByPenId(DdbPenReadingLimit penLimit);
}
