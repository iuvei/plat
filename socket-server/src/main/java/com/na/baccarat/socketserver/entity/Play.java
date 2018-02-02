package com.na.baccarat.socketserver.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 玩法
 * 
 * @author alan
 * @date 2017年5月1日 下午12:04:46
 */
public class Play implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 游戏ID
	 */
	private Integer gameId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 交易项列表
	 */
	private List<TradeItem> tradeList;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<TradeItem> getTradeList() {
		return tradeList;
	}
	public void setTradeList(List<TradeItem> tradeList) {
		this.tradeList = tradeList;
	}
	
	
	
}