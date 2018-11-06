package com.mpen.api.domain;

/**
 * 
 * 点读笔笔口语考试卷
 *
 */

public class DdbOraltestBook {

    // 口语考试书籍的bookId
    private String bookId;
    // 书籍名字
    private String name;
    // 口语考试卷子数量
    private int num;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
