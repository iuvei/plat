package com.gameportal.web.user.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.web.user.dao.UserPropertyDao;
import com.gameportal.web.user.model.PayOrderLog;
import com.gameportal.web.user.model.UserGrade;
import com.gameportal.web.user.model.WithdrawalcountLog;
import com.gameportal.web.user.service.IUserPropertyService;

@Service("userPropertyService")
public class UserPropertyServiceImpl implements IUserPropertyService{
	
	@Resource(name = "userPropertyDao")
	private UserPropertyDao userPropertyDao;

	@Override
	public boolean insertWithdrawalcountLog(WithdrawalcountLog entity) {
		return userPropertyDao.insertWithdrawalcountLog(entity);
	}

	@Override
	public int updateWithdrawalcountLog(WithdrawalcountLog entity) {
		return userPropertyDao.updateWithdrawalcountLog(entity);
	}

	@Override
	public List<UserGrade> getUserGradeList(Map<String, Object> params) {
		return userPropertyDao.getUserGradeList(params);
	}

	@Override
	public List<WithdrawalcountLog> getWithdrawalcountLogList(
			Map<String, Object> params) {
		return userPropertyDao.getWithdrawalcountLogList(params);
	}

	@Override
	public UserGrade getUserGrade(Map<String, Object> params) {
		return userPropertyDao.getUserGrade(params);
	}

	@Override
	public WithdrawalcountLog getWithdrawalcountLog(Map<String, Object> params) {
		return userPropertyDao.getWithdrawalcountLog(params);
	}

	@Override
	public Long getPayOrder(Map<String, Object> params) {
		return userPropertyDao.getPayOrder(params);
	}

	@Override
	public void insertPayLog(PayOrderLog log) {
		userPropertyDao.insertPayLog(log);
	}

	@Override
	public Long sumPayOrderAmount(Map<String, Object> params) {
		return userPropertyDao.sumPayOrderAmount(params);
	}
}
