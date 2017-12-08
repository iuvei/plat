package com.gameportal.manage.member.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;


public class GameTransferLog extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "GameTransferLog";
	public static final String ALIAS_LID = "lid";
	public static final String ALIAS_UID = "uid";
	public static final String ALIAS_ACCOUNT = "account";
	public static final String ALIAS_FORMPLAT = "formplat";
	public static final String ALIAS_TOPLAT = "toplat";
	public static final String ALIAS_AMOUNT = "amount";
	public static final String ALIAS_CREATE_DATE = "createDate";
	public static final String ALIAS_CREATEUSERID = "createuserid";
	public static final String ALIAS_CREATEUSERNAME = "createusername";
	
	
	//columns START
	private java.lang.Long lid;
	private java.lang.Long uid;
	private java.lang.String account;
	private java.lang.String formplat;
	private java.lang.String toplat;
	private java.lang.Integer amount;
	private java.sql.Timestamp createDate;
	private java.lang.String createuserid;
	private java.lang.String createusername;
	//columns END

	public GameTransferLog(){
	}

	public GameTransferLog(
		java.lang.Long lid
	){
		this.lid = lid;
	}

	
	public void setLid(java.lang.Long value) {
		this.lid = value;
	}
	
	public java.lang.Long getLid() {
		return this.lid;
	}
	
	public void setUid(java.lang.Long value) {
		this.uid = value;
	}
	
	public java.lang.Long getUid() {
		return this.uid;
	}
	
	public void setAccount(java.lang.String value) {
		this.account = value;
	}
	
	public java.lang.String getAccount() {
		return this.account;
	}
	
	public void setFormplat(java.lang.String value) {
		this.formplat = value;
	}
	
	public java.lang.String getFormplat() {
		return this.formplat;
	}
	
	public void setToplat(java.lang.String value) {
		this.toplat = value;
	}
	
	public java.lang.String getToplat() {
		return this.toplat;
	}
	
	public void setAmount(java.lang.Integer value) {
		this.amount = value;
	}
	
	public java.lang.Integer getAmount() {
		return this.amount;
	}
	
	public void setCreateDate(java.sql.Timestamp value) {
		this.createDate = value;
	}
	
	public java.sql.Timestamp getCreateDate() {
		return this.createDate;
	}
	
	public void setCreateuserid(java.lang.String value) {
		this.createuserid = value;
	}
	
	public java.lang.String getCreateuserid() {
		return this.createuserid;
	}
	
	public void setCreateusername(java.lang.String value) {
		this.createusername = value;
	}
	
	public java.lang.String getCreateusername() {
		return this.createusername;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Lid",getLid())
			.append("Uid",getUid())
			.append("Account",getAccount())
			.append("Formplat",getFormplat())
			.append("Toplat",getToplat())
			.append("Amount",getAmount())
			.append("CreateDate",getCreateDate())
			.append("Createuserid",getCreateuserid())
			.append("Createusername",getCreateusername())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getLid())
			.append(getUid())
			.append(getAccount())
			.append(getFormplat())
			.append(getToplat())
			.append(getAmount())
			.append(getCreateDate())
			.append(getCreateuserid())
			.append(getCreateusername())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof GameTransferLog == false) return false;
		if(this == obj) return true;
		GameTransferLog other = (GameTransferLog)obj;
		return new EqualsBuilder()
			.append(getLid(),other.getLid())
			.append(getUid(),other.getUid())
			.append(getAccount(),other.getAccount())
			.append(getFormplat(),other.getFormplat())
			.append(getToplat(),other.getToplat())
			.append(getAmount(),other.getAmount())
			.append(getCreateDate(),other.getCreateDate())
			.append(getCreateuserid(),other.getCreateuserid())
			.append(getCreateusername(),other.getCreateusername())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.lid;
	}
}

