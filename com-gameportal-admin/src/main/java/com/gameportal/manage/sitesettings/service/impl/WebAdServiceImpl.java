package com.gameportal.manage.sitesettings.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.sitesettings.dao.WebAdDao;
import com.gameportal.manage.sitesettings.model.WebAdEntity;
import com.gameportal.manage.sitesettings.service.IWebAdService;

/**
 * 
 * @author Administrator
 *
 */
@Service("webAdService")
public class WebAdServiceImpl implements IWebAdService{
	
	public static final Logger logger = Logger.getLogger(WebAdServiceImpl.class);

	@Resource(name = "webAdDao")
	private WebAdDao webAdDao;

	@Override
	public boolean save(WebAdEntity entity) {
		// TODO Auto-generated method stub
		return webAdDao.save(entity);
	}

	@Override
	public boolean update(WebAdEntity entity) {
		// TODO Auto-generated method stub
		return webAdDao.update(entity);
	}

	@Override
	public WebAdEntity getObject(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return webAdDao.getObject(params);
	}

	@Override
	public List<WebAdEntity> getList(Map<String, Object> params, int thisPage,
			int pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(thisPage))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			thisPage = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return webAdDao.getList(params);
	}

	@Override
	public Long getCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return webAdDao.getRecordCount(params);
	}

	@Override
	public boolean delete(Long adid) {
		// TODO Auto-generated method stub
		return webAdDao.delete(adid);
	}
}
