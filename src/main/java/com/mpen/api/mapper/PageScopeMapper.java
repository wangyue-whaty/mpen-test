/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.bean.PageScope;
import com.mpen.api.domain.DdbResourcePageScope;

@Mapper
public interface PageScopeMapper {

    @Select("SELECT S.ID,C.FK_BOOK_ID BOOK_ID,C.PAGE_NUM,S.CODE_START,S.CODE_END,S.X_CODE_NUM,"
        + "S.Y_CODE_NUM,S.MATRIX_GAP,S.TOP_MARGIN,S.BOTTOM_MARGIN,S.LEFT_MARGIN,S.RIGHT_MARGIN,S.MATRIX_SIZE,"
        + "S.DOT_DISTANCE_IN_PIXELS,S.DOT_SIZE,S.DOT_SHIFT,S.SUB_PAGES  FROM DDB_RESOURCE_PAGE_SCOPE S JOIN "
        + "( SELECT MAX(ID) ID FROM DDB_RESOURCE_PAGE_SCOPE GROUP BY CODE_START ) S2 ON S.ID=S2.ID  JOIN "
        + "DDB_RESOURCE_PAGE_CODE C ON S.FK_PAGE_ID=C.ID WHERE C.FK_BOOK_ID = #{bookId}  ORDER BY S.CODE_START ")
    List<PageScope> get(String bookId);

    @Select("SELECT MAX(SIGN) SIGN,MAX(CODE_END) CODE_END FROM DDB_RESOURCE_PAGE_SCOPE")
    Map<String, Object> getSignAndCode();

    @Insert("INSERT INTO DDB_RESOURCE_PAGE_SCOPE (ID,FK_PAGE_ID,CODE_START,CODE_END,CREATE_TIME,TIF_LINK,"
        + "X_CODE_NUM,Y_CODE_NUM,SIGN,MATRIX_GAP,MATRIX_SIZE,DOT_DISTANCE_IN_PIXELS,DOT_SIZE,DOT_SHIFT,"
        + "TOP_MARGIN,BOTTOM_MARGIN,LEFT_MARGIN,RIGHT_MARGIN,FK_TYPE_ID,REBUILD,SUB_PAGES) VALUES(#{id},#{fkPageId},#{codeStart},"
        + "#{codeEnd},#{createTime},#{tifLink},#{xCodeNum},#{yCodeNum},#{sign},#{matrixGap},#{matrixSize},"
        + "#{dotDistanceInPixels},#{dotSize},#{dotShift},#{topMargin},#{bottomMargin},#{leftMargin},#{rightMargin},#{fkTypeId},#{rebuild},#{subPages})")
    void save(DdbResourcePageScope pageScope);
    
    @Select("SELECT * FROM DDB_RESOURCE_PAGE_SCOPE WHERE FK_PAGE_ID=#{fkPageId} AND FK_TYPE_ID=#{fkTypeId} LIMIT 1")
    DdbResourcePageScope getByTypeAndPage(@Param("fkTypeId") String fkTypeId, @Param("fkPageId") String fkPageId);
    
    /**
     * 根据id删除(单元测试使用)
     * @param id
     */
    @Delete("DELETE FROM DDB_RESOURCE_PAGE_SCOPE WHERE ID =#{id}")
    void deleteById(String id);
}
