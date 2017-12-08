package com.gameportal.manage.sitesettings.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.sitesettings.model.WebAdEntity;

public interface IWebAdService {

	public boolean save(WebAdEntity entity);
	public boolean update(WebAdEntity entity);
	public WebAdEntity getObject(Map<String, Object> params);
	public List<WebAdEntity> getList(Map<String, Object> params,int thisPage,int pageSize);
	public Long getCount(Map<String, Object> params);
	public boolean delete(Long adid);
}
