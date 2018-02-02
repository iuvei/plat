package com.na.baccarat.socketserver.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.na.baccarat.socketserver.common.enums.TradeItemEnum;
import com.na.roulette.socketserver.common.enums.RouletteTradeItemEnum;

/**
 * 交易项
 * 
 * @author alan
 * @date 2017年5月1日 下午12:04:46
 */
public class TradeItem implements Serializable{
	
	/**
	 * 交易项ID
	 */
	private Integer id;
	/**
	 * 系统中识别码
	 */
	private String key;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 游戏ID
	 */
	private Integer gameId;
	/**
	 * 玩法ID
	 */
	private Integer playId;
	/**
	 * 赔率
	 */
	private BigDecimal rate;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 状态：0 暂停使用 1 正常
	 */
	private Integer status;
	/**
	 * 交易项具体的含义
	 */
	private String addition;
	
	public String getAddition() {
		return addition;
	}
	public void setAddition(String addition) {
		this.addition = addition;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public TradeItemEnum getTradeItemEnum() {
		return TradeItemEnum.get(key);
	}
	public RouletteTradeItemEnum getRouletteTradeItemEnum() {
		return RouletteTradeItemEnum.get(key);
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getGameId() {
		return gameId;
	}
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
	public Integer getPlayId() {
		return playId;
	}
	public void setPlayId(Integer playId) {
		this.playId = playId;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}