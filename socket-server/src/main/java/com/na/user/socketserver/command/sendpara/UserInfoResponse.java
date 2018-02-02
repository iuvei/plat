package com.na.user.socketserver.command.sendpara;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 试玩请求参数
 * 
 * @author alan
 * @date 2017年6月5日 上午11:22:52
 */
public class UserInfoResponse implements IResponse {
	
	/**
     * 昵称
     */
	private String nickName;
	
    /**
     * 注册时间
     */
	private Date registerTime;
	
	/**
     * 余额
     */
	private BigDecimal blance;
	
	/**
     * 投注次数
     */
	private String betCount;
	
	/**
     * 胜率
     */
	private String successRate;
	
	/**
     * 总收益
     */
	private String totalAmount;
	
	/**
     * 今日收益
     */
	private String todayAmount;

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public BigDecimal getBlance() {
		return blance;
	}

	public void setBlance(BigDecimal blance) {
		this.blance = blance;
	}

	public String getBetCount() {
		return betCount;
	}

	public void setBetCount(String betCount) {
		this.betCount = betCount;
	}

	public String getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(String successRate) {
		this.successRate = successRate;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTodayAmount() {
		return todayAmount;
	}

	public void setTodayAmount(String todayAmount) {
		this.todayAmount = todayAmount;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
    
    
}
