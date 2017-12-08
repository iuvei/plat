package com.gameportal.manage.betlog.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;


public class CollectLog extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "CollectLog";
	public static final String ALIAS_PID = "pid";
	public static final String ALIAS_FLATFORM_CODE = "flatformCode";
	public static final String ALIAS_KIND = "kind";
	public static final String ALIAS_MESSAGE = "message";
	public static final String ALIAS_CREATE_DATE = "createDate";
	
	
	//columns START
	private java.lang.Long pid;
	private java.lang.String flatformCode;
	private java.lang.String kind;
	private java.lang.String message;
	private java.sql.Timestamp createDate;
	//columns END

	public CollectLog(){
	}

	public CollectLog(
		java.lang.Long pid
	){
		this.pid = pid;
	}

	
	public void setPid(java.lang.Long value) {
		this.pid = value;
	}
	
	public java.lang.Long getPid() {
		return this.pid;
	}
	
	public void setFlatformCode(java.lang.String value) {
		this.flatformCode = value;
	}
	
	public java.lang.String getFlatformCode() {
		return this.flatformCode;
	}
	
	public void setKind(java.lang.String value) {
		this.kind = value;
	}
	
	public java.lang.String getKind() {
		return this.kind;
	}
	
	public void setMessage(java.lang.String value) {
		this.message = value;
	}
	
	public java.lang.String getMessage() {
		return this.message;
	}
	
	public void setCreateDate(java.sql.Timestamp value) {
		this.createDate = value;
	}
	
	public java.sql.Timestamp getCreateDate() {
		return this.createDate;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Pid",getPid())
			.append("FlatformCode",getFlatformCode())
			.append("Kind",getKind())
			.append("Message",getMessage())
			.append("CreateDate",getCreateDate())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getPid())
			.append(getFlatformCode())
			.append(getKind())
			.append(getMessage())
			.append(getCreateDate())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof CollectLog == false) return false;
		if(this == obj) return true;
		CollectLog other = (CollectLog)obj;
		return new EqualsBuilder()
			.append(getPid(),other.getPid())
			.append(getFlatformCode(),other.getFlatformCode())
			.append(getKind(),other.getKind())
			.append(getMessage(),other.getMessage())
			.append(getCreateDate(),other.getCreateDate())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.pid;
	}
}

