package com.gameportal.manage.sitesettings.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.sitesettings.model.SiteSettings;
import com.gameportal.manage.sitesettings.model.WebAdEntity;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component("webAdDao")
public class WebAdDao extends BaseIbatisDAO{

	@Override
	public Class<WebAdEntity> getEntityClass() {
		return WebAdEntity.class;
	}

	public boolean save(WebAdEntity entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(super.save(entity))) ? true: false;
	}
	
	public boolean update(WebAdEntity entity){
		return super.update(entity);
	}
	
	public WebAdEntity getObject(Map<String, Object> params){
		List<WebAdEntity> list = getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public List<WebAdEntity> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
	
}
