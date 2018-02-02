package com.na.manager.entity;

import java.math.BigDecimal;

import com.na.manager.enums.LiveUserIsBet;
import com.na.manager.enums.LiveUserSource;
import com.na.manager.enums.LiveUserType;

/**
 * Created by Administrator on 2017/6/21 0021.
 */
public class LiveUser extends User{
    private Long userId;
    private Integer isBet;
    private Integer roomMember;
    private Integer source;
    private Integer type;
    private Long parentId;
    private String parentPath;
    private BigDecimal washPercentage;
    private BigDecimal intoPercentage;
    private BigDecimal winMoney;
    /**
     * 最高赢额
     */
    private BigDecimal biggestWinMoney;
    /**
     * 最高余额
     */
    private BigDecimal biggestBalance;
    private String chips;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getIsBet() {
        return isBet;
    }

    public LiveUserIsBet getIsBetEnum(){
    	if(isBet == null) {
    		return null;
    	}
        return LiveUserIsBet.get(isBet);
    }

    public void setIsBet(Integer isBet) {
        this.isBet = isBet;
    }

    public void setIsBet(LiveUserIsBet isBet){
        this.isBet = isBet.get();
    }

    public Integer getSource() {
        return source;
    }

    public LiveUserSource getSourceEnum(){
    	if(source == null) {
    		return null;
    	}
        return LiveUserSource.get(source);
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public void setSource(LiveUserSource source){
        this.source = source.get();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public BigDecimal getWashPercentage() {
        return washPercentage;
    }

    public void setWashPercentage(BigDecimal washPercentage) {
        this.washPercentage = washPercentage;
    }

    public BigDecimal getIntoPercentage() {
        return intoPercentage;
    }

    public void setIntoPercentage(BigDecimal intoPercentage) {
        this.intoPercentage = intoPercentage;
    }

    public BigDecimal getWinMoney() {
        return winMoney;
    }

    public void setWinMoney(BigDecimal winMoney) {
        this.winMoney = winMoney;
    }

    public BigDecimal getBiggestWinMoney() {
        return biggestWinMoney;
    }

    public void setBiggestWinMoney(BigDecimal biggestWinMoney) {
        this.biggestWinMoney = biggestWinMoney;
    }

    public Integer getType() {
        return type;
    }

    public LiveUserType getTypeEnum(){
    	if(type == null) {
    		return null;
    	}
        return LiveUserType.get(type);
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setType(LiveUserType type) {
        this.type = type.get();
    }

	public String getChips() {
		return chips;
	}

	public void setChips(String chips) {
		this.chips = chips;
	}

	public BigDecimal getBiggestBalance() {
		return biggestBalance;
	}

	public void setBiggestBalance(BigDecimal biggestBalance) {
		this.biggestBalance = biggestBalance;
	}

	public Integer getRoomMember() {
		return roomMember;
	}

	public void setRoomMember(Integer roomMember) {
		this.roomMember = roomMember;
	}
}
