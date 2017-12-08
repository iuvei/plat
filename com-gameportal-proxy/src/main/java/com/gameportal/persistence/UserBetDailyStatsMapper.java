package com.gameportal.persistence;

import java.util.List;

import com.gameportal.domain.UserBetDailyStats;

public interface UserBetDailyStatsMapper {
	
	public void insertProxyDownStats(UserBetDailyStats stats);
	
	public void insertBetDailyStatList(List<UserBetDailyStats> list);

}
