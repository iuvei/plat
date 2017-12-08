package com.gameportal.manage.reportform.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.reportform.dao.PlatMoneyLogDao;
import com.gameportal.manage.reportform.model.PlatMoneyLog;
import com.gameportal.manage.reportform.service.PlatMoneyLogService;

@Service("platMoneyLogService")
public class PlatMoneyLogServiceImpl implements PlatMoneyLogService {

	@Resource(name = "platMoneyLogDao")
	private PlatMoneyLogDao platMoneyLogDao = null;
	
	@Override
	public boolean save(PlatMoneyLog entity) {
		return platMoneyLogDao.save(entity);
	}
	
	@Override
	public List<PlatMoneyLog> querPlatMoneyLog(Map<String, Object> map, Integer startNo,
			Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		map.put("limit", true);
		map.put("thisPage", startNo);
		map.put("pageSize", pageSize);
		return platMoneyLogDao.selectPlatMoneyLog(map);
	}
	
	@Override
	public Long queryPlatMoneyLogCount(Map<String, Object> map) {
		return platMoneyLogDao.getRecordCount(map);
	}
}
