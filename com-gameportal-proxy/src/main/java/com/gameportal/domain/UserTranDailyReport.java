package com.gameportal.domain;

import java.math.BigDecimal;

/**
 * 会员每日交易报表
 * 
 * @author Administrator
 *
 */
public class UserTranDailyReport extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private long stId;

	private String createDate;

	private long uiid;

	private String puid;

	private String account;

	private String realName;

	private BigDecimal deposit;

	private BigDecimal withdrawal;

	private BigDecimal preferential;

	private BigDecimal washCode;

	private BigDecimal finalAmount;

	public long getStId() {
		return stId;
	}

	public void setStId(long stId) {
		this.stId = stId;
	}

	public long getUiid() {
		return uiid;
	}

	public void setUiid(long uiid) {
		this.uiid = uiid;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getPuid() {
		return puid;
	}

	public void setPuid(String puid) {
		this.puid = puid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	public BigDecimal getWithdrawal() {
		return withdrawal;
	}

	public void setWithdrawal(BigDecimal withdrawal) {
		this.withdrawal = withdrawal;
	}

	public BigDecimal getPreferential() {
		return preferential;
	}

	public void setPreferential(BigDecimal preferential) {
		this.preferential = preferential;
	}

	public BigDecimal getWashCode() {
		return washCode;
	}

	public void setWashCode(BigDecimal washCode) {
		this.washCode = washCode;
	}

	public BigDecimal getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(BigDecimal finalAmount) {
		this.finalAmount = finalAmount;
	}
}
