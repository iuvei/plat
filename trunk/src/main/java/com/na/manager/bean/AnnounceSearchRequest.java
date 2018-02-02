package com.na.manager.bean;

/**
 * @author andy
 * @date 2017年6月26日 下午6:13:56
 * 
 */
public class AnnounceSearchRequest extends PageCondition {
	private String startDate;
	private String endDate;
	private String userName;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
