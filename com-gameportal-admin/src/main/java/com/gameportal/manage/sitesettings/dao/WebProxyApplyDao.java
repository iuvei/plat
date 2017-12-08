package com.gameportal.manage.sitesettings.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.proxy.model.ProxySet;
import com.gameportal.manage.sitesettings.model.WebProxyApply;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component("webProxyApplyDao")
public class WebProxyApplyDao extends BaseIbatisDAO{

	@Override
	public Class<WebProxyApply> getEntityClass() {
		return WebProxyApply.class;
	}

	public boolean save(WebProxyApply entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(super.save(entity))) ? true: false;
	}
	
	public boolean update(WebProxyApply entity){
		return super.update(entity);
	}
	
	public WebProxyApply getObject(Map<String, Object> params){
		List<WebProxyApply> list = getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	public List<WebProxyApply> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
}
