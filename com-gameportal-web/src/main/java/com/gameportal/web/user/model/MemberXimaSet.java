package com.gameportal.web.user.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class MemberXimaSet extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "MemberXimaSet";
	public static final String ALIAS_MXSID = "主键ID";
	public static final String ALIAS_GPID = "游戏平台ID";
	public static final String ALIAS_GRADE = "会员星级";
	public static final String ALIAS_SCALE = "返水的比例值（是个小数）";
	public static final String ALIAS_UPDATEUSERID = "更新者ID";
	public static final String ALIAS_UPDATETIME = "更新时间";
	public static final String ALIAS_UPDATEUSERNAME = "更新者名称";
	
	
	//columns START
	private java.lang.Long mxsid;
	private java.lang.Long gpid;
	private java.lang.Integer grade;
	private java.lang.String scale;
	private java.lang.Long updateuserid;
	private java.sql.Timestamp updatetime;
	private java.lang.String updateusername;
	//columns END

	public MemberXimaSet(){
	}

	public MemberXimaSet(
		java.lang.Long mxsid
	){
		this.mxsid = mxsid;
	}

	
	public void setMxsid(java.lang.Long value) {
		this.mxsid = value;
	}
	
	public java.lang.Long getMxsid() {
		return this.mxsid;
	}
	
	public void setGpid(java.lang.Long value) {
		this.gpid = value;
	}
	
	public java.lang.Long getGpid() {
		return this.gpid;
	}
	
	public void setGrade(java.lang.Integer value) {
		this.grade = value;
	}
	
	public java.lang.Integer getGrade() {
		return this.grade;
	}
	
	
	public java.lang.String getScale() {
		return scale;
	}

	public void setScale(java.lang.String scale) {
		this.scale = scale;
	}

	public void setUpdateuserid(java.lang.Long value) {
		this.updateuserid = value;
	}
	
	public java.lang.Long getUpdateuserid() {
		return this.updateuserid;
	}
	
	public void setUpdatetime(java.sql.Timestamp value) {
		this.updatetime = value;
	}
	
	public java.sql.Timestamp getUpdatetime() {
		return this.updatetime;
	}
	
	public void setUpdateusername(java.lang.String value) {
		this.updateusername = value;
	}
	
	public java.lang.String getUpdateusername() {
		return this.updateusername;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Mxsid",getMxsid())
			.append("Gpid",getGpid())
			.append("Grade",getGrade())
			.append("Scale",getScale())
			.append("Updateuserid",getUpdateuserid())
			.append("Updatetime",getUpdatetime())
			.append("Updateusername",getUpdateusername())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getMxsid())
			.append(getGpid())
			.append(getGrade())
			.append(getScale())
			.append(getUpdateuserid())
			.append(getUpdatetime())
			.append(getUpdateusername())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof MemberXimaSet == false) return false;
		if(this == obj) return true;
		MemberXimaSet other = (MemberXimaSet)obj;
		return new EqualsBuilder()
			.append(getMxsid(),other.getMxsid())
			.append(getGpid(),other.getGpid())
			.append(getGrade(),other.getGrade())
			.append(getScale(),other.getScale())
			.append(getUpdateuserid(),other.getUpdateuserid())
			.append(getUpdatetime(),other.getUpdatetime())
			.append(getUpdateusername(),other.getUpdateusername())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.mxsid;
	}
}

