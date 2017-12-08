package com.gameportal.manage.proxy.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.proxy.model.ProxyClearingLog;
import com.gameportal.manage.proxy.model.ProxyClearingLogTotal;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component("proxyClearingLogDao")
public class ProxyClearingLogDao extends BaseIbatisDAO{

	@Override
	public Class<ProxyClearingLog> getEntityClass() {
		return ProxyClearingLog.class;
	}
	
	public boolean save(ProxyClearingLog entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(super.save(entity))) ? true
				: false;
	}
	
	public ProxyClearingLog getObject(Map<String, Object> params){
		List<ProxyClearingLog> list = getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public List<ProxyClearingLog> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
	
	public int updateStatus(ProxyClearingLog entity){
		return getSqlMapClientTemplate().update(getSimpleName()+".updateStauts", entity);
	}
	
	/**
	 * 总计
	 * @param params
	 * @return
	 */
	public ProxyClearingLogTotal getSumClearMoney(Map<String, Object> params){
		return  (ProxyClearingLogTotal) getSqlMapClientTemplate().queryForObject(getSimpleName()+".proxyClearingLogSum", params);
	}
	
	
}
