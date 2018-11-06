/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;
import java.util.List;

public final class Unit implements Serializable {
    private static final long serialVersionUID = 1068540390964941399L;
    private String model;
    private String unit;
    private String name;
    private String date;
    private int learn;
    private List<Activity> activities;

    public String getModel() {
        return model;
    }

    public String getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getLearn() {
        return learn;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLearn(int learn) {
        this.learn = learn;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
