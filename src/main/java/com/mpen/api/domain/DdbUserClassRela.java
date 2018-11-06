package com.mpen.api.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 班级学生信息 domain
 * 涉及：教师端查询班级学生信息以及app2.0学生查询某个班级信息
 *
 * @author hzy
 * @since 2018-07-03
 */
public class DdbUserClassRela implements Serializable {
    
    private static final long serialVersionUID = 5557326457346237297L;
    
    private String Id;
    /**
     * 班级表外键
     */
	private String fkClassId;
    /**
     * 用户表LOGIN_ID
     */
	private String fkLoginId;
    // 学生真实姓名
    private String trueName;
    // 创建时间
    private Date createTime;
    // 更新时间
    private Date updateTime;
	
	private String photo;

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public String getFkClassId() {
		return fkClassId;
	}

	public void setFkClassId(String fkClassId) {
		this.fkClassId = fkClassId;
	}

    public String getFkLoginId() {
        return fkLoginId;
    }

    public void setFkLoginId(String fkLoginId) {
        this.fkLoginId = fkLoginId;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}