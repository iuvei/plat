package com.gameportal.manage.api.sa;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("DebitBalanceResponse")
public class DebitBalanceDV extends UserInfoResponse{
	
	@XStreamAlias("Balance")
	private double balance;
	
	@XStreamAlias("DebitAmount")
	private double DebitAmount;
	
	@XStreamAlias("OrderId")
	private String orderId;

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getDebitAmount() {
		return DebitAmount;
	}

	public void setDebitAmount(double debitAmount) {
		DebitAmount = debitAmount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
