package com.gameportal.manage.sitesettings.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.sitesettings.model.WebBulletinEntity;

public interface IWebBulletinService {

	public boolean save(WebBulletinEntity entity);
	public boolean update(WebBulletinEntity entity);
	public WebBulletinEntity getObject(Map<String, Object> params);
	public List<WebBulletinEntity> getList(Map<String, Object> params,int thisPage,int pageSize);
	public Long getCount(Map<String, Object> params);
	public boolean delete(Long bid);
}
