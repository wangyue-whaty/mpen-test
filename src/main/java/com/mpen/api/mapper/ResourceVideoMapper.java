/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.EnumTypeHandler;

import com.mpen.api.domain.DdbResourceVideo;
import com.mpen.api.mapper.db.ResourceBookTypeHandler;

@Mapper
public interface ResourceVideoMapper {
    @Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class)
    @Select("SELECT * FROM DDB_RESOURCE_VEDIO WHERE CODE=#{code} AND FK_BOOK_ID=#{bookId}")
    List<DdbResourceVideo> getUrl(@Param("code") int code, @Param("bookId") String bookId);
    
    /**
     * 查询某book某model对应的activityid, 句子, 视频
     * 涉及相关表: DDB_RESOURCE_VEDIO(书籍视频资源表), ddb_resource_book_catalog(书籍内容详情 : 句子, activity, unit, model)
     * 两张表相关的字段为 fk_book_id, name(例如:M1U1A1.mp4, M1即Model1, U1即Unit 1, A1即Activity 1)
     * 使用 SUBSTRING, LOCATE截取name获取对应Model, Unit, Activity关联查询
     * @param model
     * @param bookId
     * @return
     */
    @Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class)
    @Select("SELECT drv.*, drbc.ID activityId, drbc.ITEM item, CONCAT(drbc1.`NAME`,' ',drbc1.NUMBER) unitName  "
            + "FROM DDB_RESOURCE_VEDIO drv"
            + " LEFT JOIN "
            + "ddb_resource_book_catalog drbc"
            + " ON  "
            + "( "
            + "drv.fk_book_id = drbc.fk_book_id"
            + " and "
            + "SUBSTRING(drv.`name` , LOCATE('A',drv.`name`)+1 , LOCATE('.',drv.`name`) - LOCATE('A',drv.`name`) -1 )"
            + " = drbc.NUMBER and 'Activity' = drbc.`name`"
            + " ) "
            + "INNER JOIN "
            + "ddb_resource_book_catalog drbc1"
            + " ON "
            + "( "
            + "drbc.FK_CATALOG_ID = drbc1.id"
            + " AND "
            + "SUBSTRING(drv.`name` , LOCATE('U',drv.`name`)+1 ,LOCATE('A',drv.`name`) - LOCATE('U',drv.`name`) -1 )"
            + " = drbc1.NUMBER AND 'Unit' = drbc1.`name`"
            + " ) "
            + "INNER JOIN "
            + "ddb_resource_book_catalog drbc2"
            + " ON "
            + "( "
            + "drbc1.FK_CATALOG_ID = drbc2.ID"
            + " AND "
            + "SUBSTRING(drv.`name` , LOCATE('M',drv.`name`) + 1 ,LOCATE('U',drv.`name`) - LOCATE('M',drv.`name`) - 1 )"
            + " = drbc2.NUMBER"
            + " AND 'Module' = drbc2.`name`"
            + " ) "
            + "WHERE drv.`name` like CONCAT('%', #{model}, '%') AND drv.fk_book_id=#{bookId} "
            + "GROUP BY drv.`name`")
    List<DdbResourceVideo> getNoTeacherClassVideo(@Param("model") String model, @Param("bookId") String bookId);
}
