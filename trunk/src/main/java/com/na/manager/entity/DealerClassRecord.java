package com.na.manager.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author Andy
 * @version 创建时间：2017年10月14日 下午3:35:04
 */
public class DealerClassRecord {

	private long id;

	private Long userId;

	private String loginName;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	private Long gameTableId;

	private String gameTableName;

	private Long gameId;

	private String gameName;
	
	private Long members;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
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

	public Long getGameTableId() {
		return gameTableId;
	}

	public void setGameTableId(Long gameTableId) {
		this.gameTableId = gameTableId;
	}

	public String getGameTableName() {
		return gameTableName;
	}

	public void setGameTableName(String gameTableName) {
		this.gameTableName = gameTableName;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Long getMembers() {
		return members;
	}

	public void setMembers(Long members) {
		this.members = members;
	}
}
