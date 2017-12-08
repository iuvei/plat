package com.gameportal.web.user.service;

import java.util.List;
import java.util.Map;

import com.gameportal.web.user.model.PayOrder;

public interface IReportQueryService {
	
	public List<PayOrder> queryPayOrder(Long uiid, String startTime, String endTime, int paytyple, int status, int pageNo, int pageSize);
	
	public List<PayOrder> queryPayOrder(Map<String, Object> params);
	
	public long queryPayOrderCount(Long uiid, String startTime, String endTime, int paytyple, int status);
	
	long sumPayOrder(Map<String, Object> params);
	
	long countPayOrder(Map<String, Object> params);
	
	/**
	 * 查找指定日期的玩家
	 * @param params
	 * @return
	 */
	public List<String> selectGameMemberByDate(Map<String, Object> params);
	
	/**
	 * 查找每周的玩家
	 * @param params
	 * @return
	 */
	public List<String> selectGameMemberByWeek(Map<String, Object> params);
}
