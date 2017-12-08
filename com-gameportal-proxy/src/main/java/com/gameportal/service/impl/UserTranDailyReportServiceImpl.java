package com.gameportal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameportal.domain.UserTranDailyReport;
import com.gameportal.persistence.UserTranDailyReportMapper;
import com.gameportal.service.BaseService;
import com.gameportal.service.IUserTranDailyReportService;

@Service("proxyDownReportService")
public class UserTranDailyReportServiceImpl extends BaseService implements IUserTranDailyReportService {

	@Autowired
	private UserTranDailyReportMapper userTranDailyReportMapper;

	@Override
	public void insertTransDailyStat(UserTranDailyReport stats) {
		userTranDailyReportMapper.insertProxyDownReport(stats);
	}

	@Override
	public void insertTransDailyStatList(List<UserTranDailyReport> list) {
		userTranDailyReportMapper.insertTransDailyStatList(list);
	}
}
