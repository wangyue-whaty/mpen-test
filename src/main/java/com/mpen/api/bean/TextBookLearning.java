package com.mpen.api.bean;

import java.util.ArrayList;

import com.mpen.api.bean.EnglishBookContent.Activity;

import javafx.beans.binding.StringBinding;
/**
 * 教师端涉及:课本学习详情  bean
 * @author whaty
 */
public class TextBookLearning {
    // 书籍ID
    private String bookId;
    // 书籍名
    private String bookName;
    // 模块
    private String model;
    // 单元名字
    private String name;
    // 单元
    private String unit;
    // unit平均点读次数
    private int classUnitAvgCountTimes;
    // unit平均平均分数
    private float classUnitAvgScore;
    // 0课本点读 1 口语评测
    private String type;
    // activities
    private ArrayList<Activity> activities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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

    public int getClassUnitAvgCountTimes() {
        return classUnitAvgCountTimes;
    }

    public void setClassUnitAvgCountTimes(int classUnitAvgCountTimes) {
        this.classUnitAvgCountTimes = classUnitAvgCountTimes;
    }

    public float getClassUnitAvgScore() {
        return classUnitAvgScore;
    }

    public void setClassUnitAvgScore(float classUnitAvgScore) {
        this.classUnitAvgScore = classUnitAvgScore;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
