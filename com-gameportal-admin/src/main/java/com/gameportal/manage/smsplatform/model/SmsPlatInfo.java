package com.gameportal.manage.smsplatform.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class SmsPlatInfo extends BaseEntity {

	// alias
	public static final String TABLE_ALIAS = "SmsPlatInfo";
	public static final String ALIAS_SPIID = "spiid";
	public static final String ALIAS_NAME = "name";
	public static final String ALIAS_STATUS = "status";
	public static final String ALIAS_CREATETIME = "createtime";
	public static final String ALIAS_CREATEUSERID = "createuserid";
	public static final String ALIAS_CREATEUSERNAME = "createusername";
	public static final String ALIAS_UPDATETIME = "updatetime";
	public static final String ALIAS_UPDATEUSERID = "updateuserid";
	public static final String ALIAS_UPDATEUSERNAME = "updateusername";

	// columns START
	private java.lang.Long spiid;
	private java.lang.String name;
	private java.lang.Integer status;
	private java.sql.Timestamp createtime;
	private java.lang.Long createuserid;
	private java.lang.String createusername;
	private java.sql.Timestamp updatetime;
	private java.lang.Long updateuserid;
	private java.lang.String updateusername;

	// columns END

	public SmsPlatInfo() {
	}

	public SmsPlatInfo(java.lang.Long spiid) {
		this.spiid = spiid;
	}

	public void setSpiid(java.lang.Long value) {
		this.spiid = value;
	}

	public java.lang.Long getSpiid() {
		return this.spiid;
	}

	public void setName(java.lang.String value) {
		this.name = value;
	}

	public java.lang.String getName() {
		return this.name;
	}

	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}

	public java.lang.Integer getStatus() {
		return this.status;
	}

	public void setCreatetime(java.sql.Timestamp value) {
		this.createtime = value;
	}

	public java.sql.Timestamp getCreatetime() {
		return this.createtime;
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

	public void setUpdatetime(java.sql.Timestamp value) {
		this.updatetime = value;
	}

	public java.sql.Timestamp getUpdatetime() {
		return this.updatetime;
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

	public String toString() {
		return new ToStringBuilder(this).append("Spiid", getSpiid())
				.append("Name", getName()).append("Status", getStatus())
				.append("Createtime", getCreatetime())
				.append("Createuserid", getCreateuserid())
				.append("Createusername", getCreateusername())
				.append("Updatetime", getUpdatetime())
				.append("Updateuserid", getUpdateuserid())
				.append("Updateusername", getUpdateusername()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getSpiid()).append(getName())
				.append(getStatus()).append(getCreatetime())
				.append(getCreateuserid()).append(getCreateusername())
				.append(getUpdatetime()).append(getUpdateuserid())
				.append(getUpdateusername()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof SmsPlatInfo == false)
			return false;
		if (this == obj)
			return true;
		SmsPlatInfo other = (SmsPlatInfo) obj;
		return new EqualsBuilder().append(getSpiid(), other.getSpiid())
				.append(getName(), other.getName())
				.append(getStatus(), other.getStatus())
				.append(getCreatetime(), other.getCreatetime())
				.append(getCreateuserid(), other.getCreateuserid())
				.append(getCreateusername(), other.getCreateusername())
				.append(getUpdatetime(), other.getUpdatetime())
				.append(getUpdateuserid(), other.getUpdateuserid())
				.append(getUpdateusername(), other.getUpdateusername())
				.isEquals();
	}

	@Override
	public Serializable getID() {

		return this.spiid;
	}
}
