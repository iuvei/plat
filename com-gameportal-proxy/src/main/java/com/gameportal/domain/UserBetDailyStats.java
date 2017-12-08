package com.gameportal.domain;

import java.math.BigDecimal;

/**
 * 会员每日投注报表
 * @author Administrator
 *
 */
public class UserBetDailyStats extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private long stId;
	
	private String createDate;
	
	private long uiid;
	
	private String puid;
	
	private String account;
	
	private String realName;
	
	private double scale;
	
	private long betCount;
	
	private BigDecimal betAmount;
	
	private BigDecimal validAmount;
	
	private BigDecimal payoutAmount;
	
	private BigDecimal finalAmount;

	public long getStId() {
		return stId;
	}

	public void setStId(long stId) {
		this.stId = stId;
	}

	public long getUiid() {
		return uiid;
	}

	public void setUiid(long uiid) {
		this.uiid = uiid;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getPuid() {
		return puid;
	}

	public void setPuid(String puid) {
		this.puid = puid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public long getBetCount() {
		return betCount;
	}

	public void setBetCount(long betCount) {
		this.betCount = betCount;
	}

	public BigDecimal getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(BigDecimal betAmount) {
		this.betAmount = betAmount;
	}

	public BigDecimal getValidAmount() {
		return validAmount;
	}

	public void setValidAmount(BigDecimal validAmount) {
		this.validAmount = validAmount;
	}

	public BigDecimal getPayoutAmount() {
		return payoutAmount;
	}

	public void setPayoutAmount(BigDecimal payoutAmount) {
		this.payoutAmount = payoutAmount;
	}

	public BigDecimal getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(BigDecimal finalAmount) {
		this.finalAmount = finalAmount;
	}
}
