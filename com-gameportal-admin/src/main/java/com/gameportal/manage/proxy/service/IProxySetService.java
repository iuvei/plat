package com.gameportal.manage.proxy.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.proxy.model.ProxySet;

public interface IProxySetService {

	/**
	 * 统计数据条数
	 * @param params
	 * @return
	 */
	public Long count(Map<String, Object> params);
	
	/**
	 * 查询数据列表并分页
	 * @param params
	 * @param thisPage
	 * @param pageSize
	 * @return
	 */
	public List<ProxySet> getList(Map<String, Object> params,int thisPage,int pageSize);
	
	/**
	 * 保存数据
	 * @param entity
	 * @return
	 */
	public boolean save(ProxySet entity);
	
	/**
	 * 更新数据
	 * @param entity
	 * @return
	 */
	public boolean update(ProxySet entity);
	
	/**
	 * 删除数据
	 * @param pid
	 * @return
	 */
	public boolean delete(Long pid);
	
	/**
	 * 查询对象
	 * @param params
	 * @return
	 */
	public ProxySet queryObject(Map<String, Object> params);
}
