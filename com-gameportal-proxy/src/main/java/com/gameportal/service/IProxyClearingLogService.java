package com.gameportal.service;

import java.util.List;
import java.util.Map;

import com.gameportal.domain.Page;
import com.gameportal.domain.ProxyClearingLog;

/**
 * 代理结算记录
 * @author leron
 *
 */
public interface IProxyClearingLogService {

	/**
	 * 查询代理结算记录
	 * @param page
	 * @return
	 */
	public List<ProxyClearingLog> queryProxyClearingLog(Page page);
	
	/**
	 * 自助洗码
	 * @param params
	 * @return
	 */
	public String saveXima(Map<String, Object> params) throws Exception;
	
	/**
	 * 代理结算记录总计
	 * @param params
	 * @return
	 */
	public ProxyClearingLog queryProxyClearingLogTotal(Map<String, Object> params);
	
}
