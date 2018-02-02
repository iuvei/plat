package com.na.baccarat.socketserver.entity;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;
import com.na.baccarat.socketserver.common.limitrule.CheckLimitRule;
import com.na.user.socketserver.entity.UserPO;

/**
 * @author
 * @time 2015-07-21 18:13:40
 */
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private UserPO userPO;
	
	/**
	 * 用户当前加入类型
	 */
	private BetOrderSourceEnum source;
	
	// 用户当前桌位ID
	private Integer seat;
	
	//当前用户所在虚拟桌ID
	private Integer virtualTableId;
	
	// 当前的限红ID
	private Integer chipId;
	
	//国籍代码
	private String countryCode;
	
	/**
	 * 统计未下注局数   用于不下注踢出用户  第三局警告  第五局强制踢出
	 */
	private Integer notBetRoundNumber = null;
	
	/**
	 * 限红计算
	 * key为tableId
	 */
	@JsonIgnore
	private Map<Integer,CheckLimitRule> limitRuleMap = new ConcurrentHashMap<>();
	
	public User() {
	}

	public Map<Integer, CheckLimitRule> getLimitRuleMap() {
		return limitRuleMap;
	}
	public void setLimitRuleMap(Map<Integer, CheckLimitRule> limitRuleMap) {
		this.limitRuleMap = limitRuleMap;
	}
	public CheckLimitRule getLimitRule(Integer tableId) {
		return limitRuleMap.get(tableId);
	}
	public void setLimitRule(Integer tableId, CheckLimitRule rule) {
		limitRuleMap.put(tableId, rule);
	}
	public void removeLimitRule(Integer tableId) {
		limitRuleMap.remove(tableId);
	}

	public User(UserPO userPO) {
		super();
		this.userPO = userPO;
	}

	public Integer getSeat() {
		return seat;
	}

	public void setSeat(Integer seat) {
		this.seat = seat;
	}

	public Integer getChipId() {
		return chipId;
	}

	public void setChipId(Integer chipId) {
		this.chipId = chipId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Integer getNotBetRoundNumber() {
		return notBetRoundNumber;
	}

	public void setNotBetRoundNumber(Integer notBetRoundNumber) {
		this.notBetRoundNumber = notBetRoundNumber;
	}

	public Integer getVirtualTableId() {
		return virtualTableId;
	}

	public void setVirtualTableId(Integer virtualTableId) {
		this.virtualTableId = virtualTableId;
	}

	public UserPO getUserPO() {
		return userPO;
	}

	public void setUserPO(UserPO userPO) {
		this.userPO = userPO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userPO == null) ? 0 : userPO.hashCode());
		return result;
	}
	
	@JsonIgnore
	public boolean isMultipleTableUser() {
		return BaccaratCache.getMultipleUserMap().containsKey(this.getUserPO().getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userPO == null) {
			if (other.userPO != null)
				return false;
		} else if (!userPO.equals(other.userPO))
			return false;
		return true;
	}

	public BetOrderSourceEnum getSource() {
		return source;
	}

	public void setSource(BetOrderSourceEnum source) {
		this.source = source;
	}
	
	
	
}