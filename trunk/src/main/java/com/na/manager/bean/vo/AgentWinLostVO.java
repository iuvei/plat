package com.na.manager.bean.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.manager.enums.LiveUserType;

import java.math.BigDecimal;

/**
 * @author andy
 * @date 2017年6月24日 上午10:44:43
 * 
 */
public class AgentWinLostVO {
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
	 * 统计类型
	 */
	private Integer statisType;
	/**
	 * 统计统计类型描述
	 */
	private String statisTypeDesc;
	/**
	 * 交易次数
	 */
	private String tradeTime;
	/**
	 * 投注金额
	 */
	private BigDecimal amountBetting;
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
	 * 代理洗码金额
	 */
	private BigDecimal washAmount = BigDecimal.ZERO;
	
	/**
	 * 直属会员洗码金额
	 */
	private BigDecimal memberWashAmount = BigDecimal.ZERO;
	/**
	 * 代理总收入
	 */
	private BigDecimal agentIncome = BigDecimal.ZERO;
	/**
	 * 代理占成比
	 */
	private String intoPercentage = "0";
	/**
	 * 代理交公司收入
	 */
	private BigDecimal agentCompanyIncome = BigDecimal.ZERO;
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

	public Integer getStatisType() {
		return statisType;
	}

	public void setStatisType(Integer statisType) {
		this.statisType = statisType;
	}

	public String getStatisTypeDesc() {
		return statisTypeDesc;
	}

	public void setStatisTypeDesc(String statisTypeDesc) {
		this.statisTypeDesc = statisTypeDesc;
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

	public BigDecimal getAgentIncome() {
		return agentIncome;
	}

	public void setAgentIncome(BigDecimal agentIncome) {
		this.agentIncome = agentIncome;
	}

	public String getIntoPercentage() {
		return intoPercentage;
	}

	public void setIntoPercentage(String intoPercentage) {
		this.intoPercentage = intoPercentage;
	}

	public BigDecimal getAgentCompanyIncome() {
		return agentCompanyIncome;
	}

	public void setAgentCompanyIncome(BigDecimal agentCompanyIncome) {
		this.agentCompanyIncome = agentCompanyIncome;
	}

	public BigDecimal getWinloSepercentage() {
		return winloSepercentage;
	}

	public void setWinloSepercentage(BigDecimal winloSepercentage) {
		this.winloSepercentage = winloSepercentage;
	}

	public BigDecimal getMemberWashAmount() {
		return memberWashAmount;
	}

	public void setMemberWashAmount(BigDecimal memberWashAmount) {
		this.memberWashAmount = memberWashAmount;
	}

	public Integer getLiveUserType() {
		return liveUserType;
	}

	public void setLiveUserType(Integer liveUserType) {
		this.liveUserType = liveUserType;
	}

	@JSONField(serialize = false)
	public LiveUserType getLiveUserTypeEnum(){
		return LiveUserType.get(this.liveUserType);
	}
}
