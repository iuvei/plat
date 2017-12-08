package com.gameportal.manage.xima.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.xima.model.ProxyXimaSet;
import com.gameportal.manage.xima.dao.ProxyXimaSetDao;
import com.gameportal.manage.xima.service.IProxyXimaSetService;

@Service("proxyXimaSetServiceImpl")
public class ProxyXimaSetServiceImpl implements IProxyXimaSetService {
	
	private static final Logger logger = Logger
			.getLogger(ProxyXimaSetServiceImpl.class);
	
	@Resource
	private ProxyXimaSetDao proxyXimaSetDao;
	
	public ProxyXimaSetServiceImpl() {
		super();
	}
	
	@Override
	public boolean saveOrUpdateProxyXimaSet(ProxyXimaSet card) {
		return proxyXimaSetDao.saveOrUpdate(card);
	}

	@Override
	public boolean deleteProxyXimaSet(Long id) {
		return proxyXimaSetDao.delete(id);
	}

	@Override
	public Long queryProxyXimaSetCount(Map<String, Object> params) {
		return proxyXimaSetDao.getRecordCount(params);
	}

	@Override
	public List<ProxyXimaSet> queryProxyXimaSet(Map<String, Object> params, Integer startNo,
			Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return proxyXimaSetDao.getList(params);
	}
}
