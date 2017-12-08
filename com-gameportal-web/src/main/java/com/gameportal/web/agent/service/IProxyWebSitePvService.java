package com.gameportal.web.agent.service;

import java.util.Map;

import com.gameportal.web.agent.model.ProxyWebSitePv;

public abstract interface IProxyWebSitePvService {

	/**
	 * 新增域名访问记录
	 * @param entity
	 */
	void save(ProxyWebSitePv entity);
	
	/**
	 * 更新域名访问记录
	 * @param entity
	 */
	void update(ProxyWebSitePv entity);
	
	/**
	 * 条件统计数量
	 * @param map
	 * @return
	 */
	long count(Map<String, Object> map);
	
	/**
	 * 获取代理推广链接信息
	 * @param map
	 * @return
	 */
	ProxyWebSitePv getProxyWebSitePv(Map<String, Object> map);
}
