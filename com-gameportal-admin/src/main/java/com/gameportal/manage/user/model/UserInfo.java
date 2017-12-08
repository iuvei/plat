package com.gameportal.manage.user.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

@SuppressWarnings("serial")
public class UserInfo extends BaseEntity {

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
	
	private String regdevice; // 注册客户端信息
	
	private String weekrake; // 周返水开关
	
	private String withdrawlFlag; // 提款开关
	
	private String platforms; // 游戏平台名称
	
	private String spread; // 推广ID
	
	private String mgId; //MG 用户ID
	// columns END

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
	

	public java.lang.String getApipassword() {
		return apipassword;
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
	
	public String getMgId() {
		return mgId;
	}

	public void setMgId(String mgId) {
		this.mgId = mgId;
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
				.append("Phonevalid", getCreateDate())
				.append("Emailvalid", getCreateDate())
				.append("CreateDate", getCreateDate())
				.append("UpdateDate", getUpdateDate()).toString();
	}
	

	public int hashCode() {
		return new HashCodeBuilder().append(getUiid()).append(getAccount())
				.append(getPasswd()).append(getAccounttype())
				.append(getUname()).append(getAtmpasswd())
				.append(getIdentitycard()).append(getPhone())
				.append(getEmail()).append(getQq()).append(getBirthday())
				.append(getGrade()).append(getPuiid()).append(getStatus()).append(getPhonevalid()).append(getEmailvalid())
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
				.append(getCreateDate(), other.getCreateDate()).append(getPhonevalid(),other.getPhonevalid())
				.append(getEmailvalid(), other.getEmailvalid())
				.append(getUpdateDate(), other.getUpdateDate()).isEquals();
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.uiid;
	}
}
