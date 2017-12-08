package com.gameportal.manage.system.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SystemModule extends BaseEntity {

	// alias
	public static final String TABLE_ALIAS = "SystemModule";
	public static final String ALIAS_MODULE_ID = "模块ID";
	public static final String ALIAS_MODULE_NAME = "模块名称";
	public static final String ALIAS_MODULE_URL = "模块URL";
	public static final String ALIAS_PARENT_ID = "父模块ID";
	public static final String ALIAS_LEAF = "叶子节点(0:树枝节点;1:叶子节点)";
	public static final String ALIAS_EXPANDED = "展开状态(1:展开;0:收缩)";
	public static final String ALIAS_DISPLAY_INDEX = "显示顺序";
	public static final String ALIAS_IS_DISPLAY = "是否显示 0:否 1:是";
	public static final String ALIAS_EN_MODULE_NAME = "模块英文名称";
	public static final String ALIAS_ICON_CSS = "图标或者样式";
	public static final String ALIAS_INFORMATION = "节点说明";
	public static final String ALIAS_STATUS = "status";
	public static final String ALIAS_CREATE_DATE = "createDate";
	public static final String ALIAS_UPDATE_DATE = "updateDate";

	// columns START
	private java.lang.Long moduleId;
	private java.lang.String moduleName;
	private java.lang.String moduleUrl;
	private java.lang.Long parentId;
	private java.lang.Integer leaf;
	private java.lang.Integer expanded;
	private java.lang.Integer displayIndex;
	private java.lang.Integer isDisplay;
	private java.lang.String enModuleName;
	private java.lang.String iconCss;
	private java.lang.String information;
	private java.lang.Integer status;
	private java.util.Date createDate;
	private java.util.Date updateDate;

	// columns END

	public SystemModule() {
	}

	public SystemModule(java.lang.Long moduleId) {
		this.moduleId = moduleId;
	}

	public void setModuleId(java.lang.Long value) {
		this.moduleId = value;
	}

	public java.lang.Long getModuleId() {
		return this.moduleId;
	}

	public void setModuleName(java.lang.String value) {
		this.moduleName = value;
	}

	public java.lang.String getModuleName() {
		return this.moduleName;
	}

	public void setModuleUrl(java.lang.String value) {
		this.moduleUrl = value;
	}

	public java.lang.String getModuleUrl() {
		return this.moduleUrl;
	}

	public void setParentId(java.lang.Long value) {
		this.parentId = value;
	}

	public java.lang.Long getParentId() {
		return this.parentId;
	}

	public void setLeaf(java.lang.Integer value) {
		this.leaf = value;
	}

	public java.lang.Integer getLeaf() {
		return this.leaf;
	}

	public void setExpanded(java.lang.Integer value) {
		this.expanded = value;
	}

	public java.lang.Integer getExpanded() {
		return this.expanded;
	}

	public void setDisplayIndex(java.lang.Integer value) {
		this.displayIndex = value;
	}

	public java.lang.Integer getDisplayIndex() {
		return this.displayIndex;
	}

	public void setIsDisplay(java.lang.Integer value) {
		this.isDisplay = value;
	}

	public java.lang.Integer getIsDisplay() {
		return this.isDisplay;
	}

	public void setEnModuleName(java.lang.String value) {
		this.enModuleName = value;
	}

	public java.lang.String getEnModuleName() {
		return this.enModuleName;
	}

	public void setIconCss(java.lang.String value) {
		this.iconCss = value;
	}

	public java.lang.String getIconCss() {
		return this.iconCss;
	}

	public void setInformation(java.lang.String value) {
		this.information = value;
	}

	public java.lang.String getInformation() {
		return this.information;
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

	private Set tbSystemRoleModules = new HashSet(0);

	public void setTbSystemRoleModules(Set<SystemRoleModule> tbSystemRoleModule) {
		this.tbSystemRoleModules = tbSystemRoleModule;
	}

	public Set<SystemRoleModule> getTbSystemRoleModules() {
		return tbSystemRoleModules;
	}

	public String toString() {
		return new ToStringBuilder(this).append("ModuleId", getModuleId())
				.append("ModuleName", getModuleName())
				.append("ModuleUrl", getModuleUrl())
				.append("ParentId", getParentId()).append("Leaf", getLeaf())
				.append("Expanded", getExpanded())
				.append("DisplayIndex", getDisplayIndex())
				.append("IsDisplay", getIsDisplay())
				.append("EnModuleName", getEnModuleName())
				.append("IconCss", getIconCss())
				.append("Information", getInformation())
				.append("Status", getStatus())
				.append("CreateDate", getCreateDate())
				.append("UpdateDate", getUpdateDate()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getModuleId())
				.append(getModuleName()).append(getModuleUrl())
				.append(getParentId()).append(getLeaf()).append(getExpanded())
				.append(getDisplayIndex()).append(getIsDisplay())
				.append(getEnModuleName()).append(getIconCss())
				.append(getInformation()).append(getStatus())
				.append(getCreateDate()).append(getUpdateDate()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof SystemModule == false)
			return false;
		if (this == obj)
			return true;
		SystemModule other = (SystemModule) obj;
		return new EqualsBuilder().append(getModuleId(), other.getModuleId())
				.append(getModuleName(), other.getModuleName())
				.append(getModuleUrl(), other.getModuleUrl())
				.append(getParentId(), other.getParentId())
				.append(getLeaf(), other.getLeaf())
				.append(getExpanded(), other.getExpanded())
				.append(getDisplayIndex(), other.getDisplayIndex())
				.append(getIsDisplay(), other.getIsDisplay())
				.append(getEnModuleName(), other.getEnModuleName())
				.append(getIconCss(), other.getIconCss())
				.append(getInformation(), other.getInformation())
				.append(getStatus(), other.getStatus())
				.append(getCreateDate(), other.getCreateDate())
				.append(getUpdateDate(), other.getUpdateDate()).isEquals();
	}

	@Override
	public Serializable getID() {

		return this.moduleId;
	}
}
