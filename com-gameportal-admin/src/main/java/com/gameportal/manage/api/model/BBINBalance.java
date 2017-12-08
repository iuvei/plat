package com.gameportal.manage.api.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用户余额实体类。
 * 
 * @author sum
 *
 */
public class BBINBalance {
	// 账号
	@JsonProperty("LoginName")
	private String loginName;
	// 币种
	@JsonProperty("Currency")
	private String currency;
	// 额度
	@JsonProperty("Balance")
	private BigDecimal balance;
	// 总额度
	@JsonProperty("TotalBalance")
	private BigDecimal totalBalance;

	public String getLoginName() {
		return loginName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
