package com.gameportal.web.order.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.web.user.model.BaseEntity;

public class CCGroup extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "CCGroup";
	public static final String ALIAS_CCGID = "银行卡分组ID";
	public static final String ALIAS_TYPE = "分组类型 0会员星级分组 1自定义分组";
	public static final String ALIAS_GRADE = "会员星级";
	public static final String ALIAS_NAME = "分组名称";
	public static final String ALIAS_DESCRIPT = "分组描述";
	public static final String ALIAS_CREATEUSERID = "创建者ID";
	public static final String ALIAS_CREATEUSERNAME = "创建者名称";
	public static final String ALIAS_CREATETIME = "创建时间";
	public static final String ALIAS_UPDATEUSERID = "修改者ID";
	public static final String ALIAS_UPDATEUSERNAME = "修改者名称";
	public static final String ALIAS_UPDATETIME = "修改时间";
	
	
	//columns START
	private java.lang.Long ccgid;
	private java.lang.Integer type;
	private java.lang.Integer grade;
	private java.lang.String name;
	private java.lang.String descript;
	private java.lang.Long createuserid;
	private java.lang.String createusername;
	private java.sql.Timestamp createtime;
	private java.lang.Long updateuserid;
	private java.lang.String updateusername;
	private java.sql.Timestamp updatetime;
	//columns END

	public CCGroup(){
	}

	public CCGroup(
		java.lang.Long ccgid
	){
		this.ccgid = ccgid;
	}

	
	public void setCcgid(java.lang.Long value) {
		this.ccgid = value;
	}
	
	public java.lang.Long getCcgid() {
		return this.ccgid;
	}
	
	public void setType(java.lang.Integer value) {
		this.type = value;
	}
	
	public java.lang.Integer getType() {
		return this.type;
	}
	
	public void setGrade(java.lang.Integer value) {
		this.grade = value;
	}
	
	public java.lang.Integer getGrade() {
		return this.grade;
	}
	
	public void setName(java.lang.String value) {
		this.name = value;
	}
	
	public java.lang.String getName() {
		return this.name;
	}
	
	public void setDescript(java.lang.String value) {
		this.descript = value;
	}
	
	public java.lang.String getDescript() {
		return this.descript;
	}
	
	public void setCreateuserid(java.lang.Long value) {
		this.createuserid = value;
	}
	
	public java.lang.Long getCreateuserid() {
		return this.createuserid;
	}
	
	public void setCreateusername(java.lang.String value) {
		this.createusername = value;
	}
	
	public java.lang.String getCreateusername() {
		return this.createusername;
	}
	
	public void setCreatetime(java.sql.Timestamp value) {
		this.createtime = value;
	}
	
	public java.sql.Timestamp getCreatetime() {
		return this.createtime;
	}
	
	public void setUpdateuserid(java.lang.Long value) {
		this.updateuserid = value;
	}
	
	public java.lang.Long getUpdateuserid() {
		return this.updateuserid;
	}
	
	public void setUpdateusername(java.lang.String value) {
		this.updateusername = value;
	}
	
	public java.lang.String getUpdateusername() {
		return this.updateusername;
	}
	
	public void setUpdatetime(java.sql.Timestamp value) {
		this.updatetime = value;
	}
	
	public java.sql.Timestamp getUpdatetime() {
		return this.updatetime;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Ccgid",getCcgid())
			.append("Type",getType())
			.append("Grade",getGrade())
			.append("Name",getName())
			.append("Descript",getDescript())
			.append("Createuserid",getCreateuserid())
			.append("Createusername",getCreateusername())
			.append("Createtime",getCreatetime())
			.append("Updateuserid",getUpdateuserid())
			.append("Updateusername",getUpdateusername())
			.append("Updatetime",getUpdatetime())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getCcgid())
			.append(getType())
			.append(getGrade())
			.append(getName())
			.append(getDescript())
			.append(getCreateuserid())
			.append(getCreateusername())
			.append(getCreatetime())
			.append(getUpdateuserid())
			.append(getUpdateusername())
			.append(getUpdatetime())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof CCGroup == false) return false;
		if(this == obj) return true;
		CCGroup other = (CCGroup)obj;
		return new EqualsBuilder()
			.append(getCcgid(),other.getCcgid())
			.append(getType(),other.getType())
			.append(getGrade(),other.getGrade())
			.append(getName(),other.getName())
			.append(getDescript(),other.getDescript())
			.append(getCreateuserid(),other.getCreateuserid())
			.append(getCreateusername(),other.getCreateusername())
			.append(getCreatetime(),other.getCreatetime())
			.append(getUpdateuserid(),other.getUpdateuserid())
			.append(getUpdateusername(),other.getUpdateusername())
			.append(getUpdatetime(),other.getUpdatetime())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.ccgid;
	}
}

