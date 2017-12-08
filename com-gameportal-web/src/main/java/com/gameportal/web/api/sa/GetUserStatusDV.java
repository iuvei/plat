package com.gameportal.web.api.sa;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("GetUserStatusResponse")
public class GetUserStatusDV extends UserInfoResponse {
	@XStreamAlias("IsSuccess")
	private boolean success;

	@XStreamAlias("Balance")
	private String balance;

	@XStreamAlias("Online")
	private String online;

	@XStreamAlias("Betted")
	private String betted;

	@XStreamAlias("BettedAmount")
	private String bettedAmount;

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public String getBetted() {
		return betted;
	}

	public void setBetted(String betted) {
		this.betted = betted;
	}

	public String getBettedAmount() {
		return bettedAmount;
	}

	public void setBettedAmount(String bettedAmount) {
		this.bettedAmount = bettedAmount;
	}
}
