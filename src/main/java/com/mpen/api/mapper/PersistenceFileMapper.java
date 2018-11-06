/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.type.EnumTypeHandler;

import com.mpen.api.domain.PersistenceFile;

/**
 * AppMapper对象.
 * 
 * @author zyt
 *
 */
@Mapper
public interface PersistenceFileMapper {
    @Results(value = { @Result(column = "PROJECT", property = "project", typeHandler = EnumTypeHandler.class),
        @Result(column = "STORE_TYPE", property = "storeType", typeHandler = EnumTypeHandler.class) })
    @Insert("INSERT INTO PERSISTENCE_FILE (ID,PROGECT,TYPE,PROPERTY,STORE_TYPE,UPLOAT_TYPE,ADDRESS,CREATE_TIME,PRIORITY,VERSION,USER,SIZE,HASH,OPTIONAL) "
        + "VALUES (#{id},#{project},#{type},#{property},#{storeType},#{uploadTime},#{address},#{createTime},#{priority},#{version},#{user},#{size},#{hash},#{optional})")
    void insert(PersistenceFile file);
}
