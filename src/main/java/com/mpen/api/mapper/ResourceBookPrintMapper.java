/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.domain.DdbResourceBookPrint;

/**
 * AppMapper对象.
 * 
 * @author zyt
 *
 */
@Mapper
public interface ResourceBookPrintMapper {
    @Select("SELECT * FROM DDB_RESOURCE_BOOK_PRINT WHERE FK_BOOK_ID=#{fkBookId} AND NAME=#{name} LIMIT 1")
    DdbResourceBookPrint getByBookIdAndName(@Param("fkBookId") String fkBookId, @Param("name") String name);

    @Insert("INSERT INTO DDB_RESOURCE_BOOK_PRINT (ID,FK_BOOK_ID,NAME) VALUES(#{id},#{fkBookId},#{name})")
    void save(DdbResourceBookPrint version);
    
    @Update("UPDATE DDB_RESOURCE_BOOK_PRINT SET ZIP_LINK=#{zipLink} WHERE ID=#{id}")
    void updateZipLink(DdbResourceBookPrint version);
}
