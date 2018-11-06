package com.mpen.api.bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class DataAnalysisResult {
    public String date;
    public int clickTimes;
    public int userCount;
    public List<BookRanking> bookList;
    public List<Version> romVersion;
    public List<Version> appVersion;
    public TimeStudying[] timeStudying;

    public static final class BookRanking {
        public String name;
        public int number;
    }

    public static final class Version {
        public String name;
        public int value;
    }

    public static final class TimeStudying {
        public Set<String> user;
        public int clickNum;

        public TimeStudying() {
            user = new HashSet<>();
        }
    }
}
