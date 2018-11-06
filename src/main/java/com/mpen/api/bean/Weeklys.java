package com.mpen.api.bean;

import java.io.Serializable;
import java.util.List;

public final class Weeklys implements Serializable {
    private static final long serialVersionUID = -6399677933037782237L;
    private int month;
    private List<WeeklyDetail> weeklyList;

    public final int getMonth() {
        return month;
    }

    public final void setMonth(int month) {
        this.month = month;
    }

    public final List<WeeklyDetail> getWeeklyList() {
        return weeklyList;
    }

    public final void setWeeklyList(List<WeeklyDetail> weeklyList) {
        this.weeklyList = weeklyList;
    }

    public Weeklys() {
        super();
    }

    public Weeklys(int month, List<WeeklyDetail> weeklyList) {
        super();
        this.month = month;
        this.weeklyList = weeklyList;
    }

}
