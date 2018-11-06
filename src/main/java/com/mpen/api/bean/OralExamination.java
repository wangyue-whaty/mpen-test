package com.mpen.api.bean;

import java.util.List;

import com.mpen.api.bean.OralTestInfo.OralTestPaper;

public class OralExamination {
    // 书籍id
    private String bookId;
    // 口语考试卷名字
    private String name;
    private List<OralTestPaper> examPaper;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public List<OralTestPaper> getExamPaper() {
        return examPaper;
    }

    public void setExamPaper(List<OralTestPaper> examPaper) {
        this.examPaper = examPaper;
    }
}
