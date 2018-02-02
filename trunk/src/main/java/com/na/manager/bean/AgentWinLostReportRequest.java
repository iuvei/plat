package com.na.manager.bean;

/**
 * 代理输赢报表
 * 
 * @author andy
 * @date 2017年6月23日 上午9:51:46
 * 
 */
public class AgentWinLostReportRequest {
	// 开始时间
	private String startTime;
	// 结束时间
	private String endTime;
	// 统计类型 1 代理团队 2 下级团队, 3 会员汇总 ,4 会员
	private Integer type;
	// 用户ID
	private Long userId;
	// 路径
	private String userPath;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserPath() {
		return userPath;
	}

	public void setUserPath(String userPath) {
		this.userPath = userPath;
	}
}
