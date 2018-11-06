/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.domain.DdbBookCoreDetail;

@Mapper
public interface BookCoreDetailMapper {
    
    /**
     * 修改查询方法:新增IS_ACTIVE字段 true-->正式版本;false-->非正式版本
     * @return
     */
    @Select("SELECT * FROM DDB_BOOK_CORE_DETAIL WHERE IS_ACTIVE = #{isActive} ORDER BY VERSION DESC LIMIT 1")
    DdbBookCoreDetail get(boolean isActive);

    @Insert("INSERT INTO DDB_BOOK_CORE_DETAIL (VERSION,BOOK_INFOS) VALUES(#{version},#{bookInfos})")
    void save(DdbBookCoreDetail bookDetail);
    
    /**
     * 标记此VERSION为正式版本
     * @param version
     */
    @Update("UPDATE DDB_BOOK_CORE_DETAIL SET IS_ACTIVE = #{isActive} WHERE VERSION = #{version}")
    void activate(@Param("version") String version, @Param("isActive") boolean isActive);

    /**
     * 标记此version外的版本为非正式版本
     * @param version
     * @param isActive
     */
    @Update("UPDATE DDB_BOOK_CORE_DETAIL SET IS_ACTIVE = false WHERE VERSION != #{version}")
    void inActivateOtherThan(@Param("version") String version);
}
