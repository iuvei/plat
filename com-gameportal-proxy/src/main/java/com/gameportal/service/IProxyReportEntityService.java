package com.gameportal.service;

import java.util.Map;

import com.gameportal.domain.ProxyReportEntity;

public abstract interface IProxyReportEntityService {
	/**
	 * 获取代理详细信息
	 * 
	 * @param params
	 * @return
	 */
	ProxyReportEntity getProxyMsg(Map<String, Object> params);

	/**
	 * 获取代理报表
	 * 
	 * @param params
	 * @return
	 */
	ProxyReportEntity getProxyFrom(Map<String, Object> params);
}
