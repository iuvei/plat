package com.gameportal.manage.system.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.system.model.SystemUserLoginLog;

/**
 * 
 * @author Administrator
 *
 */
public interface ISystemUserLoginLogService {

	public boolean insert(SystemUserLoginLog entity);
	public Long count(Map<String, Object> params);
	public List<SystemUserLoginLog> getList(Map<String, Object> params,int thisPage,int pageSize);
}
