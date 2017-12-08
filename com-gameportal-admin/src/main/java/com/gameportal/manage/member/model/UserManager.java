package com.gameportal.manage.member.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 电销用户管理
 * @author Administrator
 *
 */
public class UserManager extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6773278800662024277L;
	
	/**
	 * 用户ID
	 */
	private Long uiid;
	
	/**
	 * 对应电销ID
	 */
	private Long belongid;
	
	/**
	 * 电销姓名
	 */
	private String truename;
	
	/**
	 * 玩家类型默认：slot电子游戏
	 * 
	 */
	private String payertype;
	
	/**
	 * 电销备注
	 */
	private String remark;
	
	/**
	 * 客户类型
	 * 1：维护客户
	 * 2：开发客户
	 */
	private int clienttype = 1;
	
	//其他属性
	/**
	 * 玩家账号
	 */
	private String account;
	
	/**
	 * 账号类型
	 * 0：普通玩家
	 * 1：代理
	 */
	private int accounttype;
	
	/**
	 * 用户姓名
	 */
	private String uname;
	
	/**
	 * 身份证号码
	 */
	private String identitycard;
	
	/**
	 * 电话号码
	 */
	private String phone;
	
	/**
	 * 电子邮件
	 */
	private String email;
	
	/**
	 * QQ
	 */
	private String qq;
	
	/**
	 * 注册时间
	 */
	private String createDate;
	
	/**
	 * 登录时间
	 */
	private String updateDate;
	
	/**
	 * 登录次数
	 */
	private Integer logincount;
	
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
	/**
	 * 用户类型标示默认0
	 * 0：线下会员
	 * 1：线上会员
	 */
	private int typeflag;
	
	/**
	 * URL
	 */
	private String url;
	
	/**
	 * 充值总金额
	 */
	private String paytotal;
	
	/**
	 * 首存优惠
	 */
	private String fastpay;
	
	/**
	 * 分机号码
	 */
	private String extno;

	public Long getUiid() {
		return uiid;
	}

	public void setUiid(Long uiid) {
		this.uiid = uiid;
	}

	public Long getBelongid() {
		return belongid;
	}

	public void setBelongid(Long belongid) {
		this.belongid = belongid;
	}

	public String getPayertype() {
		return payertype;
	}

	public void setPayertype(String payertype) {
		this.payertype = payertype;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(int accounttype) {
		this.accounttype = accounttype;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getIdentitycard() {
		return identitycard;
	}

	public void setIdentitycard(String identitycard) {
		this.identitycard = identitycard;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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

	public int getTypeflag() {
		return typeflag;
	}

	public void setTypeflag(int typeflag) {
		this.typeflag = typeflag;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getLogincount() {
		return logincount;
	}

	public void setLogincount(Integer logincount) {
		this.logincount = logincount;
	}

	public String getPaytotal() {
		return paytotal;
	}

	public void setPaytotal(String paytotal) {
		this.paytotal = paytotal;
	}

	public String getFastpay() {
		return fastpay;
	}

	public void setFastpay(String fastpay) {
		this.fastpay = fastpay;
	}
	

	public int getClienttype() {
		return clienttype;
	}

	public void setClienttype(int clienttype) {
		this.clienttype = clienttype;
	}

	public String getExtno() {
		return extno;
	}

	public void setExtno(String extno) {
		this.extno = extno;
	}

	@Override
	public String toString() {
		return "UserManager [uiid=" + uiid + ", belongid=" + belongid
				+ ", payertype=" + payertype + ", remark=" + remark
				+ ", account=" + account + ", accounttype=" + accounttype
				+ ", uname=" + uname + ", identitycard=" + identitycard
				+ ", phone=" + phone + ", email=" + email + ", qq=" + qq
				+ ", createDate=" + createDate + ", phonevalid=" + phonevalid
				+ ", emailvalid=" + emailvalid + ", typeflag=" + typeflag + ",extno="+extno+"]";
	}

	@Override
	public Serializable getID() {
		return this.getUiid();
	}

}
