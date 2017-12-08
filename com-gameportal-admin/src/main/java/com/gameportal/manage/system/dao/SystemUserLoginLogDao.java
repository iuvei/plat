package com.gameportal.manage.system.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.manage.system.model.SystemUserLoginLog;

/**
 * 
 * @author Administrator
 *
 */
@Component("systemUserLoginLogDao")
public class SystemUserLoginLogDao extends BaseIbatisDAO{

	@Override
	public Class<SystemUserLoginLog> getEntityClass() {
		return SystemUserLoginLog.class;
	}
	
	public List<SystemUserLoginLog> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
