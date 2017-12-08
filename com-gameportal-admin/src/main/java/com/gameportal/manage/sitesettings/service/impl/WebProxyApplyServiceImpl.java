package com.gameportal.manage.sitesettings.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.sitesettings.dao.WebProxyApplyDao;
import com.gameportal.manage.sitesettings.model.WebProxyApply;
import com.gameportal.manage.sitesettings.service.IWebProxyApplyService;

@Service("webProxyApplyService")
public class WebProxyApplyServiceImpl implements IWebProxyApplyService{

public static final Logger logger = Logger.getLogger(WebProxyApplyServiceImpl.class);
	
	@Resource(name = "webProxyApplyDao")
	private WebProxyApplyDao webProxyApplyDao;
	
	@Override
	public boolean save(WebProxyApply entity) {
		// TODO Auto-generated method stub
		return webProxyApplyDao.save(entity);
	}

	@Override
	public boolean update(WebProxyApply entity) {
		// TODO Auto-generated method stub
		return webProxyApplyDao.update(entity);
	}

	@Override
	public WebProxyApply getObject(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return webProxyApplyDao.getObject(params);
	}

	@Override
	public List<WebProxyApply> getList(Map<String, Object> params,
			int thisPage, int pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(thisPage))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			thisPage = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return webProxyApplyDao.getList(params);
	}

	@Override
	public Long getCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return webProxyApplyDao.getRecordCount(params);
	}

	@Override
	public boolean delete(Long aid) {
		return webProxyApplyDao.delete(aid);
	}

}
