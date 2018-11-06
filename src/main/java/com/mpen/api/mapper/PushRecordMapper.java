/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import com.mpen.api.domain.DdbPushRecord;
import org.apache.ibatis.annotations.Insert;

import org.apache.ibatis.annotations.Mapper;

/**
 * PushRecordMapper对象.
 * 
 * @author zyt
 *
 */
@Mapper
public interface PushRecordMapper {
    @Insert("INSERT INTO DDB_PUSH_RECORD (ID,LOGIN_ID,CONTENT,TYPE,TIME) VALUES (#{id},#{loginId},#{content}"
        + ",#{type},#{time})")
    void create(DdbPushRecord pushRecord);
}
