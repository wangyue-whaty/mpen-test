package com.mpen.api.bean;

import java.util.ArrayList;
import java.util.List;
/**
 * 教师端涉及:课前导学详情 bean
 */
public class PreviewContent {
    // 课前导学名称
    private  String name;
    // 课前导学任务
    private String task;
    // 课前导学音频，图片 urls
    private List<HomeworkResourceUrl> urls;
    // 不限 图片 音频 视频
    private ArrayList<String> submits;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public ArrayList<String> getSubmits() {
        return submits;
    }

    public void setSubmits(ArrayList<String> submits) {
        this.submits = submits;
    }

    public List<HomeworkResourceUrl> getUrls() {
        return urls;
    }

    public void setUrls(List<HomeworkResourceUrl> urls) {
        this.urls = urls;
    }


}
