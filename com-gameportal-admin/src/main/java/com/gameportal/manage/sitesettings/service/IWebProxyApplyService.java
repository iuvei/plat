package com.gameportal.manage.sitesettings.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.sitesettings.model.WebProxyApply;

public interface IWebProxyApplyService {

	public boolean save(WebProxyApply entity);
	public boolean update(WebProxyApply entity);
	public WebProxyApply getObject(Map<String, Object> params);
	public List<WebProxyApply> getList(Map<String, Object> params,int thisPage,int pageSize);
	public Long getCount(Map<String, Object> params);
	public boolean delete(Long aid);
}
