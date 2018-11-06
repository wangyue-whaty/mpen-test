/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import com.mpen.api.domain.EnumConst;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * EnumConst Mapper对象.
 * 
 * @author zyt
 *
 */
@Mapper
public interface EnumConstMapper {
    @Select("SELECT * FROM ENUM_CONST WHERE ID=#{id} LIMIT 1")
    EnumConst getById(String id);

    @Select("SELECT * FROM ENUM_CONST WHERE NAMESPACE=#{nameSpace} and CODE=#{code} LIMIT 1")
    EnumConst getByNamespaceCode(@Param("nameSpace") String nameSpace, @Param("code") String code);
}
