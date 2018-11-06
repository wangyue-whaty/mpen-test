package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 班级信息
 * 涉及：教师端以及app2.0对班级信息进行新增，查询
 * @author hzy
 * @since 2018-07-03
 */
public class DdbUserClass implements Serializable {
    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 班级名称
     */
	private String className;
 
    /**
     * 英语老师
     */
	private String englishTeacher;
    /**
     * 教材版本
     */
	private String textbookEdition;
    /**
     * 邀请码
     */
	private String invitationCode;
    /**
     * 学校
     */
	private String school;
	
	private String fkLoginId;
	/**
     * 默认0:教师添加1:无老师添加(无邀请码,不允许其他人加入)
     */
    private int type;

    private Date createTime;

    private Date updateTime;
    // 班级编号
    private String classNumber;
    
    //班级人数
    private int classSize;
    
	public int getClassSize() {
        return classSize;
    }

    public void setClassSize(int classSize) {
        this.classSize = classSize;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEnglishTeacher() {
		return englishTeacher;
	}

	public void setEnglishTeacher(String englishTeacher) {
		this.englishTeacher = englishTeacher;
	}

	public String getTextbookEdition() {
		return textbookEdition;
	}

	public void setTextbookEdition(String textbookEdition) {
		this.textbookEdition = textbookEdition;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getFkLoginId() {
		return fkLoginId;
	}

	public void setFkLoginId(String fkLoginId) {
		this.fkLoginId = fkLoginId;
	}

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }
}