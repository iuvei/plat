package com.gameportal.manage.system.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.system.dao.SystemUserLoginLogDao;
import com.gameportal.manage.system.model.SystemUserLoginLog;
import com.gameportal.manage.system.service.ISystemUserLoginLogService;

/**
 * 
 * @author Administrator
 *
 */
@Service("systemUserLoginLogService")
public class SystemUserLoginLogServiceImpl implements ISystemUserLoginLogService{

	@Resource(name="systemUserLoginLogDao")
	private SystemUserLoginLogDao systemUserLoginLogDao;
	@Override
	public boolean insert(SystemUserLoginLog entity) {
		return StringUtils.isNotBlank(ObjectUtils.toString(systemUserLoginLogDao.save(entity))) ? true
				: false;
	}

	@Override
	public Long count(Map<String, Object> params) {
		return systemUserLoginLogDao.getRecordCount(params);
	}

	@Override
	public List<SystemUserLoginLog> getList(Map<String, Object> params,
			int thisPage, int pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(thisPage))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			thisPage = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return systemUserLoginLogDao.getList(params);
	}

}
