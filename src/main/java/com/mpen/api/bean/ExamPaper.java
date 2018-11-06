package com.mpen.api.bean;

public class ExamPaper {
    // 口语考试卷书籍id
    private String bookId;
    // 卷子编号
    private int pageNum;
    // 试卷名称
    private String name;
    // 试卷状态 0 未做   1 已做
    private int status;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
