package com.gameportal.web.game.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.gameportal.web.user.model.BaseEntity;

public class PlatformBetStats extends BaseEntity{

	/**
	 * 主键
	 */
	private Integer id;
	/**
	 * 游戏平台
	 */
	private String platform;
	/**
	 * 统计时间
	 */
	private Date createDate;
	/**
	 * 投注总金额
	 */
	private BigDecimal betTotal;
	/**
	 * 注单总次数
	 */
	private Integer betNum;
	/**
	 * 有效投注金额
	 */
	private BigDecimal validBetAmount;
	/**
	 * 派彩金额
	 */
	private BigDecimal payoutAmount;
	/**
	 * 最终输赢金额
	 */
	private BigDecimal finalAmount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public BigDecimal getBetTotal() {
		return betTotal;
	}

	public void setBetTotal(BigDecimal betTotal) {
		this.betTotal = betTotal;
	}

	public Integer getBetNum() {
		return betNum;
	}

	public void setBetNum(Integer betNum) {
		this.betNum = betNum;
	}

	public BigDecimal getValidBetAmount() {
		return validBetAmount;
	}

	public void setValidBetAmount(BigDecimal validBetAmount) {
		this.validBetAmount = validBetAmount;
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

	@Override
	public Serializable getID() {
		return getID();
	}

}
