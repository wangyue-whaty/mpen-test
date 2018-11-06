package com.mpen.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.EnumTypeHandler;

import com.mpen.api.domain.PCVersionUpdate;
/*
 * PC系统Mapper层
 */
@Mapper
public interface PCVersionUpdateMapper {

    /*
     * 园丁系统查询更新数据
     */
	@Result(column = "TYPE", property = "type", typeHandler = EnumTypeHandler.class)
	@Select("SELECT * FROM PC_VERSION_UPDATE WHERE TYPE=#{type} ORDER BY VERSION_CODE DESC LIMIT 1")
	PCVersionUpdate get(PCVersionUpdate.Type type);

}
