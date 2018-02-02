package com.na.manager.bean;

import java.util.Date;

public class VirtualGameTableSearchRequest extends PageCondition{
	
	private Date startTime;
	
	private Date endTime;
	
	private Long userId;
	
	private String status;
	
	private String roomName;
	
	private String createUser;
	
	private String fullRoomName;
	
	private Integer gameTableId;
	//房间类型 1.虚拟房间 2.VIP对冲房 3 非对冲房
	private Integer type;
	
	private Integer vType;
	
	private Integer gameId;
	

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getGameTableId() {
		return gameTableId;
	}

	public void setGameTableId(Integer gameTableId) {
		this.gameTableId = gameTableId;
	}

	public String getFullRoomName() {
		return fullRoomName;
	}

	public void setFullRoomName(String fullRoomName) {
		this.fullRoomName = fullRoomName;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Integer getvType() {
		return vType;
	}

	public void setvType(Integer vType) {
		this.vType = vType;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
}
