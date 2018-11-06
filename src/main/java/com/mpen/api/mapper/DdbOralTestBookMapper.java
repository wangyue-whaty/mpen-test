package com.mpen.api.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Select;

import com.mpen.api.domain.DdbOraltestBook;

public interface DdbOralTestBookMapper {
	 @Select("SELECT * FROM DDB_ORAL_TEST_BOOK ")
	 public ArrayList<DdbOraltestBook> getDdbOralTestBooks();
	 
	 @Select("SELECT * FROM DDB_ORAL_TEST_BOOK WHERE BOOKID=#{bookId}")
	 public DdbOraltestBook getDdbOraltestBook(String bookId);
}
