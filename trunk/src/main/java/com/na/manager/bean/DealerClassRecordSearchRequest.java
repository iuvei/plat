package com.na.manager.bean;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/26 0026.
 */
public class DealerClassRecordSearchRequest extends PageCondition {
	private String loginName;

	private String startTime;

	private String endTime;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

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
}
