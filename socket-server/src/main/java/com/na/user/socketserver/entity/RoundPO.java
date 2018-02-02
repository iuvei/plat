package com.na.user.socketserver.entity;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;

/**
 * @author java
 * @time 2015-07-21 18:05:58
 */
public class RoundPO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Logger log = LoggerFactory.getLogger(RoundPO.class);
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
	private Date bootStartTime;
	/**
	 * 局开始时间
	 */
	private Date startTime;
	/**
	 * 局结束时间
	 */
	private Date endTime;
	/**
	 * 位数
	 *	1  	 	1庄 2闲 3和    点数输赢
	 *	2		0无  1庄例牌  2闲例牌  
	 *	3 		0无  1庄对  2闲对  对子
	 *	4 		庄点数
	 */
	private String result;
	/**
	 * 百家乐 状态 0没有开始 1新一靴 3新一局 4开始投注 5截止投注 6结算完成 7CLOSE。可扩展
	 */
	private Integer status;


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

	public Date getBootStartTime() {
		return bootStartTime;
	}

	public void setBootStartTime(Date bootStartTime) {
		this.bootStartTime = bootStartTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public RoundStatusEnum getStatusEnum(){
		return RoundStatusEnum.get(status);
	}
	
	public void setStatusEnum(RoundStatusEnum roundStatusEnum) {
		log.debug("【局】: " + this.getId() + ",状态: " + this.status +",修改为: " + roundStatusEnum);
		this.status = roundStatusEnum.get();
	}
}