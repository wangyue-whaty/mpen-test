/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;
import java.util.List;

public final class Activity implements Serializable {
    private static final long serialVersionUID = 1248671001813987152L;
    private String id;
    private int number;
    private String sort;
    private String name;
    private int smile;
    private float duration;
    private List<Sentence> sentences;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSmile() {
        return smile;
    }

    public float getDuration() {
        return duration;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSmile(int smile) {
        this.smile = smile;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

}
