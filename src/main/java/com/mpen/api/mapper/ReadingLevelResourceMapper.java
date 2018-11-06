package com.mpen.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.ReadingLevelResource;

@Mapper
public interface ReadingLevelResourceMapper {

    /**
     * 根据阅读等级获取相应资源
     * @param level
     * @return
     */
    @Select("SELECT * FROM DDB_READING_LEVEL_RESOURCE WHERE LEVEL=#{level}")
    ReadingLevelResource getByLevel(Integer level);
}
