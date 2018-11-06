package com.mpen.api.domain;

import java.io.Serializable;

/**
 * 阅读等级对应资源
 * @author wangyue
 *
 */
public class ReadingLevelResource implements Serializable{

    private static final long serialVersionUID = -3870179729023385229L;
    // 阅读等级
    private int level;
    // 书id,一个level对应多本书,书id之间用两个下划线分隔,例:89e8553594774f0886d0762ac47a6965__ff8080816219792a0162ce1a44fa0599
    private String bookIds;
    // 本书阅读等级的说明
    private String text;
    
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public String getBookIds() {
        return bookIds;
    }
    public void setBookIds(String bookIds) {
        this.bookIds = bookIds;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
   
}
