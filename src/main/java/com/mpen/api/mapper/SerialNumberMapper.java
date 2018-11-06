/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbSerialNumber;

@Mapper
public interface SerialNumberMapper {
    @Select("SELECT * FROM DDB_SERIAL_NUMBER WHERE PREFIX=#{prefix} AND #{suffix}>=SUFFIX_START "
        + "AND #{suffix}<=SUFFIX_END LIMIT 1")
    DdbSerialNumber get(@Param("prefix") String prefix, @Param("suffix") long suffix);

}
