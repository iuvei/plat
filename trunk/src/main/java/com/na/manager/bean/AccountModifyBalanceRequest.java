package com.na.manager.bean;

import java.math.BigDecimal;

import com.na.manager.enums.AccountRecordType;
import com.na.manager.enums.UserType;

/**
 * 交易流水
 * 
 * @author andy
 * @date 2017年6月23日 上午9:49:53
 * 
 */
public class AccountModifyBalanceRequest {
	
	private Long id;
	private Long parentId;
	private Integer userType;
	private Integer accountRecordType;
	private BigDecimal balance;
	private String remark;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public UserType getUserTypeEnum() {
		if(userType == null) {
			return null;
		}
		return UserType.get(userType);
	}
	public void setUserTypeEnum(UserType userType) {
		this.userType = userType.get();
	}
	public Integer getAccountRecordType() {
		return accountRecordType;
	}
	public void setAccountRecordType(Integer accountRecordType) {
		this.accountRecordType = accountRecordType;
	}
	public AccountRecordType getAccountRecordTypeEnum() {
		if(accountRecordType == null) {
			return null;
		}
		return AccountRecordType.get(accountRecordType);
	}
	public void setAccountRecordTypeEnum(AccountRecordType accountRecordType) {
		this.accountRecordType = accountRecordType.get();
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
}
