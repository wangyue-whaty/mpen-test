/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;
import java.util.List;

/**
 * TODO 推送消息参数bean.
 * 
 * @author zyt
 *
 */
public final class Message implements Serializable {
    private static final long serialVersionUID = 1359002224941960782L;
    private String action;
    // TODO 1）如果是String，用enum，然后enum.name()这样或者enum.valueOf并且，对于String，可以用switch
    // 2）如果是int；要定义 public static final int XXX 需要统一修改codeStyle
    private String type;
    private String battery;
    private String path;
    private List<Object> videos;

    public final List<Object> getVideos() {
        return videos;
    }

    public final void setVideos(List<Object> videos) {
        this.videos = videos;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
