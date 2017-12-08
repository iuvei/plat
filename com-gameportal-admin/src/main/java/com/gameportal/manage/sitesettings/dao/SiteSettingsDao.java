package com.gameportal.manage.sitesettings.dao;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.proxy.model.ProxyClearingLog;
import com.gameportal.manage.sitesettings.model.SiteSettings;
import com.gameportal.manage.system.dao.BaseIbatisDAO;


@Component
public class SiteSettingsDao extends BaseIbatisDAO{

	public Class<SiteSettings> getEntityClass() {
		return SiteSettings.class;
	}
	
	public boolean saveOrUpdate(SiteSettings entity) {
		if(entity.getSsid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}
	
	public List<SiteSettings> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
