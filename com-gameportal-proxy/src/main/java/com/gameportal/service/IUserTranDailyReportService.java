package com.gameportal.service;

import java.util.List;

import com.gameportal.domain.UserTranDailyReport;

public interface IUserTranDailyReportService {
	
	public void insertTransDailyStat(UserTranDailyReport stats);
	
	public void insertTransDailyStatList(List<UserTranDailyReport> list);
}
