package com.na.betRobot.entity;

import java.math.BigDecimal;
import java.util.List;

import com.na.betRobot.entity.enums.LiveUserType;
import com.na.betRobot.entity.enums.UserIsBet;

/**
 * 真人用户
 * 
 * @author alan
 * @date 2017年6月21日 下午6:02:17
 */
public class LiveUser extends User {
	
	/**
	 * 用户是否能投注
	 */
	private Integer isBet;
	/**
	 * 历史最大赢额
	 */
	private BigDecimal winMoney;
	/**
	 * 历史最大赢额
	 */
	private BigDecimal biggestWinMoney;
	/**
	 * 最高余额
	 */
	private BigDecimal biggestBalance;
	
	/**
	 * 洗码比
	 */
	private BigDecimal washPercentage;
	
	/**
	 * 占成比
	 */
	private BigDecimal intoPercentage;
	/**
	 * 用户来源 1 代理网 2 现金网
	 */
	private Integer source;
	/**
	 * 上级ID
	 */
	private Long parentId;
	/**
	 * 所有上级路径，用/隔开
	 */
	private String parentPath;
	/**
	 * 1 代理 2 会员
	 */
	private Integer type;
	/**
	 * 用户限红  逗号隔开
	 */
	private String chips;
	
	/**
	 * 用户筹码
	 */
	private List<UserChips> userChipList;
	
	public Integer getIsBet() {
		return isBet;
	}
	public void setIsBet(Integer isBet) {
		this.isBet = isBet;
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
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	public UserIsBet getIsBetEnum() {
		return UserIsBet.get(this.isBet);
	}
	
	public LiveUserType getTypeEnum() {
		return LiveUserType.get(this.type);
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
	public List<UserChips> getUserChipList() {
		return userChipList;
	}
	public void setUserChipList(List<UserChips> userChipList) {
		this.userChipList = userChipList;
	}
	
	
}
