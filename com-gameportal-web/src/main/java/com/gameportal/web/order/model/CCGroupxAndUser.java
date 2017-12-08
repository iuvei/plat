package com.gameportal.web.order.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.web.user.model.BaseEntity;

public class CCGroupxAndUser extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "CCGroupxAndUser";
	public static final String ALIAS_ID = "主键ID";
	public static final String ALIAS_CCGXID = "自定义银行卡分组ID";
	public static final String ALIAS_UIID = "会员ID";
	public static final String ALIAS_CREATETIME = "创建时间";
	
	
	//columns START
	private java.lang.Long id;
	private java.lang.Long ccgxid;
	private java.lang.Long uiid;
	private java.sql.Timestamp createtime;
	//columns END

	public CCGroupxAndUser(){
	}

	public CCGroupxAndUser(
		java.lang.Long id
	){
		this.id = id;
	}

	
	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getId() {
		return this.id;
	}
	
	public void setCcgxid(java.lang.Long value) {
		this.ccgxid = value;
	}
	
	public java.lang.Long getCcgxid() {
		return this.ccgxid;
	}
	
	public void setUiid(java.lang.Long value) {
		this.uiid = value;
	}
	
	public java.lang.Long getUiid() {
		return this.uiid;
	}
	
	public void setCreatetime(java.sql.Timestamp value) {
		this.createtime = value;
	}
	
	public java.sql.Timestamp getCreatetime() {
		return this.createtime;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Id",getId())
			.append("Ccgxid",getCcgxid())
			.append("Uiid",getUiid())
			.append("Createtime",getCreatetime())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.append(getCcgxid())
			.append(getUiid())
			.append(getCreatetime())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof CCGroupxAndUser == false) return false;
		if(this == obj) return true;
		CCGroupxAndUser other = (CCGroupxAndUser)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.append(getCcgxid(),other.getCcgxid())
			.append(getUiid(),other.getUiid())
			.append(getCreatetime(),other.getCreatetime())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.id;
	}
}

