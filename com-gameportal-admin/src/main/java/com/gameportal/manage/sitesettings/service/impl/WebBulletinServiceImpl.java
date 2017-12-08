package com.gameportal.manage.sitesettings.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.sitesettings.dao.WebBulletinDao;
import com.gameportal.manage.sitesettings.model.WebBulletinEntity;
import com.gameportal.manage.sitesettings.service.IWebBulletinService;

@Service("webBulletinService")
public class WebBulletinServiceImpl implements IWebBulletinService{
	
	public static final Logger logger = Logger.getLogger(WebBulletinServiceImpl.class);
	
	@Resource(name = "webBulletinDao")
	private WebBulletinDao webBulletinDao;

	@Override
	public boolean save(WebBulletinEntity entity) {
		// TODO Auto-generated method stub
		return webBulletinDao.save(entity);
	}

	@Override
	public boolean update(WebBulletinEntity entity) {
		// TODO Auto-generated method stub
		return webBulletinDao.update(entity);
	}

	@Override
	public WebBulletinEntity getObject(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return webBulletinDao.getObject(params);
	}

	@Override
	public List<WebBulletinEntity> getList(Map<String, Object> params,
			int thisPage, int pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(thisPage))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			thisPage = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return webBulletinDao.getList(params);
	}

	@Override
	public Long getCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return webBulletinDao.getRecordCount(params);
	}

	@Override
	public boolean delete(Long bid) {
		// TODO Auto-generated method stub
		return webBulletinDao.delete(bid);
	}

}
