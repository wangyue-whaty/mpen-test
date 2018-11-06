package com.mpen.api.domain;
/**
 *  勋章字典表
 *  涉及:教师端以及app2.0涉及勋章相关接口
 *
 */
public class DdbUserMedalDictionary {
    private String id;
    // 勋章类型
    private String medalType;
    // 勋章名字
    private String medalName;
    // 勋章动态
    private String medalDynamic;
    private String createTime;
    private String updateTime;
    // 勋章口号
    private String slogan;
    // 获取勋章规则
    private String medalRule;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedalType() {
        return medalType;
    }

    public void setMedalType(String medalType) {
        this.medalType = medalType;
    }

    public String getMedalName() {
        return medalName;
    }

    public void setMedalName(String medalName) {
        this.medalName = medalName;
    }

    public String getMedalDynamic() {
        return medalDynamic;
    }

    public void setMedalDynamic(String medalDynamic) {
        this.medalDynamic = medalDynamic;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getMedalRule() {
        return medalRule;
    }

    public void setMedalRule(String medalRule) {
        this.medalRule = medalRule;
    }
}
