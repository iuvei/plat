package com.gameportal.manage.system.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SystemDepartment extends BaseEntity {

	// alias
	public static final String TABLE_ALIAS = "SystemDepartment";
	public static final String ALIAS_SDID = "部门ID";
	public static final String ALIAS_DEPARTMENT_KEY = "部门编码";
	public static final String ALIAS_DEPARTMENT_VALUE = "部门名称";
	public static final String ALIAS_PDID = "上级部门编码";
	public static final String ALIAS_DESCRIPTION = "部门描述";
	public static final String ALIAS_STATUS = "status";
	public static final String ALIAS_CREATE_DATE = "createDate";
	public static final String ALIAS_UPDATE_DATE = "updateDate";

	// columns START
	private java.lang.Long sdid;
	private java.lang.String departmentKey;
	private java.lang.String departmentValue;
	private java.lang.Long pdid;
	private java.lang.String description;
	private java.lang.Integer status;
	private java.util.Date createDate;
	private java.util.Date updateDate;

	// columns END

	public SystemDepartment() {
	}

	public SystemDepartment(java.lang.Long sdid) {
		this.sdid = sdid;
	}

	public void setSdid(java.lang.Long value) {
		this.sdid = value;
	}

	public java.lang.Long getSdid() {
		return this.sdid;
	}

	public void setDepartmentKey(java.lang.String value) {
		this.departmentKey = value;
	}

	public java.lang.String getDepartmentKey() {
		return this.departmentKey;
	}

	public void setDepartmentValue(java.lang.String value) {
		this.departmentValue = value;
	}

	public java.lang.String getDepartmentValue() {
		return this.departmentValue;
	}

	public void setPdid(java.lang.Long value) {
		this.pdid = value;
	}

	public java.lang.Long getPdid() {
		return this.pdid;
	}

	public void setDescription(java.lang.String value) {
		this.description = value;
	}

	public java.lang.String getDescription() {
		return this.description;
	}

	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}

	public java.lang.Integer getStatus() {
		return this.status;
	}

	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}

	public java.util.Date getCreateDate() {
		return this.createDate;
	}

	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}

	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}

	public String toString() {
		return new ToStringBuilder(this).append("Sdid", getSdid())
				.append("DepartmentKey", getDepartmentKey())
				.append("DepartmentValue", getDepartmentValue())
				.append("Pdid", getPdid())
				.append("Description", getDescription())
				.append("Status", getStatus())
				.append("CreateDate", getCreateDate())
				.append("UpdateDate", getUpdateDate()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getSdid())
				.append(getDepartmentKey()).append(getDepartmentValue())
				.append(getPdid()).append(getDescription()).append(getStatus())
				.append(getCreateDate()).append(getUpdateDate()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof SystemDepartment == false)
			return false;
		if (this == obj)
			return true;
		SystemDepartment other = (SystemDepartment) obj;
		return new EqualsBuilder().append(getSdid(), other.getSdid())
				.append(getDepartmentKey(), other.getDepartmentKey())
				.append(getDepartmentValue(), other.getDepartmentValue())
				.append(getPdid(), other.getPdid())
				.append(getDescription(), other.getDescription())
				.append(getStatus(), other.getStatus())
				.append(getCreateDate(), other.getCreateDate())
				.append(getUpdateDate(), other.getUpdateDate()).isEquals();
	}

	@Override
	public Serializable getID() {

		return this.sdid;
	}
}
