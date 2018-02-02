package com.na.manager.bean;

import java.util.Date;

/**
 * 包房会员输赢报表
 * 
 * @author andy
 * @date 2017年6月23日 上午9:51:46
 * 
 */
public class RoomMemberWinLostReportRequest extends PageCondition{
	// 开始时间
	private Date startTime;
	// 结束时间
	private Date endTime;
	// 用户ID
	private Long userId;
	//会员账号
	private String loginName;

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

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
