package com.mpen.api.bean;

/**
 * 教师端以及app2.0所涉及：布置作业资源url bean
 *
 */
public class HomeworkResourceUrl {
    // 资源url
    private String url;
    // 类型 音频：mp3 音频：mp4 图片： png/jpg
    private String type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
