/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import com.mpen.api.common.Constants.PenType;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.domain.DdbRomUpdate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.EnumTypeHandler;

@Mapper
public interface RomUpdateMapper {

    /**
     * 只返回单一升级路径
     */
    @Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class)
    @Select("SELECT U.* FROM DDB_ROM_UPDATE U JOIN DDB_ROM_VERSION V1 ON V1.ID=U.FROM_VERSION_ID AND "
        + "V1.NAME=#{name} AND V1.TYPE=#{type} JOIN DDB_ROM_VERSION V ON V.ID=U.TO_VERSION_ID WHERE U.ITEM IS NULL OR "
        + "TRIM(U.ITEM)='' ORDER BY V.LEVEL DESC LIMIT 1")
    DdbRomUpdate get(@Param("name") String name, @Param("type") PenType type);

    /**
     * 只返回单一升级路径
     */
    @Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class)
    @Select("SELECT U.* FROM DDB_ROM_UPDATE U JOIN DDB_ROM_VERSION V1 ON V1.ID=U.FROM_VERSION_ID AND "
        + "V1.NAME=#{name} AND V1.TYPE=#{type} JOIN DDB_ROM_VERSION V ON V.ID=U.TO_VERSION_ID WHERE U.ITEM IS NULL OR "
        + "TRIM(U.ITEM)='' OR FIND_IN_SET(U.ITEM,#{item}) ORDER BY V.LEVEL DESC LIMIT 1")
    DdbRomUpdate getByItem(@Param("item") String item, @Param("name") String name, @Param("type") PenType type);
    
    /**
     * 根据fromVersionId查询信息
     * @param versionId
     * @return
     */
    @Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class)
    @Select("SELECT * FROM DDB_ROM_UPDATE WHERE FROM_VERSION_ID=#{versionId} ORDER BY CREATETIME DESC LIMIT 1")
    DdbRomUpdate getByVersion(String versionId);
}
