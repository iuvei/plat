package com.na.betRobot.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author
 * @time 2015-07-21 18:13:40
 */
public class UserChips implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 筹码ID
	 */
	@JSONField(name = "cid")
	private Integer id;
	/**
	 * 最大限制 100
	 */
	private Double max;
	/**
	 * 最小限制 5
	 */
	private Double min;
	/**
	 * 注码形式 5,10,20,50,100
	 */
	private String jtton;
	
	@JSONField(name = "gid")
	private Integer gameId;
	
	private int isVIP;
	
	private String mtype;
	
	
	

	public String getMtype() {
		return mtype;
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public String getJtton() {
		return jtton;
	}

	public void setJtton(String jtton) {
		this.jtton = jtton;
	}

	public int getIsVIP() {
		return isVIP;
	}

	public void setIsVIP(int isVIP) {
		this.isVIP = isVIP;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

}