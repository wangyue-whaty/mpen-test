package com.mpen.api.bean;

import java.io.Serializable;

public final class WeeklyParam implements Serializable {
    private static final long serialVersionUID = -6595679545573576401L;
    private String action;
    private String startDate;
    private String endDate;

    public final String getAction() {
        return action;
    }

    public final void setAction(String action) {
        this.action = action;
    }

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
}
