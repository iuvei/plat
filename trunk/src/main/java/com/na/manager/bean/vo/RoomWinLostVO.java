package com.na.manager.bean.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.manager.enums.LiveUserType;

import java.math.BigDecimal;

/**
 * 包房会员输赢统计
 * @author andy
 * @date 2017年6月24日 上午10:44:43
 * 
 */
public class RoomWinLostVO {
	/**
	 * 账号
	 */
	private String loginName;
	
	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 代理ID
	 */
	private Long agentId;

	/**
	 * 用户类型
	 */
	private Integer liveUserType;

	/**
	 * 路径
	 */
	private String parentPath;

	/**
	 * 交易次数
	 */
	private String tradeTime;
	/**
	 * 投注金额
	 */
	private BigDecimal amountBetting;
	
	/**
	 * 庄投注总额
	 */
	private BigDecimal bankTotal = BigDecimal.ZERO;
	
	/**
	 * 闲投注总额
	 */
	private BigDecimal playerTotal = BigDecimal.ZERO;

	/**
	 * 输赢金额
	 */
	private BigDecimal winLostAmount = BigDecimal.ZERO;
	
	/**
	 * 洗码量
	 */
	private BigDecimal washBetting = BigDecimal.ZERO;
	
	/**
	 * 洗码比
	 */
	private String washPercentage = "0";
	
	/**
	 * 洗码金额
	 */
	private BigDecimal washAmount = BigDecimal.ZERO;

	/**
	 * 公司获利比
	 */
	private BigDecimal winloSepercentage = BigDecimal.ZERO;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public BigDecimal getAmountBetting() {
		return amountBetting;
	}

	public void setAmountBetting(BigDecimal amountBetting) {
		this.amountBetting = amountBetting;
	}

	public BigDecimal getWinLostAmount() {
		return winLostAmount;
	}

	public void setWinLostAmount(BigDecimal winLostAmount) {
		this.winLostAmount = winLostAmount;
	}

	public BigDecimal getWashBetting() {
		return washBetting;
	}

	public void setWashBetting(BigDecimal washBetting) {
		this.washBetting = washBetting;
	}

	public String getWashPercentage() {
		return washPercentage;
	}

	public void setWashPercentage(String washPercentage) {
		this.washPercentage = washPercentage;
	}

	public BigDecimal getWashAmount() {
		return washAmount;
	}

	public void setWashAmount(BigDecimal washAmount) {
		this.washAmount = washAmount;
	}

	public BigDecimal getWinloSepercentage() {
		return winloSepercentage;
	}

	public void setWinloSepercentage(BigDecimal winloSepercentage) {
		this.winloSepercentage = winloSepercentage;
	}

	public Integer getLiveUserType() {
		return liveUserType;
	}

	public void setLiveUserType(Integer liveUserType) {
		this.liveUserType = liveUserType;
	}

	public BigDecimal getBankTotal() {
		return bankTotal;
	}

	public void setBankTotal(BigDecimal bankTotal) {
		this.bankTotal = bankTotal;
	}

	public BigDecimal getPlayerTotal() {
		return playerTotal;
	}

	public void setPlayerTotal(BigDecimal playerTotal) {
		this.playerTotal = playerTotal;
	}

	@JSONField(serialize = false)
	public LiveUserType getLiveUserTypeEnum(){
		return LiveUserType.get(this.liveUserType);
	}
}
