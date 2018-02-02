package com.na.manager.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投注记录表
 * 
 * @author andy add by 2017.05.16
 */
public class BetOrder implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * ID
	 */
	private Long id;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 用户登陆名
	 */
	private String loginName;

	/**
	 * 交易项ID
	 */
	private Integer tradeItemId;

	/**
	 * 交易项KEY
	 */
	private String tradeItemKey;

	/**
	 * 游戏桌ID
	 */
	private Integer gameId;

	/**
	 * 游戏桌ID
	 */
	private Integer gameTableId;

	/**
	 * 虚拟游戏桌ID
	 */
	private Integer virtualGametableId;

	/**
	 * 玩法ID
	 */
	private Integer playId;

	/**
	 * 靴ID（20150720-1）
	 */
	private String bootsId;

	/**
	 * 局ID
	 */
	private Integer roundId;

	/**
	 * 局数（0++）
	 */
	private Integer roundNum;

	/**
	 * 靴数（0++）
	 */
	private Integer bootsNum;

	/**
	 * 投注金额
	 */
	private BigDecimal amount;

	/**
	 * 投注赔率
	 */
	private BigDecimal betRate;

	/**
	 * 洗码比
	 */
	private BigDecimal washPercentage;

	/**
	 * 占成
	 */
	private BigDecimal oPercentage;

	/**
	 * 投注时间
	 */
	private Date betTime;

	/**
	 * 1 新建 2 确认 3 结算 0 取消
	 */
	private Integer status;

	/**
	 * 1 输 2 赢 3 和 0 取消
	 */
	private Integer winLostStatus;

	/**
	 * 输赢金额（不含本金，输为负数，赢为正数）
	 */
	private BigDecimal winLostAmount;

	/**
	 * 结算赔率
	 */
	private BigDecimal settleRate;

	/**
	 * 结算时间
	 */
	private Date settleTime;

	/**
	 * 有效投注金额 (等于注额，除和局、取消)
	 */
	private BigDecimal validAmount;

	/**
	 * 投注前余额
	 */
	private BigDecimal userPreBalance;

	/**
	 * 开奖结果
	 */
	private String roundResult;

	/**
	 * 投注来源：1 座位 、2 旁注、3 多台、4 好路
	 */
	private Integer source;

	/**
	 * 投注来源内容。不同投注来源，存储不同值。（投注来源：1 座位（座位号） 、2 旁注（空）、3 多台（空）、4 好路（空））
	 */
	private String sourceValue;

	/**
	 * 投注类型：1 普通投注、2 语音投注
	 */
	private Integer betType;

	/**
	 * 桌台类型：0 普通台、1 包台
	 */
	private Integer tableType;

	/**
	 * 上级ID
	 */
	private Integer parentId;

	/**
	 * 用户上级路径
	 */
	private String userParentPath;
	/**
	 * 用户下注IP
	 */
	private String betIp;

	/**
	 * 下注设备类型：1 WEB 2 IOS 3 ANDROID
	 */
	private String deviceType;

	/**
	 * 设备信息
	 */
	private String deviceInfo;

	/**
	 * 用户点数调整后余额
	 */
	private BigDecimal modifiedUserBalance;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Integer getTradeItemId() {
		return tradeItemId;
	}

	public void setTradeItemId(Integer tradeItemId) {
		this.tradeItemId = tradeItemId;
	}

	public String getTradeItemKey() {
		return tradeItemKey;
	}

	public void setTradeItemKey(String tradeItemKey) {
		this.tradeItemKey = tradeItemKey;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public Integer getGameTableId() {
		return gameTableId;
	}

	public void setGameTableId(Integer gameTableId) {
		this.gameTableId = gameTableId;
	}

	public Integer getVirtualGametableId() {
		return virtualGametableId;
	}

	public void setVirtualGametableId(Integer virtualGametableId) {
		this.virtualGametableId = virtualGametableId;
	}

	public Integer getPlayId() {
		return playId;
	}

	public void setPlayId(Integer playId) {
		this.playId = playId;
	}

	public String getBootsId() {
		return bootsId;
	}

	public void setBootsId(String bootsId) {
		this.bootsId = bootsId;
	}

	public Integer getRoundId() {
		return roundId;
	}

	public void setRoundId(Integer roundId) {
		this.roundId = roundId;
	}

	public Integer getRoundNum() {
		return roundNum;
	}

	public void setRoundNum(Integer roundNum) {
		this.roundNum = roundNum;
	}

	public Integer getBootsNum() {
		return bootsNum;
	}

	public void setBootsNum(Integer bootsNum) {
		this.bootsNum = bootsNum;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBetRate() {
		return betRate;
	}

	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}

	public BigDecimal getWashPercentage() {
		return washPercentage;
	}

	public void setWashPercentage(BigDecimal washPercentage) {
		this.washPercentage = washPercentage;
	}

	public BigDecimal getoPercentage() {
		return oPercentage;
	}

	public void setoPercentage(BigDecimal oPercentage) {
		this.oPercentage = oPercentage;
	}

	public Date getBetTime() {
		return betTime;
	}

	public void setBetTime(Date betTime) {
		this.betTime = betTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public BigDecimal getSettleRate() {
		return settleRate;
	}

	public void setSettleRate(BigDecimal settleRate) {
		this.settleRate = settleRate;
	}

	public Date getSettleTime() {
		return settleTime;
	}

	public void setSettleTime(Date settleTime) {
		this.settleTime = settleTime;
	}

	public BigDecimal getValidAmount() {
		return validAmount;
	}

	public void setValidAmount(BigDecimal validAmount) {
		this.validAmount = validAmount;
	}

	public BigDecimal getUserPreBalance() {
		return userPreBalance;
	}

	public void setUserPreBalance(BigDecimal userPreBalance) {
		this.userPreBalance = userPreBalance;
	}

	public String getRoundResult() {
		return roundResult;
	}

	public void setRoundResult(String roundResult) {
		this.roundResult = roundResult;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getSourceValue() {
		return sourceValue;
	}

	public void setSourceValue(String sourceValue) {
		this.sourceValue = sourceValue;
	}

	public Integer getBetType() {
		return betType;
	}

	public void setBetType(Integer betType) {
		this.betType = betType;
	}

	public Integer getTableType() {
		return tableType;
	}

	public void setTableType(Integer tableType) {
		this.tableType = tableType;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getUserParentPath() {
		return userParentPath;
	}

	public void setUserParentPath(String userParentPath) {
		this.userParentPath = userParentPath;
	}

	public String getBetIp() {
		return betIp;
	}

	public void setBetIp(String betIp) {
		this.betIp = betIp;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public BigDecimal getModifiedUserBalance() {
		return modifiedUserBalance;
	}

	public void setModifiedUserBalance(BigDecimal modifiedUserBalance) {
		this.modifiedUserBalance = modifiedUserBalance;
	}
}
