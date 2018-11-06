/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.domain.DdbResourceBook;
import com.mpen.api.mapper.db.ResourceBookTypeHandler;

/**
 * ResourceBookMapper对象.
 *
 * @author wangkai05
 *
 */
@Mapper
public interface ResourceBookMapper {
    /**
     * 增加查询效率，去除排序
     * IS_LINE_READ:
     * 1: 支持云点读
     * 2: 本地点读
     * 其它值：以后其它用途：总体： > 0: 包括在 bookinfo list里面；< 0: 不包括
     */
    @Results(value = { @Result(column = "X_CODE_NUM", property = "xaxesCodeNum"),
        @Result(column = "Y_CODE_NUM", property = "yaxesCodeNum"),
        @Result(column = "TYPE", property = "type", typeHandler = ResourceBookTypeHandler.class) })
    @Select("SELECT * FROM  DDB_RESOURCE_BOOK WHERE IS_LINE_READ > 0")
    List<DdbResourceBook> getValidBooks();
    
    /**
     * 查询教学资源列表
     * 查询的结果集为TEACH_LINK not null
     */
    @Results(value = { @Result(column = "X_CODE_NUM", property = "xaxesCodeNum"),
        @Result(column = "Y_CODE_NUM", property = "yaxesCodeNum"),
        @Result(column = "TYPE", property = "type", typeHandler = ResourceBookTypeHandler.class) })
    @Select("SELECT * FROM DDB_RESOURCE_BOOK WHERE TEACH_LINK IS NOT NULL AND TEACH_LINK != ''")
    List<DdbResourceBook> getBooksTeachLink();

    // xCodeNum,yCodeNum作为字段名不符合代码规范 所以这里做个映射
    @Results(value = { @Result(column = "X_CODE_NUM", property = "xaxesCodeNum"),
        @Result(column = "Y_CODE_NUM", property = "yaxesCodeNum"),
        @Result(column = "TYPE", property = "type", typeHandler = ResourceBookTypeHandler.class) })
    @Select("SELECT * FROM DDB_RESOURCE_BOOK WHERE ID=#{id} LIMIT 1")
    DdbResourceBook getId(String id);
    
    @Results(value = { @Result(column = "X_CODE_NUM", property = "xaxesCodeNum"),
        @Result(column = "Y_CODE_NUM", property = "yaxesCodeNum"),
        @Result(column = "TYPE", property = "type", typeHandler = ResourceBookTypeHandler.class) })
    @Select("SELECT * FROM DDB_RESOURCE_BOOK WHERE NAME=#{name} ORDER BY CREATE_DATETIME DESC LIMIT 1")
    DdbResourceBook getByName(String name);

    @Result(column = "TYPE", property = "type", typeHandler = ResourceBookTypeHandler.class)
    @Insert("INSERT INTO DDB_RESOURCE_BOOK( ID,NAME,ISBN,CREATE_DATETIME,"
        + "IS_LINE_READ,TYPE,SEQUENCE) VALUES (#{id},#{name},#{isbn},#{createDatetime},#{isLineRead},#{type.name},#{sequence})")
    void create(DdbResourceBook book);

    @Result(column = "TYPE", property = "type", typeHandler = ResourceBookTypeHandler.class)
    @Update("UPDATE DDB_RESOURCE_BOOK SET NAME=#{name},CODE=#{code},ISBN=#{isbn},PHOTO=#{photo},INTRODUCTION=#{introduction},"
        + "CATALOG=#{catalog},AUTHOR=#{author},RES_SIZE=#{resSize},BOOK_LINK=#{bookLink},SUIT_IMAGE=#{suitImage},VERSION=#{version},TYPE=#{type.name},"
        + "GRADE=#{grade},BOOK_PAGE_NUM=#{bookPageNum},WIDTH=#{width},HEIGHT=#{height},POINT_SIZE=#{pointSize},POINT_NUM=#{pointNum},"
        + "POINT_OFFSET=#{pointOffset},POINT_DISTANCE=#{pointDistance},POINT_PEDDING=#{pointPedding},POINT_TOP_MARGIN=#{pointTopMargin},"
        + "POINT_LEFT_MARGIN=#{pointLeftMargin},MPP_LINK=#{mppLink},MPV_LINK=#{mpvLink},TEACH_LINK=#{teachLink},MP_LINK_MD5=#{mpLinkMd5},"
        + "MPP_LINK_MD5=#{mppLinkMd5},MPV_LINK_MD5=#{mpvLinkMd5},TEACH_LINK_MD5=#{teachLinkMd5} WHERE ID=#{id}")
    void update(DdbResourceBook book);
    
    @Result(column = "TYPE", property = "type", typeHandler = ResourceBookTypeHandler.class)
    @Delete("DELETE FROM DDB_RESOURCE_BOOK WHERE ID=#{id}")
    void delete(String id);

    @Results(value = { @Result(column = "X_CODE_NUM", property = "xaxesCodeNum"),
        @Result(column = "Y_CODE_NUM", property = "yaxesCodeNum"),
        @Result(column = "TYPE", property = "type", typeHandler = ResourceBookTypeHandler.class) })
    @Select("SELECT * FROM DDB_RESOURCE_BOOK WHERE IS_PRE_DOWNLOAD= '1' LIMIT #{pageNo},#{pageSize}")
    List<DdbResourceBook> getPreDownload(@Param("pageNo") int pageNo, @Param("pageSize") int pageSize);
    
    /**
     * 查询相同书名的个数(单元测试使用)
     * @param id
     * @return
     */
    @Select("SELECT COUNT(*) FROM DDB_RESOURCE_BOOK WHERE NAME=#{name}")
    int countByName(String name);
}
