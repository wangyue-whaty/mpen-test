package com.mpen.api.common;

import java.io.Serializable;

public class Progress implements Serializable {
    private static final long serialVersionUID = -7076796735750477251L;
    public String uuid;
    public String version;
    public int allItems;
    public int nowItems;
    public boolean finish; // 是否完成
    public String msg;// 提示信息

    public Progress(String uuid, int allItems, int nowItems, boolean finish, String msg,String version) {
        this.uuid = uuid;
        this.allItems = allItems;
        this.nowItems = nowItems;
        this.finish = finish;
        this.msg = msg;
        this.version = version;
    }

}
