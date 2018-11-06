/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbLearnWordStructureDetail;

@Mapper
public interface LearnWordStructureDetailMapper {
	@Select("SELECT * FROM DDB_LEARN_WORD_STRUCTURE_DETAIL ORDER BY VERSION DESC LIMIT 1")
	DdbLearnWordStructureDetail get();

	@Insert("INSERT INTO DDB_LEARN_WORD_STRUCTURE_DETAIL (VERSION,LEARN_WORD_STRUCTURE_INFOS) VALUES(#{version},#{learnWordStructureInfos})")
	void save(DdbLearnWordStructureDetail bookDetail);

}
