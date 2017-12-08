package com.gameportal.manage.proxy.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.proxy.model.ProxyUserXimaLog;
import com.gameportal.manage.system.dao.BaseIbatisDAO;
@Component("proxyUserXimaDao")
public class ProxyUserXimaDao extends BaseIbatisDAO{

	@Override
	public Class<ProxyUserXimaLog> getEntityClass() {
		return ProxyUserXimaLog.class;
	}
	
	public boolean save(ProxyUserXimaLog entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(super.save(entity))) ? true
				: false;
	}
	
	public ProxyUserXimaLog getObject(Map<String, Object> params){
		List<ProxyUserXimaLog> list = getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	public List<ProxyUserXimaLog> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
}
