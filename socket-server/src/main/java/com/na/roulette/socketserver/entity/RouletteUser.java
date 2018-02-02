package com.na.roulette.socketserver.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.na.roulette.socketserver.common.limitrule.RouletteCheckLimitRule;
import com.na.user.socketserver.entity.UserPO;

public class RouletteUser implements Serializable {

	private static final long serialVersionUID = 1688179793687246869L;
	

	public RouletteUser() {
	}
	
	// 当前的限红ID
	private Integer chipId;
	// 统计未下注局数   用于不下注踢出用户  第三局警告  第五局强制踢出
	private Integer notBetRoundNumber = 0;
	
	private UserPO userPO;
	//个人限红和台红
	private RouletteCheckLimitRule limitRule;
	
	/**
	 * 统计下注情况
	 * key 交易项ID， val 交易金额
	 */
	private Map<Integer,BigDecimal> tradeItemBetMoneyMap = new ConcurrentHashMap<>();
	
	public Map<Integer, BigDecimal> getTradeItemBetMoneyMap() {
		return tradeItemBetMoneyMap;
	}

	public void setTradeItemBetMoneyMap(
			Map<Integer, BigDecimal> tradeItemBetMoneyMap) {
		this.tradeItemBetMoneyMap = tradeItemBetMoneyMap;
	}

	public void setTradeItemBetMoney(Integer tradeId, BigDecimal amount) {
		if(tradeItemBetMoneyMap.containsKey(tradeId)) {
			tradeItemBetMoneyMap.put(tradeId, amount.add(tradeItemBetMoneyMap.get(tradeId)));
		} else {
			tradeItemBetMoneyMap.put(tradeId, amount);
		}
	}
	
	public RouletteCheckLimitRule getLimitRule() {
		return limitRule;
	}

	public void setLimitRule(RouletteCheckLimitRule limitRule) {
		this.limitRule = limitRule;
	}

	public RouletteUser(UserPO userPO) {
		this.userPO = userPO;
	}

	public UserPO getUserPO() {
		return userPO;
	}

	public void setUserPO(UserPO userPO) {
		this.userPO = userPO;
	}

	public Integer getChipId() {
		return chipId;
	}

	public void setChipId(Integer chipId) {
		this.chipId = chipId;
	}

	public Integer getNotBetRoundNumber() {
		return notBetRoundNumber;
	}

	public void setNotBetRoundNumber(Integer notBetRoundNumber) {
		this.notBetRoundNumber = notBetRoundNumber;
	}
	
	
}
