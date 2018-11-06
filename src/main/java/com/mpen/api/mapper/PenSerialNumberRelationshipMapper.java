/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbPenSerialNumberRelationship;

@Mapper
public interface PenSerialNumberRelationshipMapper {
    @Select("SELECT * FROM DDB_PEN_SERIAL_NUMBER_RELATIONSHIP WHERE FK_PEN_ID=#{penId} AND IS_VALID=1 LIMIT 1")
    DdbPenSerialNumberRelationship getVolidDataByPenId(String penId);

    @Select("SELECT * FROM DDB_PEN_SERIAL_NUMBER_RELATIONSHIP WHERE SERIAL_NUMBER=#{serialNumber} AND IS_VALID=1 LIMIT 1")
    DdbPenSerialNumberRelationship getVolidDataBySerialNumber(String serialNumber);

    @Insert("INSERT INTO DDB_PEN_SERIAL_NUMBER_RELATIONSHIP (ID,FK_PEN_ID,SERIAL_NUMBER,IS_VALID,CREATE_DATE) VALUES (#{id},"
        + "#{fkPenId},#{serialNumber},#{isValid},#{createDate})")
    void save(DdbPenSerialNumberRelationship penSerialNumberRelationship);

    @Delete("DELETE FROM DDB_PEN_SERIAL_NUMBER_RELATIONSHIP WHERE SERIAL_NUMBER=#{serialNumber}")
    void deleteBySerialNumber(String serialNumber);
}
