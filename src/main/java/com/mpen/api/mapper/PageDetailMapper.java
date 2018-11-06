/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.domain.DdbPageDetail;


@Mapper
public interface PageDetailMapper {
    
    /**
     * 修改查询方法:新增IS_ACTIVE字段,仅查询标记为正式版本的数据
     * 新增bookId字段,区分每本书的pageInfo
     * @return
     */
    @Select("SELECT * FROM DDB_PAGE_DETAIL WHERE IS_ACTIVE = #{isActive} AND BOOK_ID =#{bookId} ORDER BY VERSION DESC LIMIT 1")
    DdbPageDetail get(@Param("bookId") String bookId, @Param("isActive") boolean isActive);

    @Insert("INSERT INTO DDB_PAGE_DETAIL (VERSION,PAGE_INFOS,BOOK_ID,IS_ACTIVE) VALUES(#{version},#{pageInfos},#{bookId},#{isActive})")
    void save(DdbPageDetail pageDetail);

    /**
     * 将一本书的指定version标记为正式版本
     * @param version
     */
    @Update("UPDATE DDB_PAGE_DETAIL SET IS_ACTIVE = #{isActive} WHERE VERSION= #{version} AND BOOK_ID = #{bookId}")
    void activate(@Param("version") String version, @Param("isActive") boolean isActive, @Param("bookId") String bookId);
    
    /**
     * 将一本书除指定version外的pageInfo标记为非正式版本
     * @param version
     * @param isActive
     * @param bookId
     */
    @Update("UPDATE DDB_PAGE_DETAIL SET IS_ACTIVE = false WHERE VERSION != #{version} AND BOOK_ID = #{bookId}")
    void inActivateOtherThan(@Param("version") String version, @Param("bookId") String bookId);
    
    /**
     * 查询所有书的pageInfo
     * 多一层查询,防止bookId为""时,group by出错:先查询出所有正式版本的pageInfo的version集合,再查询出包含在version集合中的所有pageInfo.
     * @return
     */
    @Select("SELECT D.* FROM DDB_PAGE_DETAIL D WHERE VERSION IN ( SELECT VERSION FROM DDB_PAGE_DETAIL WHERE IS_ACTIVE =true GROUP BY BOOK_ID)") 
    List<DdbPageDetail> getDdbPageDetailList();
}
