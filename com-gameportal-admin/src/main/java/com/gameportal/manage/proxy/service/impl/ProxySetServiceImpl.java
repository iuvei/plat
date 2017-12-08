package com.gameportal.manage.proxy.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.proxy.dao.ProxySetDao;
import com.gameportal.manage.proxy.model.ProxySet;
import com.gameportal.manage.proxy.service.IProxySetService;

@Service("proxySetService")
public class ProxySetServiceImpl implements IProxySetService{
	
	@Resource(name = "proxySetDao")
	private ProxySetDao proxySetDao;

	@Override
	public Long count(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return proxySetDao.getRecordCount(params);
	}

	@Override
	public List<ProxySet> getList(Map<String, Object> params, int thisPage,
			int pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(thisPage))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			thisPage = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return proxySetDao.getList(params);
	}

	@Override
	public boolean save(ProxySet entity) {
		return StringUtils.isNotBlank(ObjectUtils.toString(proxySetDao.save(entity))) ? true
				: false;
	}

	@Override
	public boolean update(ProxySet entity) {
		return proxySetDao.update(entity);
	}

	@Override
	public boolean delete(Long pid) {
		return proxySetDao.delete(pid);
	}

	@Override
	public ProxySet queryObject(Map<String, Object> params) {
		List<ProxySet> list = proxySetDao.queryForPager(params, 0, 1);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

}
