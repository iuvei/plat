package com.na.baccarat.socketserver.command.sendpara;

import java.math.BigDecimal;

import com.na.user.socketserver.command.sendpara.IResponse;

/**
 * 用户余额变动响应。
 * Created by sunny on 2017/5/13 0013.
 */
public class UserBalanceChangeResponse implements IResponse {
    /**用户ID*/
    private Long userId;
    /**用户余额*/
    private BigDecimal balance;
    //由于什么改变余额
    private String affect;

    public UserBalanceChangeResponse(Long userId, BigDecimal balance, String affect) {
        this.userId = userId;
        this.balance = balance;
        this.affect = affect;
    }

    public UserBalanceChangeResponse() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

	public String getAffect() {
		return affect;
	}

	public void setAffect(String affect) {
		this.affect = affect;
	}
}
