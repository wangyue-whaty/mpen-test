package com.mpen.api.bean;

import java.io.Serializable;

public final class WeeklyDetail implements Serializable {
    private static final long serialVersionUID = 8144984504577591068L;
    private String startDate;
    private String endDate;
    private String userName;
    private float totalTime;
    private float studyTime;
    private float readTime;
    private float testTime;
    private float spokenTime;
    private int spokenTimes;
    private float spokenScore;
    private float[] timeArray;

    public final String getStartDate() {
        return startDate;
    }

    public final void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public final String getEndDate() {
        return endDate;
    }

    public final void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public final String getUserName() {
        return userName;
    }

    public final void setUserName(String userName) {
        this.userName = userName;
    }

    public final float getTotalTime() {
        return totalTime;
    }

    public final void setTotalTime(float totalTime) {
        this.totalTime = totalTime;
    }

    public final float getStudyTime() {
        return studyTime;
    }

    public final void setStudyTime(float studyTime) {
        this.studyTime = studyTime;
    }

    public final float getReadTime() {
        return readTime;
    }

    public final void setReadTime(float readTime) {
        this.readTime = readTime;
    }

    public final float getTestTime() {
        return testTime;
    }

    public final void setTestTime(float testTime) {
        this.testTime = testTime;
    }

    public final float getSpokenTime() {
        return spokenTime;
    }

    public final void setSpokenTime(float spokenTime) {
        this.spokenTime = spokenTime;
    }

    public final int getSpokenTimes() {
        return spokenTimes;
    }

    public final void setSpokenTimes(int spokenTimes) {
        this.spokenTimes = spokenTimes;
    }

    public final float getSpokenScore() {
        return spokenScore;
    }

    public final void setSpokenScore(float spokenScore) {
        this.spokenScore = spokenScore;
    }

    public final float[] getTimeArray() {
        return timeArray;
    }

    public final void setTimeArray(float[] timeArray) {
        this.timeArray = timeArray;
    }

}
