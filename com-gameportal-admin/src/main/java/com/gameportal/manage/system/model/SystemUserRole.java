package com.gameportal.manage.system.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SystemUserRole extends BaseEntity {

	// alias
	public static final String TABLE_ALIAS = "SystemUserRole";
	public static final String ALIAS_USER_ROLE_ID = "用户角色ID";
	public static final String ALIAS_USER_ID = "用户ID";
	public static final String ALIAS_ROLE_ID = "角色ID";

	// columns START
	private java.lang.Long userRoleId;
	private java.lang.Long userId;
	private java.lang.Long roleId;

	// columns END

	public SystemUserRole() {
	}

	public SystemUserRole(java.lang.Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public void setUserRoleId(java.lang.Long value) {
		this.userRoleId = value;
	}

	public java.lang.Long getUserRoleId() {
		return this.userRoleId;
	}

	public void setUserId(java.lang.Long value) {
		this.userId = value;
	}

	public java.lang.Long getUserId() {
		return this.userId;
	}

	public void setRoleId(java.lang.Long value) {
		this.roleId = value;
	}

	public java.lang.Long getRoleId() {
		return this.roleId;
	}

	private SystemRole tbSystemRole;

	public void setTbSystemRole(SystemRole tbSystemRole) {
		this.tbSystemRole = tbSystemRole;
	}

	public SystemRole getTbSystemRole() {
		return tbSystemRole;
	}

	private SystemUser tbSystemUsers;

	public void setTbSystemUsers(SystemUser tbSystemUsers) {
		this.tbSystemUsers = tbSystemUsers;
	}

	public SystemUser getTbSystemUsers() {
		return tbSystemUsers;
	}

	public String toString() {
		return new ToStringBuilder(this).append("UserRoleId", getUserRoleId())
				.append("UserId", getUserId()).append("RoleId", getRoleId())
				.toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getUserRoleId())
				.append(getUserId()).append(getRoleId()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof SystemUserRole == false)
			return false;
		if (this == obj)
			return true;
		SystemUserRole other = (SystemUserRole) obj;
		return new EqualsBuilder()
				.append(getUserRoleId(), other.getUserRoleId())
				.append(getUserId(), other.getUserId())
				.append(getRoleId(), other.getRoleId()).isEquals();
	}

	@Override
	public Serializable getID() {

		return this.userRoleId;
	}
}
