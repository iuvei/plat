package com.na.test.batchbet.common;

/**
 * Created by Administrator on 2017/5/13 0013.
 */
public class BetLog {
    private Long userId;
    private Long balance;
    private Long time;

    public BetLog(Long userId, Long balance, Long time) {
        this.userId = userId;
        this.balance = balance;
        this.time = time;
    }

    public Long getTime() {

        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
}
