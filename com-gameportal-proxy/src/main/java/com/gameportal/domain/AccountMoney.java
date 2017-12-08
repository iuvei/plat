package com.gameportal.domain;

import java.math.BigDecimal;

/**
 * 钱包余额
 * @author leron
 *
 */
public class AccountMoney extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1363789127807358469L;

	
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
	public java.lang.Long getAmid() {
		return amid;
	}
	public void setAmid(java.lang.Long amid) {
		this.amid = amid;
	}
	public java.lang.Long getUiid() {
		return uiid;
	}
	public void setUiid(java.lang.Long uiid) {
		this.uiid = uiid;
	}
	public BigDecimal getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(BigDecimal totalamount) {
		this.totalamount = totalamount;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	public java.util.Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
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
