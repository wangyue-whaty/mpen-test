/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;

/**
 * TODO 学习记录返回bean.
 * 
 * @author zyt
 *
 */
public final class DateStudyTime implements Serializable, Comparable<DateStudyTime> {
    private static final long serialVersionUID = 8813457987128522007L;
    private float bookStudyTime;
    private float spokenTestTime;
    private float exercisesTime;
    private float readingTime;
    private float otherTime;
    private float countTime;
    private String date;

    public float getCountTime() {
        return countTime;
    }

    public void setCountTime(float countTime) {
        this.countTime = countTime;
    }

    public float getOtherTime() {
        return otherTime;
    }

    public void setOtherTime(float otherTime) {
        this.otherTime = otherTime;
    }

    public float getBookStudyTime() {
        return bookStudyTime;
    }

    public float getSpokenTestTime() {
        return spokenTestTime;
    }

    public float getExercisesTime() {
        return exercisesTime;
    }

    public float getReadingTime() {
        return readingTime;
    }

    public String getDate() {
        return date;
    }

    public void setBookStudyTime(float bookStudyTime) {
        this.bookStudyTime = bookStudyTime;
    }

    public void setSpokenTestTime(float spokenTestTime) {
        this.spokenTestTime = spokenTestTime;
    }

    public void setExercisesTime(float exercisesTime) {
        this.exercisesTime = exercisesTime;
    }

    public void setReadingTime(float readingTime) {
        this.readingTime = readingTime;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(DateStudyTime o) {
        if (this.getDate().length() > o.getDate().length()) {
            return 1;
        } else if (this.getDate().length() < o.getDate().length()) {
            return -1;
        }
        return this.getDate().compareTo(o.getDate());
    }
}
