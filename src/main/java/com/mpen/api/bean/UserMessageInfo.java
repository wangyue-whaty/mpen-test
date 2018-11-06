package com.mpen.api.bean;

/**
 * 
 * 用户消息分装bean，往前台返回的格式化数据bean
 * 
 * @author sxg
 * @date 2018年8月21日
 */
public class UserMessageInfo {
    // 消息id
    private String id;
    // 用户id
    private String loginId;
    // 消息类型
    private String type;
    // 消息内容
    private String content;
    // 是否读取
    private int isRead;
    // 是否删除
    private int isDel;
    // 创建时间
    private long createTimeInMs;
    // 消息具体类型，给app标识跳转页面
    private String typeDetail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getloginId() {
        return loginId;
    }

    public void setloginId(String loginId) {
        this.loginId = loginId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public long getCreateTimeInMs() {
        return createTimeInMs;
    }

    public void setCreateTimeInMs(long createTimeInMs) {
        this.createTimeInMs = createTimeInMs;
    }

    public String getTypeDetail() {
        return typeDetail;
    }

    public void setTypeDetail(String typeDetail) {
        this.typeDetail = typeDetail;
    }

}
