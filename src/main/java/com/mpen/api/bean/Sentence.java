/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mp.shared.record.RecognizeTxtParser.RecognizeInfo;

public final class Sentence implements Serializable {
    private static final long serialVersionUID = 5485620821548159196L;
    private String title;
    private int number;
    private float score;
    // 返回给APP端的单词评测信息
    private ArrayList<RecognizeInfo> recognizeInfos;
    
    public String getTitle() {
        return title;
    }

    public int getNumber() {
        return number;
    }

    public float getScore() {
        return score;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setScore(float score) {
        this.score = score;
    }
   
    public ArrayList<RecognizeInfo> getRecognizeInfos() {
        return recognizeInfos;
    }

    public void setRecognizeInfos(ArrayList<RecognizeInfo> recognizeInfos) {
        this.recognizeInfos = recognizeInfos;
    }
    
}
