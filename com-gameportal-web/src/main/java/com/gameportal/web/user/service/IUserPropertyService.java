package com.gameportal.web.user.service;

import java.util.List;
import java.util.Map;

import com.gameportal.web.user.model.PayOrderLog;
import com.gameportal.web.user.model.UserGrade;
import com.gameportal.web.user.model.WithdrawalcountLog;

public interface IUserPropertyService {

	public boolean insertWithdrawalcountLog(WithdrawalcountLog entity);
	
	public int updateWithdrawalcountLog(WithdrawalcountLog entity);
	
	public List<UserGrade> getUserGradeList(Map<String, Object> params);
	
	public List<WithdrawalcountLog> getWithdrawalcountLogList(Map<String, Object> params);
	
	public UserGrade getUserGrade(Map<String, Object> params);
	
	public WithdrawalcountLog getWithdrawalcountLog(Map<String, Object> params);
	
	public Long getPayOrder(Map<String, Object> params);
	
	public Long sumPayOrderAmount(Map<String, Object> params);
	
	/**
	 * 新增帐变日志
	 * @param log
	 */
	public void insertPayLog(PayOrderLog log);
	
}
