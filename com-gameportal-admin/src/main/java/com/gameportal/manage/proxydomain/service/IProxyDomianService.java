package com.gameportal.manage.proxydomain.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.proxydomain.model.ProxyDomian;

public interface IProxyDomianService {

	public List<ProxyDomian> getList(Map<String, Object> params,int thisPage,int pageSize);
	public Long getCount(Map<String, Object> params);
	
	/**
	 * 查询域名详情
	 * @param params
	 * @return
	 */
	public ProxyDomian query(Map<String, Object> params);
	
	/**
	 * 保存更新
	 * @param entity
	 * @return
	 */
	public boolean saveOrUpdate(ProxyDomian entity);
	
	public boolean delete(Long id);
}
