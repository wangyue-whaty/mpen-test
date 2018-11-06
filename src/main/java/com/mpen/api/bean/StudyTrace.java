/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;
import java.util.TreeSet;

public final class StudyTrace implements Serializable {
    private static final long serialVersionUID = 8316723801702069899L;
    private String date;
    private TreeSet<String> learningPages;
    private TreeSet<String> speakingPages;

    public StudyTrace() {
        learningPages = new TreeSet<String>(new StudyTraceComparator());
        speakingPages = new TreeSet<String>(new StudyTraceComparator());
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TreeSet<String> getLearningPages() {
        return learningPages;
    }

    public TreeSet<String> getSpeakingPages() {
        return speakingPages;
    }

    public void setLearningPages(TreeSet<String> learningPages) {
        this.learningPages = learningPages;
    }

    public void setSpeakingPages(TreeSet<String> speakingPages) {
        this.speakingPages = speakingPages;
    }

}
