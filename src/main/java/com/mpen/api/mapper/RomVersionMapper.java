/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import com.mpen.api.common.Constants.PenType;
import com.mpen.api.domain.DdbRomVersion;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.EnumTypeHandler;

@Mapper
public interface RomVersionMapper {
    @Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class)
    @Select("SELECT * FROM DDB_ROM_VERSION WHERE ID=#{id} LIMIT 1")
    DdbRomVersion getById(String id);
    
    /**
     * 查询最新版本信息
     * 用于单元测试
     * @param id
     * @param type
     * @return
     */
    @Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class)
    @Select("SELECT * FROM DDB_ROM_VERSION WHERE TYPE=#{type} AND NAME LIKE CONCAT('%',#{version},'%') ORDER BY LEVEL DESC LIMIT 1")
    List<DdbRomVersion> getVersionById(@Param("type") PenType type, @Param("version") String version);
    
}
