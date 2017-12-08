package com.gameportal.domain;


/**
 * 游戏账号
 * @author leron
 *
 */
public class UserInfo extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2033837592518908344L;

	// alias
	public static final String TABLE_ALIAS = "UserInfo";
	public static final String ALIAS_UIID = "用户ID";
	public static final String ALIAS_ACCOUNT = "帐号";
	public static final String ALIAS_PASSWD = "密码";
	public static final String ALIAS_ACCOUNTTYPE = "帐号类型";
	public static final String ALIAS_UNAME = "真实姓名";
	public static final String ALIAS_ATMPASSWD = "提款密码";
	public static final String ALIAS_IDENTITYCARD = "身份证";
	public static final String ALIAS_PHONE = "电话";
	public static final String ALIAS_EMAIL = "邮箱";
	public static final String ALIAS_QQ = "QQ";
	public static final String ALIAS_BIRTHDAY = "生日";
	public static final String ALIAS_GRADE = "会员等级";
	public static final String ALIAS_PUIID = "父用户ID";
	public static final String ALIAS_STATUS = "状态";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_DATE = "更新时间";

	// columns START
	private java.lang.Long uiid;
	private java.lang.String account;
	private java.lang.String passwd;
	private java.lang.Integer accounttype;
	private java.lang.String uname;
	private java.lang.String atmpasswd;
	private java.lang.String identitycard;
	private java.lang.String phone;
	private java.lang.String email;
	private java.lang.String qq;
	private java.lang.String birthday;
	private java.lang.Integer grade;
	private java.lang.Long puiid;
	private java.lang.Integer status;
	private java.util.Date createDate;
	private java.util.Date updateDate;
	private java.lang.String apipassword;//第三方游戏密码
	public java.lang.Long getUiid() {
		return uiid;
	}
	public void setUiid(java.lang.Long uiid) {
		this.uiid = uiid;
	}
	public java.lang.String getAccount() {
		return account;
	}
	public void setAccount(java.lang.String account) {
		this.account = account;
	}
	public java.lang.String getPasswd() {
		return passwd;
	}
	public void setPasswd(java.lang.String passwd) {
		this.passwd = passwd;
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
	public java.lang.String getAtmpasswd() {
		return atmpasswd;
	}
	public void setAtmpasswd(java.lang.String atmpasswd) {
		this.atmpasswd = atmpasswd;
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
	public java.lang.Long getPuiid() {
		return puiid;
	}
	public void setPuiid(java.lang.Long puiid) {
		this.puiid = puiid;
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
	public java.lang.String getApipassword() {
		return apipassword;
	}
	public void setApipassword(java.lang.String apipassword) {
		this.apipassword = apipassword;
	}
}
