/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.domain.DdbPenCmd;

/**
 * AppMapper对象.
 * 
 * @author zyt
 *
 */
@Mapper
public interface PenCmdMapper {
    @Select("SELECT * FROM DDB_PEN_CMD WHERE FK_PEN_ID=#{penId} AND STATUS=0 ORDER BY CREATE_DATE LIMIT 1")
    DdbPenCmd getByPenId(String penId);

    @Update("UPDATE DDB_PEN_CMD SET STATUS=1, RESULT=#{result} WHERE ID=#{id}")
    void updateStatusById(@Param("id") String id, @Param("result") String result);

    @Update("UPDATE DDB_PEN_CMD SET URL=#{url}, DESCRIPTION=#{description} WHERE ID=#{id}")
    void updateUrlById(@Param("id") String id, @Param("url") String url, @Param("description") String description);
}
