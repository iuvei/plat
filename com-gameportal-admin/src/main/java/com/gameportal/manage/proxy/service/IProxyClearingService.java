package com.gameportal.manage.proxy.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.proxy.model.ProxyClearingLog;
import com.gameportal.manage.proxy.model.ProxyClearingLogTotal;

/**
 * 
 * @author Administrator
 *
 */
public interface IProxyClearingService {

	public Long count(Map<String, Object> params);
	public List<ProxyClearingLog> getList(Map<String, Object> params,int thisPage,int pageSize);
	
	public ProxyClearingLog getObject(Map<String, Object> params);
	
	public boolean save(ProxyClearingLog entity);
	
	public boolean update(ProxyClearingLog entity);
	
	public boolean delete(Long clearingid);
	
	/**
	 * 结算
	 * @param params 结算参数
	 * @return
	 */
	public String clearing(Map<String, Object> params) throws Exception;
	
	/**
	 * 新代理结算(2015-12-11)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String clearing2(Map<String, Object> params) throws Exception;
	
	/**
	 * 入账
	 * @param entity 结算记录对象
	 * @param params 参数
	 * @return
	 */
	public String recorded(ProxyClearingLog entity,Map<String, Object> params) throws Exception;
	
	public String xima(Map<String, Object> params) throws Exception;
	
	/**
	 * 总计结算金额
	 * @param params 参数
	 * @return
	 */
	public ProxyClearingLogTotal clearMoneyTotal(Map<String, Object> params);
}
