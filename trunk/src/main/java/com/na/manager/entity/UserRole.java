package com.na.manager.entity;

import java.io.Serializable;
/**
 * 用户角色表
 * @author andy
 * @date 2017年6月22日 上午11:45:46
 * 
 */
public class UserRole implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long userID;
	private String roleID;
	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

}
