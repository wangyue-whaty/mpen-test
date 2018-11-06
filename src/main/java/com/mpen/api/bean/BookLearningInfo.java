/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;

import com.mp.shared.common.FullBookInfo;

/**
 * TODO 书籍资源返回bean.
 * 
 * @author zyt
 *
 */
public final class BookLearningInfo implements Serializable {
    private static final long serialVersionUID = -2830650792622825010L;
    private String id;
    private String name;
    private String photo;
    private String lastReading;
    private Float learnedDuration;
    private Float speakingDuration;
    private Integer gold;
    private Integer ranking;
    private FullBookInfo.Type type;

    public FullBookInfo.Type getType() {
        return type;
    }

    public void setType(FullBookInfo.Type type) {
        this.type = type;
    }

    public Integer getGold() {
        return gold;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Float getLearnedDuration() {
        return learnedDuration;
    }

    public Float getSpeakingDuration() {
        return speakingDuration;
    }

    public void setLearnedDuration(Float learnedDuration) {
        this.learnedDuration = learnedDuration;
    }

    public void setSpeakingDuration(Float speakingDuration) {
        this.speakingDuration = speakingDuration;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getLastReading() {
        return lastReading;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setLastReading(String lastReading) {
        this.lastReading = lastReading;
    }
}
