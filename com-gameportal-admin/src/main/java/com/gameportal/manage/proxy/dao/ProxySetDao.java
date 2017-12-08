package com.gameportal.manage.proxy.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.manage.proxy.model.ProxyInfo;
import com.gameportal.manage.proxy.model.ProxySet;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component("proxySetDao")
public class ProxySetDao extends BaseIbatisDAO{

	@Override
	public Class<ProxySet> getEntityClass() {
		return ProxySet.class;
	}
	
	public ProxySet getObject(Map<String, Object> params){
		List<ProxySet> list = getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	
	public List<ProxySet> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
}
