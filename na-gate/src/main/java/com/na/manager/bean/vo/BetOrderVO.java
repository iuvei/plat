package com.na.manager.bean.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author andy
 * @date 2017年6月22日 下午2:29:46
 * 
 */
public class BetOrderVO implements Serializable{
	private static final long serialVersionUID = 1L;
	//游戏桌
    private String gameTable;
    // 游戏名称
    private String gameName;
	// 靴数（0++）
	private String bootsNum;
	// 局数（0++）
	private String roundNum;
	// 结算赔率
	private BigDecimal settleRate;
	// 下注前余额
	private BigDecimal preBalance;
	// 投注时间
	private String betTime;
	// 结算时间
	private String settleTime;
	// 代理名
	private String agentName;
	// 用户名
	private String userName;
	// 桌子ID
	private Integer tableId;
	// 桌子类型
	private Integer tableType;
	// 游戏ID
	private Integer gameId;
	// 局ID
	private String roundId;
	// 开奖结果
	private String roundResult;
	// 局结果
	private String result;
	// 交易项KEY
	private String itemKey;
	// 交易项名称
	private String itemName;
	// 交易项ID
	private long itemId;
	// 投注编号
	private String betId;
	// 轮盘投注详情
	private String addition;
	// 投注类型ID
	private String betTypeId;
	// 下注金额
	private BigDecimal betAmount;
	// 投注号码
	private String betNum;
	// 输赢状态：1 输 2 赢 3 和 0 取消
	private Integer winLostStatus;
	// 输赢金额（不含本金，输为负数，赢为正数）
	private BigDecimal winLostAmount;
	// 用户ID
	private Long userId;
	// 下注金额
	private BigDecimal amount;
	// 有效下注金额
	private BigDecimal validAmount;
	// 下注IP
	private String betIp;
	// 下注设备类型：1 WEB 2 IOS 3 ANDROID
	private Integer deviceType;

	public String getGameTable() {
		return gameTable;
	}

	public void setGameTable(String gameTable) {
		this.gameTable = gameTable;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getBootsNum() {
		return bootsNum;
	}

	public void setBootsNum(String bootsNum) {
		this.bootsNum = bootsNum;
	}

	public String getRoundNum() {
		return roundNum;
	}

	public void setRoundNum(String roundNum) {
		this.roundNum = roundNum;
	}

	public BigDecimal getSettleRate() {
		return settleRate;
	}

	public void setSettleRate(BigDecimal settleRate) {
		this.settleRate = settleRate;
	}

	public BigDecimal getPreBalance() {
		return preBalance;
	}

	public void setPreBalance(BigDecimal preBalance) {
		this.preBalance = preBalance;
	}

	public String getBetTime() {
		return betTime;
	}

	public void setBetTime(String betTime) {
		this.betTime = betTime;
	}

	public String getSettleTime() {
		return settleTime;
	}

	public void setSettleTime(String settleTime) {
		this.settleTime = settleTime;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public Integer getTableType() {
		return tableType;
	}

	public void setTableType(Integer tableType) {
		this.tableType = tableType;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public String getRoundId() {
		return roundId;
	}

	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public String getBetId() {
		return betId;
	}

	public void setBetId(String betId) {
		this.betId = betId;
	}

	public String getAddition() {
		return addition;
	}

	public void setAddition(String addition) {
		this.addition = addition;
	}

	public String getBetTypeId() {
		return betTypeId;
	}

	public void setBetTypeId(String betTypeId) {
		this.betTypeId = betTypeId;
	}

	public BigDecimal getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(BigDecimal betAmount) {
		this.betAmount = betAmount;
	}

	public String getBetNum() {
		return betNum;
	}

	public void setBetNum(String betNum) {
		this.betNum = betNum;
	}

	public Integer getWinLostStatus() {
		return winLostStatus;
	}

	public void setWinLostStatus(Integer winLostStatus) {
		this.winLostStatus = winLostStatus;
	}

	public BigDecimal getWinLostAmount() {
		return winLostAmount;
	}

	public void setWinLostAmount(BigDecimal winLostAmount) {
		this.winLostAmount = winLostAmount;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getValidAmount() {
		return validAmount;
	}

	public void setValidAmount(BigDecimal validAmount) {
		this.validAmount = validAmount;
	}

	public String getBetIp() {
		return betIp;
	}

	public void setBetIp(String betIp) {
		this.betIp = betIp;
	}

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public String getRoundResult() {
		return roundResult;
	}

	public void setRoundResult(String roundResult) {
		this.roundResult = roundResult;
	}
}
