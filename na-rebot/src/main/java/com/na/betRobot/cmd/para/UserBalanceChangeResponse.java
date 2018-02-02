package com.na.betRobot.cmd.para;

import java.math.BigDecimal;

/**
 * 用户余额变动响应。
 * Created by sunny on 2017/5/13 0013.
 */
public class UserBalanceChangeResponse {
	
    /**用户ID*/
    private Long userId;
    /**用户余额*/
    private BigDecimal balance;

    public UserBalanceChangeResponse(Long userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
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
}
