package com.mpen.api.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mpen.api.domain.DdbResourceVideo;

/**
 * 学生作业
 *
 */
public final class HomeWorkInfo implements Serializable {

    private static final long serialVersionUID = -5833144843208669999L;

    // 作业id
    private String id;
    // 作业状态
    private String status;
    // 作业名称
    private String name;
    // 截止时间
    private long endDateInMs;
    // 完成时间(有null的情况,例如还未完成作业时,completeDate为null)
    private Long completeDateInMs;
    // 老师评语
    private String comment;
    // 作业布置时间戳
    private long timeInMs;
    // 反馈类型
    public ArrayList<String> submits;

    private List<ActivityVideo> activityVideos;

    public final static class ActivityVideo {
        private String item;
        private List<DdbResourceVideo> videos;

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public List<DdbResourceVideo> getVideos() {
            return videos;
        }

        public void setVideos(List<DdbResourceVideo> videos) {
            this.videos = videos;
        }
    }

    public List<ActivityVideo> getActivityVideos() {
        return activityVideos;
    }

    public void setActivityVideos(List<ActivityVideo> activityVideos) {
        this.activityVideos = activityVideos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getSubmits() {
        return submits;
    }

    public void setSubmits(ArrayList<String> submits) {
        this.submits = submits;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getEndDateInMs() {
        return endDateInMs;
    }

    public void setEndDateInMs(long endDateInMs) {
        this.endDateInMs = endDateInMs;
    }

    public Long getCompleteDateInMs() {
        return completeDateInMs;
    }

    public void setCompleteDateInMs(Long completeDateInMs) {
        this.completeDateInMs = completeDateInMs;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTimeInMs() {
        return timeInMs;
    }

    public void setTimeInMs(long timeInMs) {
        this.timeInMs = timeInMs;
    }

}
