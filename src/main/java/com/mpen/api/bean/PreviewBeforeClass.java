package com.mpen.api.bean;

import org.springframework.web.multipart.MultipartFile;

/*
 * 教师端所涉及：课前导学内容 bean
 */
public class PreviewBeforeClass {
    // 课前导学内容
    public PreviewContent previewContent;
    // 截止日期
    public String endDate;
    // 班级
    public String className;
    // 课前导学资源文件
    public MultipartFile[] files;
    public String action;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    public PreviewContent getPreviewContent() {
        return previewContent;
    }

    public void setPreviewContent(PreviewContent previewContent) {
        this.previewContent = previewContent;
    }

}
