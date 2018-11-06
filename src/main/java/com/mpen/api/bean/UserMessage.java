package com.mpen.api.bean;

import java.io.Serializable;
import java.util.Map;

import com.mpen.api.common.Constants;

/**
 * 
 * 用户消息 涉及：app2.0消息模块
 * 
 * @author wangyue
 * @date 2018年8月21日 下午5:40:22
 */
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 1896959633475400391L;
    private String action;
    private String loginId;
    private String type;
    private String content;
    private Map<String, String> msg;
    // 消息id
    private String id;
    // 当前页码
    private int pageNo = Constants.DEFAULT_PAGENO;
    // 每页的数据大小
    private int pageSize = Constants.MAX_PAGESIZE;
    // 当前页面所对应数据库中的最后一条消息位置
    private int pageIndex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return (this.pageNo - 1) * this.pageSize;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
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

    public Map<String, String> getMsg() {
        return msg;
    }

    public void setMsg(Map<String, String> msg) {
        this.msg = msg;
    }
}
