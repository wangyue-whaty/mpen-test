/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * TODO 学习记录返回bean.
 * 
 * @author zyt
 *
 */
public final class BookStudyInfo implements Serializable {
    private static final long serialVersionUID = 3631084442670999402L;
    private BookLearningInfo bookLearningInfo;
    private Set<DateStudyTime> dateStudyTimes;
    private List<StudyTrace> studyTrace;

    public List<StudyTrace> getStudyTrace() {
        return studyTrace;
    }

    public void setStudyTrace(List<StudyTrace> studyTrace) {
        this.studyTrace = studyTrace;
    }

    public Set<DateStudyTime> getDateStudyTimes() {
        return dateStudyTimes;
    }

    public BookLearningInfo getBookLearningInfo() {
        return bookLearningInfo;
    }

    public void setBookLearningInfo(BookLearningInfo bookLearningInfo) {
        this.bookLearningInfo = bookLearningInfo;
    }

    public void setDateStudyTimes(Set<DateStudyTime> dateStudyTimes) {
        this.dateStudyTimes = dateStudyTimes;
    }
}
