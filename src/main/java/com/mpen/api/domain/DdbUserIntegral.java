package com.mpen.api.domain;

/**
 * 用户总积分
 * 涉及：教师端以及app2.0涉及积分相关接口
 */
public class DdbUserIntegral {

    private String id;
    // 分数
    private int score;
    // 名次
    private int rank;
    // 点赞数量
    private int praiseNum;
    
    private String fkLoginId;
    // 好友备注
    private String aliasUser;

    public String getFkLoginId() {
        return fkLoginId;
    }

    public void setFkLoginId(String fkLoginId) {
        this.fkLoginId = fkLoginId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getAliasUser() {
        return aliasUser;
    }

    public void setAliasUser(String aliasUser) {
        this.aliasUser = aliasUser;
    }

}
