/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbResourcePageCode;

@Mapper
public interface PageCodeMapper {
    @Insert("INSERT INTO DDB_RESOURCE_PAGE_CODE (ID,CREATE_DATETIME,PAGE_NUM,NAME,FK_BOOK_ID,WIDTH,HEIGHT) "
        + "VALUES (#{id},#{createDatetime},#{pageNum},#{name},#{fkBookId},#{width},#{height})")
    void save(DdbResourcePageCode page);

    @Select("SELECT ID,CREATE_DATETIME,PAGE_NUM,NAME,FK_BOOK_ID,WIDTH,HEIGHT FROM DDB_RESOURCE_PAGE_CODE WHERE FK_BOOK_ID=#{fkBookId} ORDER BY PAGE_NUM")
    List<DdbResourcePageCode> getBookPages(@Param("fkBookId") String fkBookId);

    /**
     * 根据pageId查询bookId,bookName
     * @param pageId
     * @return
     */
    @Select("SELECT T2.ID,T2.NAME,T2.FK_BOOK_ID FROM DDB_RESOURCE_PAGE_SCOPE T1,DDB_RESOURCE_PAGE_CODE T2 WHERE T1.FK_PAGE_ID = T2.ID AND T1.ID=#{pageId}")
    DdbResourcePageCode getPageCodeByPageId(String pageId);
    
    /**
     * 根据id删除(单元测试使用)
     * @param id
     */
    @Delete("DELETE FROM DDB_RESOURCE_PAGE_SCOPE WHERE ID=#{id}")
    void deleteById(String id);
}
