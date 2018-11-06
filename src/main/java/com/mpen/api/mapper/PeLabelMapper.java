/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import com.mpen.api.domain.DdbPeLabel;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PeLabelMapper {
    @Select("SELECT * FROM DDB_PE_LABEL WHERE ID=#{id} LIMIT 1")
    DdbPeLabel getById(String id);

    @Select("SELECT * FROM DDB_PE_LABEL L JOIN ENUM_CONST C ON C.ID=L.FLAG_LABEL_TYPE  "
        + "WHERE C.CODE='0' AND L.CODE>4 AND L.CODE<24 ORDER BY L.CODE ASC ")
    List<DdbPeLabel> get();
}
