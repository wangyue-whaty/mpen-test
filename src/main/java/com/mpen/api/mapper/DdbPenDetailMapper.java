package com.mpen.api.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.mpen.api.domain.DdbPenDetail;

@Mapper
public interface DdbPenDetailMapper {

    /**
     * 查询教师笔版本信息
     * @return
     */
    @Select("SELECT * FROM DDB_PEN_DETAIL ORDER BY VERSION DESC LIMIT 1")
    DdbPenDetail get();

    /**
     * 保存教师笔版本信息
     * @param bookDetail
     */
    @Insert("INSERT INTO DDB_PEN_DETAIL (VERSION,PEN_INFOS) VALUES(#{version},#{penInfos})")
    boolean save(DdbPenDetail penDetail);
}
