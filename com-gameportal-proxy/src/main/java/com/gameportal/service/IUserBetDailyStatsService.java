package com.gameportal.service;

import java.util.List;

import com.gameportal.domain.UserBetDailyStats;

public interface IUserBetDailyStatsService {

	public void insertProxyDownStats(UserBetDailyStats stats);
	
	public void insertBetDailyStatList(List<UserBetDailyStats> list);
}
