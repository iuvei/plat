package com.na.manager.bean;

import java.math.BigDecimal;

/**
 * 交易流水
 * 
 * @author andy
 * @date 2017年6月23日 上午9:49:53
 * 
 */
public class RobotBatchAddWithSnRequest {
	private String userName;
	private String password;
	private String nickName;
	private String initPoint;
	private BigDecimal amount;
	private int start;
	private int end;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getInitPoint() {
		return initPoint;
	}

	public void setInitPoint(String initPoint) {
		this.initPoint = initPoint;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
