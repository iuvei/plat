package com.na.baccarat.socketserver.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.na.baccarat.socketserver.common.enums.BetOrderBetTypeEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderStatusEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderTableTypeEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderWinLostStatusEnum;
import com.na.baccarat.socketserver.common.enums.PlayEnum;
import com.na.baccarat.socketserver.common.enums.TradeItemEnum;

/**
 * 投注订单表。
 * Created by sunny on 2017/5/1 0001.
 */
public class BetOrder implements Serializable {
    /**订单表ID*/
    private Long id;
    /**用户ID*/
    private Long userId;
    /**用户登录名*/
    private String loginName;
    /**交易项ID*/
    private Integer tradeItemId;
    /**交易项KEY*/
    private String tradeItemKey;
    /**游戏ID*/
    private Integer gameId;
    /**游戏桌ID*/
    private Integer gameTableId;
    /**虚拟游戏桌ID*/
    private Integer virtualgametableId;
    /**玩法ID*/
    private Integer playId;
    /**靴ID（20150720-1）*/
    private String bootsId;
    /**局ID*/
    private Long roundId;
    /**局数（0++）*/
    private int roundNum;
    /**靴数（0++）*/
    private int bootsNum;
    /**投注金额*/
    private BigDecimal amount;
    /**投注赔率*/
    private BigDecimal betRate;
    /**洗码比*/
    private BigDecimal washPercentage;
    /**占成比*/
    private BigDecimal oPercentage;
    /**打水占成*/
    private BigDecimal waterPercentage;
    /**投注时间*/
    private Date betTime;
    /**状态：1 新建 2 确认 3 结算 0 取消*/
    private Integer status;
    /**输赢状态：1 输 2 赢 3 和 0 取消*/
    private Integer winLostStatus;
    /**输赢金额*/
    private BigDecimal winLostAmount;
    /**结算赔率*/
    private BigDecimal settleRate;
    /**结算时间*/
    private Date settleTime;/***/
    /**有效投注金额 (等于注额，除和局、取消)*/
    private BigDecimal validAmount;
    /**投注前余额*/
    private BigDecimal userPreBalance;
    /**开奖结果*/
    private String roundResult;
    /**投注来源：1 座位 、2 旁注、3 多台、4 好路*/
    private Integer source;
    /**
     * 投注来源内容。不同投注来源，存储不同值。（投注来源：1 座位（座位号） 、2 旁注（空）、3 多台（空）、4 好路（空））
     */
    private String sourceValue;
    /**投注类型：1 普通投注、2 语音投注、*/
    private Integer betType;
    /**桌台类型：0 普通台、1 包桌*/
    private Integer tableType;
    /**用户上级路径*/
    private String userParentPath;
    /**用户上级路径*/
    private Long parentId;
    /**用户下注IP*/
    private String betIp;
    /**
     * 下注终端类型
     * 1 web 2 ios 3 andriod 4 PC端
     */
    private Integer deviceType;
    /**
     * 下注终端信息
     * 类似：Mozilla/5.0 (Windows NT 10.0; Win64; x64)
     */
    private String deviceInfo;
    /**
     * 交易项
     */
    private TradeItem tradeItem;


    public TradeItem getTradeItem() {
		return tradeItem;
	}

	public void setTradeItem(TradeItem tradeItem) {
		this.tradeItem = tradeItem;
	}

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

    public Integer getVirtualgametableId() {
        return virtualgametableId;
    }

    public void setVirtualgametableId(Integer virtualgametableId) {
        this.virtualgametableId = virtualgametableId;
    }

    public Integer getPlayId() {
        return playId;
    }
    
    public PlayEnum getPlayEnum() {
    	return PlayEnum.get(playId);
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

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public int getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }

    public int getBootsNum() {
        return bootsNum;
    }

    public void setBootsNum(int bootsNum) {
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
    
    public BetOrderStatusEnum getStatusEnum() {
    	return BetOrderStatusEnum.get(this.status);
    }

    public Integer getWinLostStatus() {
        return winLostStatus;
    }
    
    public BetOrderWinLostStatusEnum getWinLostStatusEnum() {
    	if(winLostStatus == null) {
    		return null;
    	}
        return BetOrderWinLostStatusEnum.get(winLostStatus);
    }

    public void setWinLostStatus(Integer winLostStatus) {
        this.winLostStatus = winLostStatus;
    }
    
    public void setWinLostStatusEnum(BetOrderWinLostStatusEnum winLostStatus) {
        this.winLostStatus = winLostStatus.get();
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
    
    public BetOrderSourceEnum getSourceEnum() {
    	if(this.source == null) {
    		return null;
    	}
        return BetOrderSourceEnum.get(source);
    }

    public void setSource(Integer source) {
        this.source = source;
    }
    
    public void setSourceEnum(BetOrderSourceEnum source) {
        this.source = source.get();
    }

    public Integer getBetType() {
        return betType;
    }

    public void setBetType(Integer betType) {
        this.betType = betType;
    }
    
    public void setBetTypeEnum(BetOrderBetTypeEnum betTypeEnum) {
        this.betType = betTypeEnum.get();
    }

    public Integer getTableType() {
        return tableType;
    }

    public void setTableType(Integer tableType) {
        this.tableType = tableType;
    }
    
    public void setTableTypeEnum(BetOrderTableTypeEnum tableType) {
        this.tableType = tableType.get();
    }
    
    public BetOrderTableTypeEnum getTableTypeEnum() {
    	if(tableType == null) {
    		return null;
    	}
        return BetOrderTableTypeEnum.get(tableType);
    }

    public BetOrderBetTypeEnum getBetTypeEnum(){
        return BetOrderBetTypeEnum.get(betType);
    }

    public BetOrderSourceEnum getBetOrderSource(){
    	if(source == null) {
    		return null;
    	}
        return BetOrderSourceEnum.get(source);
    }

    public BetOrderTableTypeEnum getBetOrderTableType(){
        return BetOrderTableTypeEnum.get(source);
    }

    public String getUserParentPath() {
        return userParentPath;
    }

    public void setUserParentPath(String userParentPath) {
        this.userParentPath = userParentPath;
    }

    public String getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(String sourceValue) {
        this.sourceValue = sourceValue;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "BetOrder{" +
                "id=" + id +
                ", userId=" + userId +
                ", loginName='" + loginName + '\'' +
                ", tradeItemId=" + tradeItemId +
                ", tradeItemKey='" + tradeItemKey + '\'' +
                ", gameId=" + gameId +
                ", gameTableId=" + gameTableId +
                ", virtualgametableId=" + virtualgametableId +
                ", playId=" + playId +
                ", bootsId='" + bootsId + '\'' +
                ", roundId=" + roundId +
                ", roundNum=" + roundNum +
                ", bootsNum=" + bootsNum +
                ", amount=" + amount +
                ", betRate=" + betRate +
                ", washPercentage=" + washPercentage +
                ", oPercentage=" + oPercentage +
                ", betTime=" + betTime +
                ", status=" + status +
                ", winLostStatus=" + winLostStatus +
                ", winLostAmount=" + winLostAmount +
                ", settleRate=" + settleRate +
                ", settleTime=" + settleTime +
                ", validAmount=" + validAmount +
                ", userPreBalance=" + userPreBalance +
                ", roundResult='" + roundResult + '\'' +
                ", source=" + source +
                ", sourceValue='" + sourceValue + '\'' +
                ", betType=" + betType +
                ", tableType=" + tableType +
                ", userParentPath='" + userParentPath + '\'' +
                ", parentId=" + parentId +
                ", tradeItem=" + tradeItem +
                '}';
    }

    public String getTradeItemKey() {
        return tradeItemKey;
    }
    
    public TradeItemEnum getTradeItemKeyEnum() {
        return TradeItemEnum.get(tradeItemKey);
    }

    public void setTradeItemKey(String tradeItemKey) {
        this.tradeItemKey = tradeItemKey;
    }
    
    public void setTradeItemKeyEnum(TradeItemEnum tradeItemKey) {
        this.tradeItemKey = tradeItemKey.get();
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

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

	public BigDecimal getWaterPercentage() {
		return waterPercentage;
	}

	public void setWaterPercentage(BigDecimal waterPercentage) {
		this.waterPercentage = waterPercentage;
	}
}
