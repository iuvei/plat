package com.gameportal.manage.xima.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.xima.model.ProxyXimaSet;

public abstract interface IProxyXimaSetService {

	boolean saveOrUpdateProxyXimaSet(ProxyXimaSet proxyXimaSet);

	boolean deleteProxyXimaSet(Long id);

	Long queryProxyXimaSetCount(Map<String, Object> params);

	List<ProxyXimaSet> queryProxyXimaSet(Map<String, Object> params, Integer startNo, Integer pageSize);
	
}
