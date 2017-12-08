package com.gameportal.web.api;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * VGS游戏接口返回参数
 * 
 * @author Administrator
 *
 */
@XStreamAlias("DATA")
public class VGSAPIResponse {

	@XStreamAlias("Status")
	private String status;

	@XStreamAlias("StatusCode")
	private String statusCode;

	@XStreamAlias("Description")
	private String description;

	@XStreamAlias("CasinoID")
	private String casinoId;

	@XStreamAlias("OperatorID")
	private String operatorId;

	@XStreamAlias("UserName")
	private String userName;

	@XStreamAlias("UserPWD")
	private String userPwd;

	@XStreamAlias("UserID")
	private String userId;

	@XStreamAlias("AccountNumber")
	private String accountNumber;

	@XStreamAlias("AccountPin")
	private String accountPin;

	@XStreamAlias("Amount")
	private String amount;

	@XStreamAlias("TransactionType")
	private String transactionType;

	@XStreamAlias("TransactionID")
	private String transactionId;

	@XStreamAlias("LoginName")
	private String loginName;

	@XStreamAlias("IntegratorUserID")
	private String integratorUserId;

	@XStreamAlias("Balance")
	private String balance;

	@XStreamAlias("PlayerCurrency")
	private String playerCurrency;
	
	@XStreamAlias("UserType")
	private String userType;
	
	@XStreamAlias("Currency")
	private String currency;

	public VGSAPIResponse() {
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCasinoId() {
		return casinoId;
	}

	public void setCasinoId(String casinoId) {
		this.casinoId = casinoId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountPin() {
		return accountPin;
	}

	public void setAccountPin(String accountPin) {
		this.accountPin = accountPin;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getIntegratorUserId() {
		return integratorUserId;
	}

	public void setIntegratorUserId(String integratorUserId) {
		this.integratorUserId = integratorUserId;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getPlayerCurrency() {
		return playerCurrency;
	}

	public void setPlayerCurrency(String playerCurrency) {
		this.playerCurrency = playerCurrency;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}	
}
