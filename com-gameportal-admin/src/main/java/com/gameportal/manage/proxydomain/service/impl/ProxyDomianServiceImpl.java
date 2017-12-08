package com.gameportal.manage.proxydomain.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.proxydomain.dao.ProxyDomainDao;
import com.gameportal.manage.proxydomain.model.ProxyDomian;
import com.gameportal.manage.proxydomain.service.IProxyDomianService;

@Service("proxyDomianService")
public class ProxyDomianServiceImpl implements IProxyDomianService{
	
	@Resource(name = "proxyDomainDao")
	private ProxyDomainDao proxyDomainDao;

	@Override
	public List<ProxyDomian> getList(Map<String, Object> params, int thisPage,
			int pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(thisPage))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			thisPage = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return proxyDomainDao.getList(params);
	}

	@Override
	public Long getCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return proxyDomainDao.getRecordCount(params);
	}

	@Override
	public ProxyDomian query(Map<String, Object> params) {
		return proxyDomainDao.getObject(params);
	}

	@Override
	public boolean saveOrUpdate(ProxyDomian entity) {
		return proxyDomainDao.saveOrUpdate(entity);
	}

	@Override
	public boolean delete(Long id) {
		return proxyDomainDao.delete(id);
	}

}
