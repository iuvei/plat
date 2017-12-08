package com.gameportal.domain;

import com.gameportal.domain.BaseEntity;

public class MemberInfo extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9132331960954334189L;
	
	//columns START
	private Long uiid;
	private String account;
	private String passwd;
	private Integer accounttype;
	private String uname;
	private String atmpasswd;
	private String identitycard;
	private String phone;
	private String email;
	private String qq;
	private String birthday;
	private Integer grade;
	private Long puiid;
	private Integer lock;
	private Integer status;
	private String url;
	private String regip;
	private java.sql.Timestamp createDate;
	private java.sql.Timestamp updateDate;
	private java.lang.String apipassword;//第三方游戏密码
	private Integer phonevalid; //验证手机号码有效性
	/**
	 * 验证E-mail
	 * 0：无效
	 * 1：有效
	 */
	private Integer emailvalid;
	/**
	 * 用户类型标示默认0
	 * 0：线下会员
	 * 1：线上会员
	 */
	private int typeflag;
	//columns END

	public MemberInfo(){
	}

	public MemberInfo(
		java.lang.Long uiid
	){
		this.uiid = uiid;
	}

	
	public void setUiid(java.lang.Long value) {
		this.uiid = value;
	}
	
	public java.lang.Long getUiid() {
		return this.uiid;
	}
	
	public void setAccount(java.lang.String value) {
		this.account = value;
	}
	
	public java.lang.String getAccount() {
		return this.account;
	}
	
	public void setPasswd(java.lang.String value) {
		this.passwd = value;
	}
	
	public java.lang.String getPasswd() {
		return this.passwd;
	}
	
	public void setAccounttype(java.lang.Integer value) {
		this.accounttype = value;
	}
	
	public java.lang.Integer getAccounttype() {
		return this.accounttype;
	}
	
	public void setUname(java.lang.String value) {
		this.uname = value;
	}
	
	public java.lang.String getUname() {
		return this.uname;
	}
	
	public void setAtmpasswd(java.lang.String value) {
		this.atmpasswd = value;
	}
	
	public java.lang.String getAtmpasswd() {
		return this.atmpasswd;
	}
	
	public void setIdentitycard(java.lang.String value) {
		this.identitycard = value;
	}
	
	public java.lang.String getIdentitycard() {
		return this.identitycard;
	}
	
	public void setPhone(java.lang.String value) {
		this.phone = value;
	}
	
	public java.lang.String getPhone() {
		return this.phone;
	}
	
	public void setEmail(java.lang.String value) {
		this.email = value;
	}
	
	public java.lang.String getEmail() {
		return this.email;
	}
	
	public void setQq(java.lang.String value) {
		this.qq = value;
	}
	
	public java.lang.String getQq() {
		return this.qq;
	}
	
	public void setBirthday(java.lang.String value) {
		this.birthday = value;
	}
	
	public java.lang.String getBirthday() {
		return this.birthday;
	}
	
	public void setGrade(java.lang.Integer value) {
		this.grade = value;
	}
	
	public java.lang.Integer getGrade() {
		return this.grade;
	}
	
	public void setPuiid(java.lang.Long value) {
		this.puiid = value;
	}
	
	public java.lang.Long getPuiid() {
		return this.puiid;
	}
	
	public void setLock(java.lang.Integer value) {
		this.lock = value;
	}
	
	public java.lang.Integer getLock() {
		return this.lock;
	}
	
	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}
	
	public java.lang.Integer getStatus() {
		return this.status;
	}
	
	public void setUrl(java.lang.String value) {
		this.url = value;
	}
	
	public java.lang.String getUrl() {
		return this.url;
	}
	
	public void setRegip(java.lang.String value) {
		this.regip = value;
	}
	
	public java.lang.String getRegip() {
		return this.regip;
	}
	
	public void setCreateDate(java.sql.Timestamp value) {
		this.createDate = value;
	}
	
	public java.sql.Timestamp getCreateDate() {
		return this.createDate;
	}
	
	public void setUpdateDate(java.sql.Timestamp value) {
		this.updateDate = value;
	}
	
	public java.sql.Timestamp getUpdateDate() {
		return this.updateDate;
	}
	

	public java.lang.String getApipassword() {
		return apipassword;
	}

	public void setApipassword(java.lang.String apipassword) {
		this.apipassword = apipassword;
	}
	

	public int getTypeflag() {
		return typeflag;
	}

	public void setTypeflag(int typeflag) {
		this.typeflag = typeflag;
	}

	public Integer getPhonevalid() {
		return phonevalid;
	}

	public void setPhonevalid(Integer phonevalid) {
		this.phonevalid = phonevalid;
	}

	public Integer getEmailvalid() {
		return emailvalid;
	}

	public void setEmailvalid(Integer emailvalid) {
		this.emailvalid = emailvalid;
	}
}

