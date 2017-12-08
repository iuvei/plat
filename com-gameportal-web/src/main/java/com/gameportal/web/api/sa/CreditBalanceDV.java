package com.gameportal.web.api.sa;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CreditBalanceResponse")
public class CreditBalanceDV extends UserInfoResponse{

	@XStreamAlias("Balance")
	private double balance;
	
	@XStreamAlias("CreditAmount")
	private double creditAmount;
	
	@XStreamAlias("OrderId")
	private String orderId;

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(double creditAmount) {
		this.creditAmount = creditAmount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
