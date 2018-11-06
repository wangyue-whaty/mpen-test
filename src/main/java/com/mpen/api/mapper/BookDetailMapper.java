/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbBookDetail;

@Mapper
public interface BookDetailMapper {
    @Select("SELECT * FROM DDB_BOOK_DETAIL ORDER BY VERSION DESC LIMIT 1")
    DdbBookDetail get();

    @Insert("INSERT INTO DDB_BOOK_DETAIL (VERSION,BOOK_INFOS) VALUES(#{version},#{bookInfos})")
    void save(DdbBookDetail bookDetail);

}
