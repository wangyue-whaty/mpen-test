package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbUserComment;
/**
 * 固定字典评论  Mapper 
 * 涉及：教师端查询固定字典评论信息 
 *
 */
@Mapper
public interface DdbUserCommentsMapper {

    @Select("SELECT * FROM DDB_USER_COMMENTS ")
    List<DdbUserComment> getAllComments();
}