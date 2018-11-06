package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbObtainIntegralInstruction;

/**
 * 积分字典Mapper 涉及：App2.0 积分相关
 */
@Mapper
public interface DdbObtainIntegralInstructionMapper {

    @Select("SELECT * FROM DDB_OBTAIN_INTEGRAL_INSTRUCTION")
    List<DdbObtainIntegralInstruction> findAll();

}
