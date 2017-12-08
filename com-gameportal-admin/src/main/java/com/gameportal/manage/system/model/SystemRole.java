package com.gameportal.manage.system.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SystemRole extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "SystemRole";
	public static final String ALIAS_ROLE_ID = "角色ID";
	public static final String ALIAS_ROLE_NAME = "角色名称";
	public static final String ALIAS_ROLE_DESC = "角色描述";
	public static final String ALIAS_STATUS = "status";
	public static final String ALIAS_CREATE_DATE = "createDate";
	public static final String ALIAS_UPDATE_DATE = "updateDate";
	
	
	//columns START
	private java.lang.Long roleId;
	private java.lang.String roleName;
	private java.lang.String roleDesc;
	private java.lang.Integer status;
	private java.util.Date createDate;
	private java.util.Date updateDate;
	//columns END

	public SystemRole(){
	}

	public SystemRole(
		java.lang.Long roleId
	){
		this.roleId = roleId;
	}

	
	public void setRoleId(java.lang.Long value) {
		this.roleId = value;
	}
	
	public java.lang.Long getRoleId() {
		return this.roleId;
	}
	
	public void setRoleName(java.lang.String value) {
		this.roleName = value;
	}
	
	public java.lang.String getRoleName() {
		return this.roleName;
	}
	
	public void setRoleDesc(java.lang.String value) {
		this.roleDesc = value;
	}
	
	public java.lang.String getRoleDesc() {
		return this.roleDesc;
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
	
	private Set tbSystemUserRoles = new HashSet(0);
	public void setTbSystemUserRoles(Set<SystemUserRole> tbSystemUserRole){
		this.tbSystemUserRoles = tbSystemUserRole;
	}
	
	public Set<SystemUserRole> getTbSystemUserRoles() {
		return tbSystemUserRoles;
	}
	
	private Set tbSystemRoleModules = new HashSet(0);
	public void setTbSystemRoleModules(Set<SystemRoleModule> tbSystemRoleModule){
		this.tbSystemRoleModules = tbSystemRoleModule;
	}
	
	public Set<SystemRoleModule> getTbSystemRoleModules() {
		return tbSystemRoleModules;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("RoleId",getRoleId())
			.append("RoleName",getRoleName())
			.append("RoleDesc",getRoleDesc())
			.append("Status",getStatus())
			.append("CreateDate",getCreateDate())
			.append("UpdateDate",getUpdateDate())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getRoleId())
			.append(getRoleName())
			.append(getRoleDesc())
			.append(getStatus())
			.append(getCreateDate())
			.append(getUpdateDate())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof SystemRole == false) return false;
		if(this == obj) return true;
		SystemRole other = (SystemRole)obj;
		return new EqualsBuilder()
			.append(getRoleId(),other.getRoleId())
			.append(getRoleName(),other.getRoleName())
			.append(getRoleDesc(),other.getRoleDesc())
			.append(getStatus(),other.getStatus())
			.append(getCreateDate(),other.getCreateDate())
			.append(getUpdateDate(),other.getUpdateDate())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {

		return  this.roleId;
	}
}

