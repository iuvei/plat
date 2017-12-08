package com.gameportal.web.api;

import com.gameportal.web.util.MD5Util;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * VGS游戏接口请求参数。
 * 
 * @author Administrator
 *
 */
@XStreamAlias("DATA")
public class VGSAPIRequest {
	@XStreamAlias("CasinoID")
	private String casinoId = "23";

	@XStreamAlias("OperatorID")
	private String operatorId = "30533";

	@XStreamAlias("UserName")
	private String userName;

	@XStreamAlias("UserPWD")
	private String userPwd = "opdxy123";

	@XStreamAlias("UserID")
	private String userId;

	@XStreamAlias("AccountNumber")
	private String accountNumber = "131266";

	@XStreamAlias("AccountPin")
	private String accountPin = "2798";

	@XStreamAlias("Amount")
	private String amount = "0";

	@XStreamAlias("TransactionType")
	private String transactionType;

	@XStreamAlias("TransactionID")
	private String transactionId;

	@XStreamAlias("Hash")
	private String hash;

	/**
	 * 互转时，忽略此属性
	 */
	@XStreamOmitField
	private String passKey;
	
	/**
	 * 构建Hash字串
	 * @return
	 */
	public String buildHashStr(){
		String origStr = this.getUserName() + this.getAmount() + this.getTransactionId() + this.getPassKey();
		return MD5Util.getMD5Encode(origStr);
	}

	public VGSAPIRequest() {
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

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getPassKey() {
		return passKey;
	}

	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}

}
