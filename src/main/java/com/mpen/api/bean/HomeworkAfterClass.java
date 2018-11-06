package com.mpen.api.bean;

import java.util.Date;
import java.util.List;

/**
 * 教师端涉及：课后作业 bean
 */

public class HomeworkAfterClass {
    // 课后作业内容
    private ClassLearnContent content;
    // 截止日期
    private String endDate;
    // 班级
    private String className;
    // 年级
    private String grade;
    // 留言
    private String remark;
    // 布置英语书籍内容
    private List<EnglishBookContent> englishBookContents;
  
    private String action;

    public ClassLearnContent getContent() {
        return content;
    }

    public void setContent(ClassLearnContent content) {
        this.content = content;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

  

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<EnglishBookContent> getEnglishBookContents() {
        return englishBookContents;
    }

    public void setEnglishBookContents(List<EnglishBookContent> englishBookContents) {
        this.englishBookContents = englishBookContents;
    }

    

}
