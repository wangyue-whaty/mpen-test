package com.mpen.api.bean;

import java.util.ArrayList;
import java.util.List;
/**
 *教师端涉及: 布置作业 bean
 */
public class ClassLearnContent {
    // 标题
    private String title;
    // 课本学习
    private List<TextBookLearning> textBookLearnings;
    // 口语考试
    private List<OralExamination> oralExaminations;
    // 班级平均点读次数
    private int classAvgCountTimes;
    // 班级平均分数
    private float classAvgScore;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TextBookLearning> getTextBookLearnings() {
        return textBookLearnings;
    }

    public void setTextBookLearnings(List<TextBookLearning> textBookLearnings) {
        this.textBookLearnings = textBookLearnings;
    }

    public void setOralExaminations(ArrayList<OralExamination> oralExaminations) {
        this.oralExaminations = oralExaminations;
    }
   
    public List<OralExamination> getOralExaminations() {
        return oralExaminations;
    }

    public void setOralExaminations(List<OralExamination> oralExaminations) {
        this.oralExaminations = oralExaminations;
    }

    public int getClassAvgCountTimes() {
        return classAvgCountTimes;
    }

    public void setClassAvgCountTimes(int classAvgCountTimes) {
        this.classAvgCountTimes = classAvgCountTimes;
    }

    public float getClassAvgScore() {
        return classAvgScore;
    }

    public void setClassAvgScore(float classAvgScore) {
        this.classAvgScore = classAvgScore;
    }

}
