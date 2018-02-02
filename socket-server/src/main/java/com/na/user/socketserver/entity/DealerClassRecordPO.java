package com.na.user.socketserver.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 荷官交接班记录
 */
public class DealerClassRecordPO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Long userId;
	private String loginName;
	private Date startTime;
	private Date endTime;
	private Integer gameTableId;
	private String gameTableName;
	private Integer gameId;
	private String gameName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public Integer getGameTableId() {
		return gameTableId;
	}
	public void setGameTableId(Integer gameTableId) {
		this.gameTableId = gameTableId;
	}
	public String getGameTableName() {
		return gameTableName;
	}
	public void setGameTableName(String gameTableName) {
		this.gameTableName = gameTableName;
	}
	public Integer getGameId() {
		return gameId;
	}
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	
}
