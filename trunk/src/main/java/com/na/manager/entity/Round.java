package com.na.manager.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author java
 * @time 2015-07-21 18:05:58
 */
public class Round implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 局ID
	 */
	private Long id;
	/**
	 * 游戏ID
	 */
	private Integer gameId;
	/**
	 * 桌ID
	 */
	private Integer gameTableId;
	/**
	 * 靴ID 20150720-1
	 */
	private String bootId;
	/**
	 * 局数
	 */
	private Integer roundNumber;
	/**
	 * 靴数
	 */
	private Integer bootNumber;
	/**
	 * 靴开始时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date bootStarttime;
	/**
	 * 局开始时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	/**
	 * 局结束时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	/**
	 * 结果游戏结果 结果 0庄 1闲 2和 3庄庄对 4庄闲对 5和庄对 6和闲对 7闲庄对 8闲闲对 9庄庄对闲对 10和庄对闲对 11闲庄对闲对
	 */
	private String result;
	
	/**
	 * 百家乐 状态 0没有开始 1新一靴 3新一局 4开始投注 5截止投注 6结算完成 7CLOSE。可扩展
	 */
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getBootId() {
		return bootId;
	}

	public void setBootId(String bootId) {
		this.bootId = bootId;
	}

	public Integer getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}

	public Integer getBootNumber() {
		return bootNumber;
	}

	public void setBootNumber(Integer bootNumber) {
		this.bootNumber = bootNumber;
	}

	public Date getBootStarttime() {
		return bootStarttime;
	}

	public void setBootStarttime(Date bootStarttime) {
		this.bootStarttime = bootStarttime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}