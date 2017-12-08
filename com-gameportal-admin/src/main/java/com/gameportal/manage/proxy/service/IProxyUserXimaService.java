package com.gameportal.manage.proxy.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.proxy.model.ProxyUserXimaLog;

public interface IProxyUserXimaService {

	
	public List<ProxyUserXimaLog> getList(Map<String, Object> params,int thisPage,int pageSize);
	public Long getCount(Map<String, Object> params);
	public boolean update(ProxyUserXimaLog entity);
	public boolean save(ProxyUserXimaLog entity);
	
	public ProxyUserXimaLog getObject(Map<String, Object> params);
	
	public String rz(ProxyUserXimaLog entity) throws Exception;
	
}
