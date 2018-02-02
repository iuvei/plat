package com.na.manager.bean;

import java.util.Date;

/**
 * 包房对冲报表
 * 
 * @author andy
 * @date 2017年6月23日 上午9:51:46
 * 
 */
public class RoomReportRequest {
	// 开始时间
	private Date startTime;
	// 结束时间
	private Date endTime;
	// 用户ID
	private Long userId;
	// 代理账号
	private String agentName;
	// 房间号
	private String roomNumber;
	//统计类型
	private Integer statisType =1;
	
	private String roomAgent;

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Integer getStatisType() {
		return statisType;
	}

	public void setStatisType(Integer statisType) {
		this.statisType = statisType;
	}

	public String getRoomAgent() {
		return roomAgent;
	}

	public void setRoomAgent(String roomAgent) {
		this.roomAgent = roomAgent;
	}
}
