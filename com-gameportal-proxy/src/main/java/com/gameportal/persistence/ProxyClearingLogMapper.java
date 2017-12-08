package com.gameportal.persistence;

import java.util.List;
import java.util.Map;

import com.gameportal.domain.BetLogTotal;
import com.gameportal.domain.Page;
import com.gameportal.domain.ProxyClearingLog;
import com.gameportal.domain.ProxyUserXimaLog;

/**
 * 代理结算记录
 * @author leron
 *
 */
public interface ProxyClearingLogMapper {
		
	/**
	 * 代理结算记录
	 * @param page
	 * @return
	 */
	public List<ProxyClearingLog> findlistPage(Page page);
	
	/**
	 * 代理结算记录总计
	 * @param params
	 * @return
	 */
	public ProxyClearingLog proxyClearingLogTotal(Map<String, Object> params);
	
	/**
	 * 查询代理结算数据
	 * @param params
	 * @return
	 */
	public ProxyClearingLog findByMap(Map<String, Object> params);
	
	/**
	 * 查询下线洗码
	 * @param params
	 * @return
	 */
	public List<BetLogTotal> selectProxyDownUserXima(Map<String, Object> params);
	
	/**
	 * 新增下线洗码记录
	 * @param proxyUserXimaLog
	 * @return
	 */
	public Long insertProxyUserXimaLog(ProxyUserXimaLog proxyUserXimaLog);
	
	/**
	 * 新增代理结算记录
	 * @param clearingLog
	 * @return
	 */
	public int insertProxyClearingLog(ProxyClearingLog clearingLog);
	
	/**
	 * 修改代理结算记录
	 * @param clearingLog
	 * @return
	 */
	public int updateProxyClearingLog(ProxyClearingLog clearingLog);
	
}
