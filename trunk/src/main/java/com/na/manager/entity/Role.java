package com.na.manager.entity;

import java.io.Serializable;

/**
 * 角色表
 * @author andy
 * @date 2017年6月22日 上午11:45:46
 * 
 */
public class Role implements Serializable{
	private static final long serialVersionUID = 1L;
	private String roleID;
	private String roleName;
	private String roleDesc;
	private Integer roleOrder;
	private Long parentID;
	private Integer status;

	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Integer getRoleOrder() {
		return roleOrder;
	}

	public void setRoleOrder(Integer roleOrder) {
		this.roleOrder = roleOrder;
	}

	public Long getParentID() {
		return parentID;
	}

	public void setParentID(Long parentID) {
		this.parentID = parentID;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
