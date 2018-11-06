package com.mpen.api.bean;

import java.util.List;

/**
 * 用户总积分集合
 * App2.0 积分相关
 */
public class UserIntegralSet {
    // 用户姓名
    private String nickName;
    // 用户头像
    private String photoUrl;
    private String loginId;
    // 用户班级
    private String className;
    // 封面
    private String cover;
    // 用户排行
    private int rank;
    // 用户排行所在比例
    private double proportion;
    // 用户积分记录
    private List<IntegralRecord> integralRecords;
    private String monthDate;
    // 积分总分
    private int integralSum;
    // 点赞数量
    private int praiseNum;
    // 点赞者
    private String praiseLoginId;
    // 被点赞者
    private String byPraiseLoginId;
    // 点赞状态0 未点赞过 1点赞过
    private int state;
    // 好友备注
    private String aliasUser;
    private String action;
    // 增加积分,暂时设置50积分
    private int AddIntegral = 50;
    // 超越用户
    private int beyondRank;
    // 点赞类型 0 积分总榜 1 积分榜
    private String type;

    public String getAliasUser() {
        return aliasUser;
    }

    public void setAliasUser(String aliasUser) {
        this.aliasUser = aliasUser;
    }

    public List<IntegralRecord> getIntegralRecords() {
        return integralRecords;
    }

    public void setIntegralRecords(List<IntegralRecord> integralRecords) {
        this.integralRecords = integralRecords;
    }

    public String getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(String monthDate) {
        this.monthDate = monthDate;
    }

    public int getIntegralSum() {
        return integralSum;
    }

    public void setIntegralSum(int integralSum) {
        this.integralSum = integralSum;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setIntegralSum(Integer integralSum) {
        this.integralSum = integralSum;
    }

    public void setPraiseNum(Integer praiseNum) {
        this.praiseNum = praiseNum;
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPraiseLoginId() {
        return praiseLoginId;
    }

    public void setPraiseLoginId(String praiseLoginId) {
        this.praiseLoginId = praiseLoginId;
    }

    public String getByPraiseLoginId() {
        return byPraiseLoginId;
    }

    public void setByPraiseLoginId(String byPraiseLoginId) {
        this.byPraiseLoginId = byPraiseLoginId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getAddIntegral() {
        return AddIntegral;
    }

    public int getBeyondRank() {
        return beyondRank;
    }

    public void setBeyondRank(int beyondRank) {
        this.beyondRank = beyondRank;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
