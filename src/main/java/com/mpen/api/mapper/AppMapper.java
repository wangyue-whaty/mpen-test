/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.EnumTypeHandler;

import com.mpen.api.common.Constants.PenType;
import com.mpen.api.domain.DdbApp;
import com.mpen.api.domain.DdbPePen;

/**
 * AppMapper对象.
 * 
 * @author zyt
 *
 */
@Mapper
public interface AppMapper {
    @Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class)
    @Select("SELECT * FROM DDB_APP WHERE IS_VALID !='0' AND (LABEL IS NULL OR TRIM(LABEL)='')"
        + " AND VERSION_CODE>#{versionCode} AND TYPE=#{type} ORDER BY VERSION_CODE DESC LIMIT 1")
    DdbApp get(@Param("versionCode") int versionCode, @Param("type") PenType type);

    @Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class)
    @Select("SELECT * FROM DDB_APP WHERE IS_VALID !='0' AND (LABEL IS NULL OR TRIM(LABEL)=''"
        + " OR FIND_IN_SET(LABEL,#{label})) AND VERSION_CODE>#{versionCode} AND TYPE=#{type} ORDER BY VERSION_CODE DESC LIMIT 1")
    DdbApp getByLabel(@Param("label") String label, @Param("versionCode") int versionCode,
        @Param("type") PenType type);

    @Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class)
    @Select("SELECT * FROM DDB_APP WHERE IS_VALID !='0' AND IS_IMPORTANT='1' AND TYPE=#{type} ORDER BY VERSION_CODE")
    List<DdbApp> getImportantVersion(@Param("type") PenType type);

    @Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class)
    @Select("SELECT * FROM DDB_APP WHERE VERSION_NAME=#{name} AND TYPE=#{type} ")
    DdbApp getByVersionName(@Param("name") String name, @Param("type") PenType type);
}
