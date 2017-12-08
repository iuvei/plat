package com.gameportal.manage.member.model;

public class MemberInfoVo extends MemberInfo {

	private String puname;// 代理商名称
	private java.sql.Timestamp create_date;
	private java.sql.Timestamp update_date;

	/**
	 * 存款金额
	 */
	private String paymoney;

	/**
	 * 提款金额
	 */
	private String withdrawalMoney;

	/**
	 * 优惠金额
	 */
	private String couponMoney;

	/**
	 * 输赢金额(总存款-总提款)
	 */
	private String winMoney;

	/**
	 * 钱包余额
	 */
	private String accountMoney;

	private String manageIds;

	public String getPaymoney() {
		return paymoney;
	}

	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}

	public String getPuname() {
		return puname;
	}

	public void setPuname(String puname) {
		this.puname = puname;
	}

	public java.sql.Timestamp getCreate_date() {
		return create_date;
	}

	public void setCreate_date(java.sql.Timestamp createDate) {
		create_date = createDate;
	}

	public java.sql.Timestamp getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(java.sql.Timestamp updateDate) {
		update_date = updateDate;
	}

	public String getWithdrawalMoney() {
		return withdrawalMoney;
	}

	public void setWithdrawalMoney(String withdrawalMoney) {
		this.withdrawalMoney = withdrawalMoney;
	}

	public String getCouponMoney() {
		return couponMoney;
	}

	public void setCouponMoney(String couponMoney) {
		this.couponMoney = couponMoney;
	}

	public String getWinMoney() {
		return winMoney;
	}

	public void setWinMoney(String winMoney) {
		this.winMoney = winMoney;
	}

	public String getAccountMoney() {
		return accountMoney;
	}

	public void setAccountMoney(String accountMoney) {
		this.accountMoney = accountMoney;
	}

	public String getManageIds() {
		return manageIds;
	}

	public void setManageIds(String manageIds) {
		this.manageIds = manageIds;
	}

}
