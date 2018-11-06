package com.mpen.api.bean;

/**
 * 学情统计辅助类
 * 用于接收参数loginId,bookId,studyDate查询书籍类型及学习轨迹
 * @author hzy
 *
 */
public class StudyCountAssist {
    private String fkLoginId;
    private String fkBookId;
    // 学习日期
    private String studyDate;
    
    public StudyCountAssist() {
        super();
    }

    public StudyCountAssist(String fkLoginId, String fkBookId, String studyDate) {
        super();
        this.fkLoginId = fkLoginId;
        this.fkBookId = fkBookId;
        this.studyDate = studyDate;
    }

    public String getFkLoginId() {
        return fkLoginId;
    }

    public void setFkLoginId(String fkLoginId) {
        this.fkLoginId = fkLoginId;
    }

    public String getFkBookId() {
        return fkBookId;
    }

    public void setFkBookId(String fkBookId) {
        this.fkBookId = fkBookId;
    }

    public String getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(String studyDate) {
        this.studyDate = studyDate;
    }

}
