package com.na.manager.entity;

import java.io.Serializable;

/**
 * 交易项表
 * 
 * @author java
 * @time 2015-07-21 18:05:58
 */
public class TradeItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	/**
	 * 游戏ID
	 */
	private Integer gameId;

	/**
	 * 玩法ID
	 */
	private Integer playId;
	/**
	 * 比例名称，用简写 目前百家乐只配置这几项，其他游戏在配置简写：
	 * 
	 * B,P,DB,DP,T(Banker,player,tied,DBander,DPlayer)
	 */
	private String key;

	/**
	 * 交易项名称
	 */
	private String name;
	/**
	 * 下注比例计算
	 */
	private Double rate;

	/**
	 * 状态 0： 暂停使用 1：正常
	 */
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getKey() {
		return key;
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

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}