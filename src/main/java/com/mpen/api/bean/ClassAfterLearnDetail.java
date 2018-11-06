package com.mpen.api.bean;

import java.io.Serializable;
import java.util.List;

/**
 * app2.0涉及:课后作业详情 bean
 */
public class ClassAfterLearnDetail implements Serializable {

    private static final long serialVersionUID = -2514756801347832959L;
    // 课后作业类型 1课前导学 2课后作业
    private String type;
    // 书籍名称
    private String textbookName;
    // 作业详情
    private List<BookWork> bookWorks;

    public final static class BookWork {
        // 单元
        private String unit;
        // 模块
        private String model;
        // 活动名称
        private String activityName;
        // 点读次数
        private int number;
        // 分钟
        private String minute;

        private List<Sentence> sentences;

        public List<Sentence> getSentences() {
            return sentences;
        }

        public void setSentences(List<Sentence> sentences) {
            this.sentences = sentences;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getMinute() {
            return minute;
        }

        public void setMinute(String minute) {
            this.minute = minute;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTextbookName() {
        return textbookName;
    }

    public void setTextbookName(String textbookName) {
        this.textbookName = textbookName;
    }

    public List<BookWork> getBookWorks() {
        return bookWorks;
    }

    public void setBookWorks(List<BookWork> bookWorks) {
        this.bookWorks = bookWorks;
    }

}
