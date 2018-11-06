package com.mpen.api.domain;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.codec.digest.DigestUtils;

import com.mpen.api.bean.FileParam;
import com.mpen.api.util.CommUtil;

public class PersistenceFile implements Serializable {
    private static final long serialVersionUID = 5927976701281185069L;
    private String id;
    // 所属项目
    private ProjectType project;
    // 文件类型/用途，项目相关
    private String type;
    // 文件内容（文本，二进制），大概大小（K级别，10K，100K，1M，10M，以上）
    private String property;
    // 储存在文件目录，还是数据库里面
    private StoreType storeType;
    // 上传时间
    private long uploadTime;
    // 上传时IP地址
    private String address;
    // createdTime：文件生成时间
    private long createTime;
    // 优先级
    private int priority;
    // 版本（可选）
    private String version;
    // 用户ID
    private String user;
    // 文件大小
    private long size;
    // 文件hash（md5，或者SHA）
    private String hash;
    // 一个json数据结构，根据 project/type 来规划
    private String optional;

    // 数据存储类型
    public enum StoreType {
        FILE, DATABASE
    }

    public enum ProjectType {
        GARDENER, PUBLISH, ANDROID_PEN, LINUX_PEN, ANDROID_PHONE, IOS_PHONE
    }

    public PersistenceFile() {
    }

    public PersistenceFile(FileParam param, String fileSavePath, String adress) throws IOException {
        this.id = CommUtil.genRecordKey();
        this.project = param.getProgect();
        this.type = param.getType();
        this.property = fileSavePath;
        this.storeType = StoreType.FILE;
        this.uploadTime = System.currentTimeMillis();
        this.address = adress;
        this.createTime = param.getCreateTime();
        this.priority = param.getPriority();
        this.version = param.getVersion();
        this.user = param.getUser();
        this.size = param.getFile().getSize();
        this.hash = DigestUtils.md5Hex(param.getFile().getBytes());
        this.optional = param.getOptional();
    }

    public ProjectType getProject() {
        return project;
    }

    public void setProject(ProjectType project) {
        this.project = project;
    }

    public StoreType getStoreType() {
        return storeType;
    }

    public void setStoreType(StoreType storeType) {
        this.storeType = storeType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }
}
