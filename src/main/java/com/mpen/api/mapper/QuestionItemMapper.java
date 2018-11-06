/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.mpen.api.domain.DdbQuestionItem;

@Mapper
public interface QuestionItemMapper {
    @Select("SELECT * FROM DDB_QUESTION_ITEM ORDER BY SEQUENCE")
    List<DdbQuestionItem> get();
}
