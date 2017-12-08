package com.gameportal.manage.member.model;
import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class MemberInfo extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5928819853429130431L;
	//alias
	public static final String TABLE_ALIAS = "MemberInfo";
	public static final String ALIAS_UIID = "用户ID";
	public static final String ALIAS_ACCOUNT = "帐号";
	public static final String ALIAS_PASSWD = "密码";
	public static final String ALIAS_ACCOUNTTYPE = "帐号类型 0 普通用户 1代理用户";
	public static final String ALIAS_UNAME = "真实姓名";
	public static final String ALIAS_ATMPASSWD = "提款密码";
	public static final String ALIAS_IDENTITYCARD = "身份证";
	public static final String ALIAS_PHONE = "电话";
	public static final String ALIAS_EMAIL = "邮箱";
	public static final String ALIAS_QQ = "QQ";
	public static final String ALIAS_BIRTHDAY = "生日";
	public static final String ALIAS_GRADE = "会员等级 1等级 2等级 3等级 4等级 5等级";
	public static final String ALIAS_PUIID = "父用户ID";
	public static final String ALIAS_LOCK = "锁定状态 0未锁定，1锁定";
	public static final String ALIAS_STATUS = "状态 0 禁用 1 首次登录 2 正常登录 3 审核通过 ";
	public static final String ALIAS_URL = "网址";
	public static final String ALIAS_REGIP = "注册IP";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_DATE = "更新时间";
	
	
	//columns START
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
	private java.lang.Integer lock;
	private java.lang.Integer status;
	private java.lang.String url;
	private java.lang.String regip;
	private java.sql.Timestamp createDate;
	private java.sql.Timestamp updateDate;
	private java.lang.String apipassword;//第三方游戏密码
	private Integer phonevalid; //验证手机号码有效性
	private String ulabel;
	private String remark;
	private Integer logincount;
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
	private Integer relaflag; //是否有关联账号  0：未关联  1：已关联
	
	private String relaaccount; // 关联账号列表
	
	private String iparea; //IP区域
	
	private String phonearea; // 电话区域
	
	private String regdevice;// 注册客户端信息
	
	private String weekrake;
	
	private String withdrawlFlag;
	
	private String mgId;

	public MemberInfo(){
	}

	public MemberInfo(
		java.lang.Long uiid
	){
		this.uiid = uiid;
	}
	
	public Integer getLogincount() {
		return logincount;
	}

	public void setLogincount(Integer logincount) {
		this.logincount = logincount;
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

	public String getUlabel() {
		return ulabel;
	}

	public void setUlabel(String ulabel) {
		this.ulabel = ulabel;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRegdevice() {
		return regdevice;
	}

	public void setRegdevice(String regdevice) {
		this.regdevice = regdevice;
	}

	public String getWeekrake() {
		return weekrake;
	}

	public void setWeekrake(String weekrake) {
		this.weekrake = weekrake;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Uiid",getUiid())
			.append("Account",getAccount())
			.append("Passwd",getPasswd())
			.append("Accounttype",getAccounttype())
			.append("Uname",getUname())
			.append("Atmpasswd",getAtmpasswd())
			.append("Identitycard",getIdentitycard())
			.append("Phone",getPhone())
			.append("Email",getEmail())
			.append("Qq",getQq())
			.append("Birthday",getBirthday())
			.append("Grade",getGrade())
			.append("Puiid",getPuiid())
			.append("Lock",getLock())
			.append("Status",getStatus())
			.append("Url",getUrl())
			.append("Regip",getRegip())
			.append("CreateDate",getCreateDate())
			.append("UpdateDate",getUpdateDate())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getUiid())
			.append(getAccount())
			.append(getPasswd())
			.append(getAccounttype())
			.append(getUname())
			.append(getAtmpasswd())
			.append(getIdentitycard())
			.append(getPhone())
			.append(getEmail())
			.append(getQq())
			.append(getBirthday())
			.append(getGrade())
			.append(getPuiid())
			.append(getLock())
			.append(getStatus())
			.append(getUrl())
			.append(getRegip())
			.append(getCreateDate())
			.append(getUpdateDate())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof MemberInfo == false) return false;
		if(this == obj) return true;
		MemberInfo other = (MemberInfo)obj;
		return new EqualsBuilder()
			.append(getUiid(),other.getUiid())
			.append(getAccount(),other.getAccount())
			.append(getPasswd(),other.getPasswd())
			.append(getAccounttype(),other.getAccounttype())
			.append(getUname(),other.getUname())
			.append(getAtmpasswd(),other.getAtmpasswd())
			.append(getIdentitycard(),other.getIdentitycard())
			.append(getPhone(),other.getPhone())
			.append(getEmail(),other.getEmail())
			.append(getQq(),other.getQq())
			.append(getBirthday(),other.getBirthday())
			.append(getGrade(),other.getGrade())
			.append(getPuiid(),other.getPuiid())
			.append(getLock(),other.getLock())
			.append(getStatus(),other.getStatus())
			.append(getUrl(),other.getUrl())
			.append(getRegip(),other.getRegip())
			.append(getCreateDate(),other.getCreateDate())
			.append(getUpdateDate(),other.getUpdateDate())
			.isEquals();
	}
	
	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.uiid;
	}

	public Integer getRelaflag() {
		return relaflag;
	}

	public void setRelaflag(Integer relaflag) {
		this.relaflag = relaflag;
	}

	public String getRelaaccount() {
		return relaaccount;
	}

	public void setRelaaccount(String relaaccount) {
		this.relaaccount = relaaccount;
	}

	public String getIparea() {
		return iparea;
	}

	public void setIparea(String iparea) {
		this.iparea = iparea;
	}

	public String getPhonearea() {
		return phonearea;
	}

	public void setPhonearea(String phonearea) {
		this.phonearea = phonearea;
	}

	public String getWithdrawlFlag() {
		return withdrawlFlag;
	}

	public void setWithdrawlFlag(String withdrawlFlag) {
		this.withdrawlFlag = withdrawlFlag;
	}

	public String getMgId() {
		return mgId;
	}

	public void setMgId(String mgId) {
		this.mgId = mgId;
	}
}

