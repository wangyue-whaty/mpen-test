package com.mpen.api.bean;

import java.util.List;

import com.mp.shared.common.Exam;
/**
 * 教师端以及app2.0涉及:口语考试卷详情 bean
 */
public class ClassAfterExamDetail {
    // 口语考试卷书籍id
    private String bookId;
    // 名字
    private String name;
    // 内容
    private String content;
    // 口语考试卷卷子
    List<ExamPaper> examPapers;
    // 口语考试卷卷子详情
    List<Exam> exams;
    // 用户口语考试卷详情
    List<ExamResult> oralExams;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExamPaper> getExamPapers() {
        return examPapers;
    }

    public void setExamPapers(List<ExamPaper> examPapers) {
        this.examPapers = examPapers;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public List<ExamResult> getOralExams() {
        return oralExams;
    }

    public void setOralExams(List<ExamResult> oralExams) {
        this.oralExams = oralExams;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
}
