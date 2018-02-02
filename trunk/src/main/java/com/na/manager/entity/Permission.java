package com.na.manager.entity;

import com.na.manager.common.annotation.I18NField;

import java.io.Serializable;

/**
 * 权限列表
 * 
 * @author andy
 * @date 2017年6月22日 上午11:45:46
 * 
 */
public class Permission extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5986002427452331204L;

	private String permissionID;

	private String permissionName;
	@I18NField
	private String permissionDesc;

	private Long permissionOrder;

	private Long permissionType;

	private String permissionUrl;

	private Long groupID;

	private Boolean status = Boolean.TRUE;

	private String cls;

	public String getPermissionID() {
		return permissionID;
	}

	public void setPermissionID(String permissionID) {
		this.permissionID = permissionID;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionDesc() {
		return permissionDesc;
	}

	public void setPermissionDesc(String permissionDesc) {
		this.permissionDesc = permissionDesc;
	}

	public Long getPermissionOrder() {
		return permissionOrder;
	}

	public void setPermissionOrder(Long permissionOrder) {
		this.permissionOrder = permissionOrder;
	}

	public Long getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(Long permissionType) {
		this.permissionType = permissionType;
	}

	public String getPermissionUrl() {
		return permissionUrl;
	}

	public void setPermissionUrl(String permissionUrl) {
		this.permissionUrl = permissionUrl;
	}

	public Long getGroupID() {
		return groupID;
	}

	public void setGroupID(Long groupID) {
		this.groupID = groupID;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}
}
