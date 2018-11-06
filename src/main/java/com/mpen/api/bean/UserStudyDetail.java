/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;
import java.util.List;

/**
 * TODO 学习记录返回bean.
 * 
 * @author zyt
 *
 */
public final class UserStudyDetail implements Serializable {
    private static final long serialVersionUID = -3219437125132446616L;
    private List<BookLearningInfo> books;
    private List<Medal> medals;
    private List<DateStudyTime> dateStudyTimes;
    private UserInfo userInfo;

    public List<BookLearningInfo> getBooks() {
        return books;
    }

    public List<Medal> getMedals() {
        return medals;
    }

    public List<DateStudyTime> getDateStudyTimes() {
        return dateStudyTimes;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setBooks(List<BookLearningInfo> books) {
        this.books = books;
    }

    public void setMedals(List<Medal> medals) {
        this.medals = medals;
    }

    public void setDateStudyTimes(List<DateStudyTime> dateStudyTimes) {
        this.dateStudyTimes = dateStudyTimes;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

}
