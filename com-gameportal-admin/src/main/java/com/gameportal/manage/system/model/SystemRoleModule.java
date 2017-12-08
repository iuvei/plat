package com.gameportal.manage.system.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SystemRoleModule extends BaseEntity {

	// alias
	public static final String TABLE_ALIAS = "SystemRoleModule";
	public static final String ALIAS_ROLE_MODULE_ID = "角色模块ID";
	public static final String ALIAS_ROLE_ID = "角色ID";
	public static final String ALIAS_MODULE_ID = "模块ID";

	// columns START
	private java.lang.Long roleModuleId;
	private java.lang.Long roleId;
	private java.lang.Long moduleId;

	// columns END

	public SystemRoleModule() {
	}

	public SystemRoleModule(java.lang.Long roleModuleId) {
		this.roleModuleId = roleModuleId;
	}

	public void setRoleModuleId(java.lang.Long value) {
		this.roleModuleId = value;
	}

	public java.lang.Long getRoleModuleId() {
		return this.roleModuleId;
	}

	public void setRoleId(java.lang.Long value) {
		this.roleId = value;
	}

	public java.lang.Long getRoleId() {
		return this.roleId;
	}

	public void setModuleId(java.lang.Long value) {
		this.moduleId = value;
	}

	public java.lang.Long getModuleId() {
		return this.moduleId;
	}

	private SystemRole tbSystemRole;

	public void setTbSystemRole(SystemRole tbSystemRole) {
		this.tbSystemRole = tbSystemRole;
	}

	public SystemRole getTbSystemRole() {
		return tbSystemRole;
	}

	private SystemModule tbSystemModules;

	public void setTbSystemModules(SystemModule tbSystemModules) {
		this.tbSystemModules = tbSystemModules;
	}

	public SystemModule getTbSystemModules() {
		return tbSystemModules;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("RoleModuleId", getRoleModuleId())
				.append("RoleId", getRoleId())
				.append("ModuleId", getModuleId()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getRoleModuleId())
				.append(getRoleId()).append(getModuleId()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof SystemRoleModule == false)
			return false;
		if (this == obj)
			return true;
		SystemRoleModule other = (SystemRoleModule) obj;
		return new EqualsBuilder()
				.append(getRoleModuleId(), other.getRoleModuleId())
				.append(getRoleId(), other.getRoleId())
				.append(getModuleId(), other.getModuleId()).isEquals();
	}

	@Override
	public Serializable getID() {

		return this.roleModuleId;
	}
}
