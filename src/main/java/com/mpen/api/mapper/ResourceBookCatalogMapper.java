/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import com.mpen.api.domain.DdbResourceBookCatalog;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface ResourceBookCatalogMapper {
    @Select("SELECT * FROM DDB_RESOURCE_BOOK_CATALOG WHERE ID=#{id} LIMIT 1")
    DdbResourceBookCatalog getById(String id);

    @Select("SELECT C1.* FROM DDB_RESOURCE_BOOK_CATALOG C1 JOIN DDB_RESOURCE_BOOK_CATALOG C2 "
        + "ON C1.FK_CATALOG_ID=C2.ID WHERE C1.FK_BOOK_ID=#{bookId} AND C1.NAME=#{name} ORDER "
        + "BY C2.NUMBER,C1.NUMBER")
    List<DdbResourceBookCatalog> getByBookIdAndName(@Param("bookId") String bookId, @Param("name") String name);

    @Select("SELECT * FROM DDB_RESOURCE_BOOK_CATALOG WHERE FK_BOOK_ID=#{bookId} AND NAME=#{name} AND "
        + "FK_CATALOG_ID=#{catalogId} ORDER BY NUMBER")
    List<DdbResourceBookCatalog> getByBookIdAndNameAndFkCatalogId(@Param("bookId") String bookId,
        @Param("name") String name, @Param("catalogId") String catalogId);

    @Select("SELECT DISTINCT(FK_BOOK_ID) FROM DDB_RESOURCE_BOOK_CATALOG ")
    List<String> getAllBookId();
    
}
