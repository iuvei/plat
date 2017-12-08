package com.gameportal.web.user.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.pay.model.BaseEntity;

import org.apache.commons.lang.StringUtils;


public class UserInfo extends BaseEntity {
	private static final long serialVersionUID = 1L;
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
	public static final String ALIAS_PHONEVALID ="手机号码验证 0:无效 1：有效";

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
	private java.lang.String url;
	private java.lang.String regip;
	private java.lang.Integer grade;
	private java.lang.Long puiid;
	private java.lang.Integer status;
	private java.util.Date createDate;
	private java.util.Date updateDate;
	private java.lang.Integer logincount;
	private java.lang.String loginip;		//登录IP
	private java.lang.String apipassword;//第三方游戏密码
	/**
	 * 注册来源
	 * direct:直接
	 */
	private java.lang.String ulabel;
	
	/**
	 * 0：无效
	 * 1：有效
	 */
	private Integer phonevalid; //验证手机号码有效性
	/**
	 * 验证E-mail
	 * 0：无效
	 * 1：有效
	 */
	private Integer emailvalid;
	// columns END
	private String key;			//登录key
	private String loginurl;	//登录URL
	private String loginClient; //登录来源
	
	private Integer relaflag; //是否有关联账号  0：未关联  1：已关联
	
	private String relaaccount; // 关联账号列表
	
	private String iparea; //IP区域
	
	private String phonearea; // 电话区域
	
	private String regdevice; //注册客户端信息
	
	private String weekrake; // 周返水开关
	
	private String platforms; // 游戏平台名称
	
	private String spread; // 推广ID
	
	private String withdrawlFlag; //提款开关
	
	private String mgId; //MG方用户ID
	

	public UserInfo() {
	}

	public UserInfo(java.lang.Long uiid) {
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

	public java.lang.String getLoginip() {
		return loginip;
	}

	public void setLoginip(java.lang.String loginip) {
		this.loginip = loginip;
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

	public String getLoginurl() {
		return loginurl;
	}

	public void setLoginurl(String loginurl) {
		this.loginurl = loginurl;
	}

	public String getLoginClient() {
		return loginClient;
	}

	public void setLoginClient(String loginClient) {
		this.loginClient = loginClient;
	}

	
	public java.lang.Integer getLogincount() {
		return logincount;
	}

	public void setLogincount(java.lang.Integer logincount) {
		this.logincount = logincount;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public java.lang.String getUrl() {
		return url;
	}

	public void setUrl(java.lang.String url) {
		this.url = url;
	}

	public java.lang.String getRegip() {
		return regip;
	}

	public void setRegip(java.lang.String regip) {
		this.regip = regip;
	}
	

	public java.lang.String getApipassword() {
		return apipassword;
	}
	

	public java.lang.String getUlabel() {
		return ulabel;
	}

	public void setUlabel(java.lang.String ulabel) {
		this.ulabel = ulabel;
	}

	public void setApipassword(java.lang.String apipassword) {
		this.apipassword = apipassword;
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
	
	public String getWithdrawlFlag() {
		return withdrawlFlag;
	}

	public void setWithdrawlFlag(String withdrawlFlag) {
		this.withdrawlFlag = withdrawlFlag;
	}

	/**
	 * 标识用户进入那些游戏。
	 * @param platId
	 */
	public void updatePlats(String platId) {
		platforms = (platforms == null ? "" : platforms);
		if (org.apache.commons.lang.StringUtils.isNotEmpty(platforms)) {
			platforms += ",";
		}
		this.platforms += platId;
		this.setPlatforms(platforms);
	}

	/**
	 * 判断用户账号在第三方是否存在。
	 * @param platId
	 * @return
	 */
	public boolean isNotExist(String platId) {
		if (StringUtils.isEmpty(platforms) || platforms.indexOf(platId) == -1) {
			return true;
		}
		return false;
	}


	public String toString() {
		return new ToStringBuilder(this).append("Uiid", getUiid())
				.append("Account", getAccount()).append("Passwd", getPasswd())
				.append("Accounttype", getAccounttype())
				.append("Uname", getUname())
				.append("Atmpasswd", getAtmpasswd())
				.append("Identitycard", getIdentitycard())
				.append("Phone", getPhone()).append("Email", getEmail())
				.append("Qq", getQq()).append("Birthday", getBirthday())
				.append("Grade", getGrade()).append("Puiid", getPuiid())
				.append("Status", getStatus())
				.append("CreateDate", getCreateDate())
				.append("PhoneValid",getPhonevalid())
				.append("Emailvalid",getEmailvalid())
				.append("UpdateDate", getUpdateDate()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getUiid()).append(getAccount())
				.append(getPasswd()).append(getAccounttype())
				.append(getUname()).append(getAtmpasswd())
				.append(getIdentitycard()).append(getPhone())
				.append(getEmail()).append(getQq()).append(getBirthday())
				.append(getGrade()).append(getPuiid()).append(getStatus())
				.append(getPhonevalid())
				.append(getCreateDate()).append(getUpdateDate()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof UserInfo == false)
			return false;
		if (this == obj)
			return true;
		UserInfo other = (UserInfo) obj;
		return new EqualsBuilder().append(getUiid(), other.getUiid())
				.append(getAccount(), other.getAccount())
				.append(getPasswd(), other.getPasswd())
				.append(getAccounttype(), other.getAccounttype())
				.append(getUname(), other.getUname())
				.append(getAtmpasswd(), other.getAtmpasswd())
				.append(getIdentitycard(), other.getIdentitycard())
				.append(getPhone(), other.getPhone())
				.append(getEmail(), other.getEmail())
				.append(getQq(), other.getQq())
				.append(getBirthday(), other.getBirthday())
				.append(getGrade(), other.getGrade())
				.append(getPuiid(), other.getPuiid())
				.append(getStatus(), other.getStatus())
				.append(getCreateDate(), other.getCreateDate()).append(getPhonevalid(), other.getPhonevalid())
				.append(getUpdateDate(), other.getUpdateDate()).isEquals();
	}

	public String getPlatforms() {
		return platforms;
	}

	public void setPlatforms(String platforms) {
		this.platforms = platforms;
	}

	public String getSpread() {
		return spread;
	}

	public void setSpread(String spread) {
		this.spread = spread;
	}

	@Override
	public Serializable getID() {
		return this.uiid;
	}

	public String getMgId() {
		return mgId;
	}

	public void setMgId(String mgId) {
		this.mgId = mgId;
	}
}
