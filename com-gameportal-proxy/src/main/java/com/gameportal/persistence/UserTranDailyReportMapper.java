package com.gameportal.persistence;

import java.util.List;

import com.gameportal.domain.UserTranDailyReport;

public interface UserTranDailyReportMapper {
	
	public void insertProxyDownReport(UserTranDailyReport stats);
	
	public void insertTransDailyStatList(List<UserTranDailyReport> list);
}
