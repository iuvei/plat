package com.gameportal.manage.member.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;


public class MemberUpgradeLog extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "MemberUpgradeLog";
	public static final String ALIAS_LID = "lid";
	public static final String ALIAS_UID = "uid";
	public static final String ALIAS_ACCOUNT = "account";
	public static final String ALIAS_OLDGRADE = "oldgrade";
	public static final String ALIAS_NEWGRADE = "newgrade";
	public static final String ALIAS_REASON = "reason";
	public static final String ALIAS_REMARK = "remark";
	public static final String ALIAS_CREATE_DATE = "createDate";
	public static final String ALIAS_CREATEUSERID = "createuserid";
	public static final String ALIAS_CREATEUSERNAME = "createusername";
	
	
	//columns START
	private java.lang.Long lid;
	private java.lang.Long uid;
	private java.lang.String account;
	private java.lang.Long oldgrade;
	private java.lang.Long newgrade;
	private java.lang.String reason;
	private java.lang.String remark;
	private java.sql.Timestamp createDate;
	private java.lang.String createuserid;
	private java.lang.String createusername;
	//columns END

	public MemberUpgradeLog(){
	}

	public MemberUpgradeLog(
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
	
	public void setOldgrade(java.lang.Long value) {
		this.oldgrade = value;
	}
	
	public java.lang.Long getOldgrade() {
		return this.oldgrade;
	}
	
	public void setNewgrade(java.lang.Long value) {
		this.newgrade = value;
	}
	
	public java.lang.Long getNewgrade() {
		return this.newgrade;
	}
	
	public void setReason(java.lang.String value) {
		this.reason = value;
	}
	
	public java.lang.String getReason() {
		return this.reason;
	}
	
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	
	public java.lang.String getRemark() {
		return this.remark;
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
			.append("Oldgrade",getOldgrade())
			.append("Newgrade",getNewgrade())
			.append("Reason",getReason())
			.append("Remark",getRemark())
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
			.append(getOldgrade())
			.append(getNewgrade())
			.append(getReason())
			.append(getRemark())
			.append(getCreateDate())
			.append(getCreateuserid())
			.append(getCreateusername())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof MemberUpgradeLog == false) return false;
		if(this == obj) return true;
		MemberUpgradeLog other = (MemberUpgradeLog)obj;
		return new EqualsBuilder()
			.append(getLid(),other.getLid())
			.append(getUid(),other.getUid())
			.append(getAccount(),other.getAccount())
			.append(getOldgrade(),other.getOldgrade())
			.append(getNewgrade(),other.getNewgrade())
			.append(getReason(),other.getReason())
			.append(getRemark(),other.getRemark())
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

