/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.EnumTypeHandler;

import com.mpen.api.domain.DdbActionRecord;

@Mapper
public interface ActionRecordMapper {
    @Results(value = { @Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class),
        @Result(column = "SUB_TYPE", property = "subType", typeHandler = EnumTypeHandler.class) })
    @Select("SELECT * FROM DDB_ACTION_RECORD WHERE UPLOAD_UUID=#{uploadUuid}")
    List<DdbActionRecord> getByUploadUuid(String uploadUuid);

    @Results(value = { @Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class),
        @Result(column = "SUB_TYPE", property = "subType", typeHandler = EnumTypeHandler.class) })
    @Insert("INSERT INTO DDB_ACTION_RECORD (UPLOAD_UUID,SEQUCE_NUM_IN_BATCH,FK_PEN_ID,"
        + "UPLOAD_TIME,TYPE,SUB_TYPE,DATA,VERSION) VALUES (#{uploadUuid},#{sequceNumInBatch},"
        + "#{fkPenId},#{uploadTime},#{type},#{subType},#{data},#{version})")
    void save(DdbActionRecord ddbActionRecord);
}
