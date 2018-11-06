/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.EnumTypeHandler;

import com.mpen.api.domain.MobileApp;

/**
 * MobileAppMapper对象.
 * 
 * @author zyt
 *
 */
@Mapper
public interface MobileAppMapper {
    @Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class)
    @Select("SELECT * FROM MOBILE_APP WHERE TYPE=#{type} ORDER BY VERSION_CODE DESC LIMIT 1")
    MobileApp get(MobileApp.Type type);
}
