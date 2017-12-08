package com.gameportal.manage.user.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class AccountMoney extends BaseEntity {

	// alias
	public static final String TABLE_ALIAS = "AccountMoney";
	public static final String ALIAS_AMID = "帐号金额ID";
	public static final String ALIAS_UIID = "帐号ID";
	public static final String ALIAS_TOTALAMOUNT = "总金额";
	public static final String ALIAS_STATUS = "状态";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_DATE = "更新时间";

	// columns START
	private java.lang.Long amid;
	private java.lang.Long uiid;
	private BigDecimal totalamount;
	private java.lang.Integer status;
	private java.util.Date createDate;
	private java.util.Date updateDate;
	private int integral;

	// columns END

	// 传值用 START
	private java.lang.String account;
	private java.lang.Integer accounttype;
	private java.lang.String uname;
	private java.lang.String identitycard;
	private java.lang.String phone;
	private java.lang.String email;
	private java.lang.String qq;
	private java.lang.String birthday;
	private java.lang.Integer grade;
	// 传值用 END

	public AccountMoney() {
	}

	public AccountMoney(java.lang.Long amid) {
		this.amid = amid;
	}

	public void setAmid(java.lang.Long value) {
		this.amid = value;
	}

	public java.lang.Long getAmid() {
		return this.amid;
	}

	public void setUiid(java.lang.Long value) {
		this.uiid = value;
	}

	public java.lang.Long getUiid() {
		return this.uiid;
	}

	public BigDecimal getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(BigDecimal totalamount) {
		this.totalamount = totalamount;
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

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String toString() {
		return new ToStringBuilder(this).append("Amid", getAmid()).append("Uiid", getUiid())
				.append("Totalamount", getTotalamount()).append("Status", getStatus())
				.append("CreateDate", getCreateDate()).append("UpdateDate", getUpdateDate()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getAmid()).append(getUiid()).append(getTotalamount()).append(getStatus())
				.append(getCreateDate()).append(getUpdateDate()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof AccountMoney == false)
			return false;
		if (this == obj)
			return true;
		AccountMoney other = (AccountMoney) obj;
		return new EqualsBuilder().append(getAmid(), other.getAmid()).append(getUiid(), other.getUiid())
				.append(getTotalamount(), other.getTotalamount()).append(getStatus(), other.getStatus())
				.append(getCreateDate(), other.getCreateDate()).append(getUpdateDate(), other.getUpdateDate())
				.isEquals();
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.amid;
	}

	public java.lang.String getAccount() {
		return account;
	}

	public void setAccount(java.lang.String account) {
		this.account = account;
	}

	public java.lang.Integer getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(java.lang.Integer accounttype) {
		this.accounttype = accounttype;
	}

	public java.lang.String getUname() {
		return uname;
	}

	public void setUname(java.lang.String uname) {
		this.uname = uname;
	}

	public java.lang.String getIdentitycard() {
		return identitycard;
	}

	public void setIdentitycard(java.lang.String identitycard) {
		this.identitycard = identitycard;
	}

	public java.lang.String getPhone() {
		return phone;
	}

	public void setPhone(java.lang.String phone) {
		this.phone = phone;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getQq() {
		return qq;
	}

	public void setQq(java.lang.String qq) {
		this.qq = qq;
	}

	public java.lang.String getBirthday() {
		return birthday;
	}

	public void setBirthday(java.lang.String birthday) {
		this.birthday = birthday;
	}

	public java.lang.Integer getGrade() {
		return grade;
	}

	public void setGrade(java.lang.Integer grade) {
		this.grade = grade;
	}
}
