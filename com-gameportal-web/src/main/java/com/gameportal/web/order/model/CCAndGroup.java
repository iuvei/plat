package com.gameportal.web.order.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.web.user.model.BaseEntity;

public class CCAndGroup extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "CCAndGroup";
	public static final String ALIAS_ID = "主键ID";
	public static final String ALIAS_CCID = "银行卡ID";
	public static final String ALIAS_CCGID = "银行卡分组ID";
	
	
	//columns START
	private java.lang.Long id;
	private java.lang.Long ccid;
	private java.lang.Long ccgid;
	//columns END

	public CCAndGroup(){
	}

	public CCAndGroup(
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
	
	public void setCcid(java.lang.Long value) {
		this.ccid = value;
	}
	
	public java.lang.Long getCcid() {
		return this.ccid;
	}
	
	public void setCcgid(java.lang.Long value) {
		this.ccgid = value;
	}
	
	public java.lang.Long getCcgid() {
		return this.ccgid;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Id",getId())
			.append("Ccid",getCcid())
			.append("Ccgid",getCcgid())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.append(getCcid())
			.append(getCcgid())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof CCAndGroup == false) return false;
		if(this == obj) return true;
		CCAndGroup other = (CCAndGroup)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.append(getCcid(),other.getCcid())
			.append(getCcgid(),other.getCcgid())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.id;
	}
}

