package com.gameportal.manage.proxy.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.proxy.model.ProxyInfo;
import com.gameportal.manage.proxy.model.ProxyReportEntity;

/**
 * @ClassName: IProxyInfoService
 * @Description: TODO(代理)
 * @author chenyun
 * @date 2015-5-10 下午4:39:05
 */
public  abstract interface IProxyInfoService {

	
public abstract ProxyInfo queryProxyInfoById(Long id);
	
	public abstract List<ProxyInfo> queryProxyInfo(Long id, Long  parentid,
			String name, Integer startNo, Integer pageSize);
	
	public abstract List<ProxyInfo> queryProxyInfo(Map<String, Object> params,Integer startNo, Integer pageSize);
	public abstract Long queryProxyInfoCount(Map<String, Object> params);
	public abstract List<ProxyInfo> queryProxyInfo(Long id,Long  parentid,
			String name, Integer status, Integer startNo, Integer pageSize);
	
	
	public abstract Long queryProxyInfoCount(Long id,Long  parentid,
			String name);
	
	public abstract Long queryProxyInfoCount(Long id,Long  parentid,
			String name, Integer status);
	
	public abstract Long queryProxyInfoCount(Long id,Long  parentid,
			String name, String domainname, String platformkey, 
			String ciphercode, String returnUrl, String noticeUrl, Integer status);
	
	public abstract ProxyInfo saveProxyInfo(ProxyInfo proxyInfo)
			throws Exception;

	public abstract boolean saveOrUpdateProxyInfo(ProxyInfo proxyInfo)
			throws Exception;

	public abstract boolean deleteProxyInfo(Long id) throws Exception;
	
	/**
	 * 获取代理报表
	 * @param params
	 * @return
	 */
	public ProxyReportEntity getProxyFrom(Map<String, Object> params);
	
	/**
	 * 获取代理结算
	 * @param params
	 * @return
	 */
	public ProxyReportEntity getProxyClearing(Map<String, Object> params);
	
}
