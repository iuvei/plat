package com.gameportal.manage.system.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SystemUser extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7238251574610835621L;
	// alias
	public static final String TABLE_ALIAS = "SystemUser";
	public static final String ALIAS_USER_ID = "用户ID";
	public static final String ALIAS_ACCOUNT = "账号";
	public static final String ALIAS_PASSWORD = "密码";
	public static final String ALIAS_REAL_NAME = "用户真实姓名";
	public static final String ALIAS_SEX = "性别 0:男 1:女";
	public static final String ALIAS_EMAIL = "电子邮件地址";
	public static final String ALIAS_MOBILE = "手机";
	public static final String ALIAS_OFFICE_PHONE = "办公电话";
	public static final String ALIAS_ERROR_COUNT = "密码错误次数";
	public static final String ALIAS_LAST_LOGIN_TIME = "上次登录时间";
	public static final String ALIAS_LAST_LOGIN_IP = "上次登录IP地址";
	public static final String ALIAS_REMARK = "备注";
	public static final String ALIAS_STATUS = "status";
	public static final String ALIAS_CREATE_DATE = "createDate";
	public static final String ALIAS_UPDATE_DATE = "updateDate";

	// columns START
	private java.lang.Long userId;
	private java.lang.String account;
	private java.lang.String password;
	private String truename;				//真实姓名
	private java.lang.String realName;		//角色名称
	private java.lang.Integer sex;
	private java.lang.String email;
	private java.lang.String mobile;
	private java.lang.String officePhone;
	private java.lang.Integer errorCount;
	private java.util.Date lastLoginTime;
	private java.lang.String lastLoginIp;
	private java.lang.String remark;
	private java.lang.Integer status;
	private java.util.Date createDate;
	private java.util.Date updateDate;
	private String clientmac;
	private String clientip;
	private java.lang.Integer bindstatus;	//绑定状态

	// columns END
	
	private java.lang.String roleIds; // 传值用

	public java.lang.String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(java.lang.String roleIds) {
		this.roleIds = roleIds;
	}
	
	
	public SystemUser() {
	}

	public SystemUser(java.lang.Long userId) {
		this.userId = userId;
	}

	public void setUserId(java.lang.Long value) {
		this.userId = value;
	}

	public java.lang.Long getUserId() {
		return this.userId;
	}

	public void setAccount(java.lang.String value) {
		this.account = value;
	}

	public java.lang.String getAccount() {
		return this.account;
	}

	public void setPassword(java.lang.String value) {
		this.password = value;
	}

	public java.lang.String getPassword() {
		return this.password;
	}

	public void setRealName(java.lang.String value) {
		this.realName = value;
	}

	public java.lang.String getRealName() {
		return this.realName;
	}

	public void setSex(java.lang.Integer value) {
		this.sex = value;
	}

	public java.lang.Integer getSex() {
		return this.sex;
	}

	public void setEmail(java.lang.String value) {
		this.email = value;
	}

	public java.lang.String getEmail() {
		return this.email;
	}

	public void setMobile(java.lang.String value) {
		this.mobile = value;
	}

	public java.lang.String getMobile() {
		return this.mobile;
	}

	public void setOfficePhone(java.lang.String value) {
		this.officePhone = value;
	}

	public java.lang.String getOfficePhone() {
		return this.officePhone;
	}

	public void setErrorCount(java.lang.Integer value) {
		this.errorCount = value;
	}

	public java.lang.Integer getErrorCount() {
		return this.errorCount;
	}

	public void setLastLoginTime(java.util.Date value) {
		this.lastLoginTime = value;
	}

	public java.util.Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginIp(java.lang.String value) {
		this.lastLoginIp = value;
	}

	public java.lang.String getLastLoginIp() {
		return this.lastLoginIp;
	}

	public void setRemark(java.lang.String value) {
		this.remark = value;
	}

	public java.lang.String getRemark() {
		return this.remark;
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

	public void setTbSystemUserRoles(Set<SystemUserRole> tbSystemUserRole) {
		this.tbSystemUserRoles = tbSystemUserRole;
	}

	public Set<SystemUserRole> getTbSystemUserRoles() {
		return tbSystemUserRoles;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getClientmac() {
		return clientmac;
	}

	public void setClientmac(String clientmac) {
		this.clientmac = clientmac;
	}

	public String getClientip() {
		return clientip;
	}

	public void setClientip(String clientip) {
		this.clientip = clientip;
	}

	public java.lang.Integer getBindstatus() {
		return bindstatus;
	}

	public void setBindstatus(java.lang.Integer bindstatus) {
		this.bindstatus = bindstatus;
	}

	public String toString() {
		return new ToStringBuilder(this).append("UserId", getUserId())
				.append("Account", getAccount())
				.append("Password", getPassword())
				.append("RealName", getRealName()).append("Sex", getSex())
				.append("Email", getEmail()).append("Mobile", getMobile())
				.append("OfficePhone", getOfficePhone())
				.append("ErrorCount", getErrorCount())
				.append("LastLoginTime", getLastLoginTime())
				.append("LastLoginIp", getLastLoginIp())
				.append("Remark", getRemark()).append("Status", getStatus())
				.append("CreateDate", getCreateDate())
				.append("UpdateDate", getUpdateDate()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getUserId()).append(getAccount())
				.append(getPassword()).append(getRealName()).append(getSex())
				.append(getEmail()).append(getMobile())
				.append(getOfficePhone()).append(getErrorCount())
				.append(getLastLoginTime()).append(getLastLoginIp())
				.append(getRemark()).append(getStatus())
				.append(getCreateDate()).append(getUpdateDate()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof SystemUser == false)
			return false;
		if (this == obj)
			return true;
		SystemUser other = (SystemUser) obj;
		return new EqualsBuilder().append(getUserId(), other.getUserId())
				.append(getAccount(), other.getAccount())
				.append(getPassword(), other.getPassword())
				.append(getRealName(), other.getRealName())
				.append(getSex(), other.getSex())
				.append(getEmail(), other.getEmail())
				.append(getMobile(), other.getMobile())
				.append(getOfficePhone(), other.getOfficePhone())
				.append(getErrorCount(), other.getErrorCount())
				.append(getLastLoginTime(), other.getLastLoginTime())
				.append(getLastLoginIp(), other.getLastLoginIp())
				.append(getRemark(), other.getRemark())
				.append(getStatus(), other.getStatus())
				.append(getCreateDate(), other.getCreateDate())
				.append(getUpdateDate(), other.getUpdateDate()).isEquals();
	}

	@Override
	public Serializable getID() {

		return this.userId;
	}
}
