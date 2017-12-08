package com.gameportal.manage.sitesettings.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.sitesettings.model.SiteSettings;
import com.gameportal.manage.sitesettings.model.WebBulletinEntity;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component("webBulletinDao")
public class WebBulletinDao extends BaseIbatisDAO{

	@Override
	public Class<WebBulletinEntity> getEntityClass() {
		return WebBulletinEntity.class;
	}
	
	public boolean save(WebBulletinEntity entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(super.save(entity))) ? true: false;
	}
	
	public boolean update(WebBulletinEntity entity){
		return super.update(entity);
	}
	
	public WebBulletinEntity getObject(Map<String, Object> params){
		List<WebBulletinEntity> list = getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public List<WebBulletinEntity> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
