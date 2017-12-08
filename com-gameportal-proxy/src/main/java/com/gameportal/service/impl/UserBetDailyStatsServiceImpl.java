package com.gameportal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameportal.domain.UserBetDailyStats;
import com.gameportal.persistence.UserBetDailyStatsMapper;
import com.gameportal.service.BaseService;
import com.gameportal.service.IUserBetDailyStatsService;

@Service("proxyDownStatsService")
public class UserBetDailyStatsServiceImpl extends BaseService implements IUserBetDailyStatsService {

	@Autowired
	private UserBetDailyStatsMapper userBetDailyStatsMapper;
	@Override
	public void insertProxyDownStats(UserBetDailyStats stats) {
		userBetDailyStatsMapper.insertProxyDownStats(stats);
	}
	@Override
	public void insertBetDailyStatList(List<UserBetDailyStats> list) {
		userBetDailyStatsMapper.insertBetDailyStatList(list);
	}
}
