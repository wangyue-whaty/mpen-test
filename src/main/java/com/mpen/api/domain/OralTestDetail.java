package com.mpen.api.domain;

import java.io.Serializable;
import java.time.Instant;

public class OralTestDetail implements Serializable {
    private static final long serialVersionUID = 6894673364057308314L;
    private int id;
    private String loginId;
    private String fkBookId;
    private String penId;
    private int num;
    private Instant uploadTime;
    private String recordingUrl;
    private String recognizeTxt;
    private double score;
    // 流利度
    private double fluency;
    // 完整度
    private double integrity;
    // 标准度
    private double pronunciation;
    private Instant answerPenTime;
    // 是否已处理0：未处理；1：已处理
    private int isDeal;
    // 由哪台服务处理
    private int shardNum;
    // 评测次数
    private int times;
    // 试卷编号
    private int serialNumber;
    //评测类型  0 云知声  1 驰声 
    private int assessmentType;
    // 最大分数
    private double maxScore;
    // 最小分数
    private double minScore;
    // 平均分数
    private double avgScore;
    // 班级每个小题平均流利度
    public double avgFluency;
    // 班级每个小题平均标准度
    public double avgIntegrity;
    // 班级每个小题平均标准度
    public double avgPronunciation;


    public double getFluency() {
        return fluency;
    }

    public void setFluency(double fluency) {
        this.fluency = fluency;
    }

    public double getIntegrity() {
        return integrity;
    }

    public void setIntegrity(double integrity) {
        this.integrity = integrity;
    }

    public double getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(double pronunciation) {
        this.pronunciation = pronunciation;
    }

    public int getIsDeal() {
        return isDeal;
    }

    public void setIsDeal(int isDeal) {
        this.isDeal = isDeal;
    }

    public int getShardNum() {
        return shardNum;
    }

    public void setShardNum(int shardNum) {
        this.shardNum = shardNum;
    }

    public Instant getAnswerPenTime() {
        return answerPenTime;
    }

    public void setAnswerPenTime(Instant answerPenTime) {
        this.answerPenTime = answerPenTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPenId() {
        return penId;
    }

    public void setPenId(String penId) {
        this.penId = penId;
    }

    public String getFkBookId() {
        return fkBookId;
    }

    public void setFkBookId(String fkBookId) {
        this.fkBookId = fkBookId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Instant getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Instant uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getRecordingUrl() {
        return recordingUrl;
    }

    public void setRecordingUrl(String recordingUrl) {
        this.recordingUrl = recordingUrl;
    }

    public String getRecognizeTxt() {
        return recognizeTxt;
    }

    public void setRecognizeTxt(String recognizeTxt) {
        this.recognizeTxt = recognizeTxt;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(int assessmentType) {
        this.assessmentType = assessmentType;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public double getMinScore() {
        return minScore;
    }

    public void setMinScore(double minScore) {
        this.minScore = minScore;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    public double getAvgFluency() {
        return avgFluency;
    }

    public void setAvgFluency(double avgFluency) {
        this.avgFluency = avgFluency;
    }

    public double getAvgIntegrity() {
        return avgIntegrity;
    }

    public void setAvgIntegrity(double avgIntegrity) {
        this.avgIntegrity = avgIntegrity;
    }

    public double getAvgPronunciation() {
        return avgPronunciation;
    }

    public void setAvgPronunciation(double avgPronunciation) {
        this.avgPronunciation = avgPronunciation;
    }
    
    
}
