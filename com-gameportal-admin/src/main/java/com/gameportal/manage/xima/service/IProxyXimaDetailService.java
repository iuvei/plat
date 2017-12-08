package com.gameportal.manage.xima.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.xima.model.ProxyXimaDetail;

public abstract interface IProxyXimaDetailService {

	boolean saveOrUpdateProxyXimaDetail(ProxyXimaDetail proxyXimaDetail);

	boolean deleteProxyXimaDetail(Long id);

	Long queryProxyXimaDetailCount(Map<String, Object> params);

	List<ProxyXimaDetail> queryProxyXimaDetail(Map<String, Object> params, Integer startNo, Integer pageSize);
	
}
