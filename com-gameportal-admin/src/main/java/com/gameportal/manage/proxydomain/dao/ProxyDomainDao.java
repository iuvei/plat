package com.gameportal.manage.proxydomain.dao;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.proxydomain.model.ProxyDomian;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

/**
 * 代理域名数据操作类
 * @author Administrator
 *
 */
@Component
public class ProxyDomainDao extends BaseIbatisDAO{
	@Override
	public Class<?> getEntityClass() {
		return ProxyDomian.class;
	}
	
	public ProxyDomian getObject(Map<String, Object> params){
		List<ProxyDomian> list = super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public List<ProxyDomian> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
	
	public boolean saveOrUpdate(ProxyDomian entity) {
		if (entity.getUrlid() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true : false;
		else
			return update(entity);
	}
}
