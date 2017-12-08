package com.gameportal.persistence;

import java.util.List;
import java.util.Map;

import com.gameportal.domain.ProxyReportEntity;

public interface ProxyReportEntityMapper {
	/**
	 * 获取代理详细信息
	 * 
	 * @param params
	 * @return
	 */
	List<ProxyReportEntity> getUserMsg(Map<String, Object> params);

	/**
	 * 获取代理报表信息
	 * 
	 * @param params
	 * @return
	 */
	ProxyReportEntity getProxySpreadInfo(Map<String, Object> params);

	/**
	 * 获取代理报表投注信息
	 * 
	 * @param params
	 * @return
	 */
	ProxyReportEntity getProxyBetInfo(Map<String, Object> params);
}
