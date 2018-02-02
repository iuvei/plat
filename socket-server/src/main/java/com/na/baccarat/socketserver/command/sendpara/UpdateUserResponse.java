package com.na.baccarat.socketserver.command.sendpara;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.CommandResponse;

/**
 * 跟新用余额返回信息
 * @author Administrator
 *
 */
public class UpdateUserResponse extends CommandResponse {

	@JSONField(name = "atm")
	private BigDecimal userBalance;

	public BigDecimal getUserBalance() {
		return userBalance;
	}

	public void setUserBalance(BigDecimal userBalance) {
		this.userBalance = userBalance;
	}
	
	

}
