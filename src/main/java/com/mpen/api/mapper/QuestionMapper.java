/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbQuestion;

@Mapper
public interface QuestionMapper {
    @Select("SELECT * FROM DDB_QUESTION WHERE FK_ITEM_ID=#{id} ORDER BY SEQUENCE")
    List<DdbQuestion> getByItemId(String id);

    @Select("SELECT * FROM DDB_QUESTION WHERE ID=#{id} LIMIT 1")
    DdbQuestion getById(String id);
}
