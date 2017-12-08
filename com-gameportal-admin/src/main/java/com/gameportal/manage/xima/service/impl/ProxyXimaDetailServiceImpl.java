package com.gameportal.manage.xima.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.xima.model.ProxyXimaDetail;
import com.gameportal.manage.xima.dao.ProxyXimaDetailDao;
import com.gameportal.manage.xima.service.IProxyXimaDetailService;

@Service("proxyXimaDetailServiceImpl")
public class ProxyXimaDetailServiceImpl implements IProxyXimaDetailService {
	
	private static final Logger logger = Logger
			.getLogger(ProxyXimaDetailServiceImpl.class);
	
	@Resource
	private ProxyXimaDetailDao proxyXimaDetailDao;
	
	public ProxyXimaDetailServiceImpl() {
		super();
	}
	
	@Override
	public boolean saveOrUpdateProxyXimaDetail(ProxyXimaDetail card) {
		return proxyXimaDetailDao.saveOrUpdate(card);
	}

	@Override
	public boolean deleteProxyXimaDetail(Long id) {
		return proxyXimaDetailDao.delete(id);
	}

	@Override
	public Long queryProxyXimaDetailCount(Map<String, Object> params) {
		return proxyXimaDetailDao.getRecordCount(params);
	}

	@Override
	public List<ProxyXimaDetail> queryProxyXimaDetail(Map<String, Object> params, Integer startNo,
			Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		return proxyXimaDetailDao.queryForPager(params, startNo, pageSize);
	}
}
