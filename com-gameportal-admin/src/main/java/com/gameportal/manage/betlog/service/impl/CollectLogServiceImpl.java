package com.gameportal.manage.betlog.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.gameportal.manage.betlog.dao.CollectLogDao;
import com.gameportal.manage.betlog.dao.SysParamDao;
import com.gameportal.manage.betlog.model.CollectLog;
import com.gameportal.manage.betlog.model.SysParam;
import com.gameportal.manage.betlog.service.ICollectLogService;

public class CollectLogServiceImpl implements ICollectLogService {

	@Resource(name = "collectLogDao")
	private CollectLogDao collectLogDao = null;
	
	@Override
	public boolean deleteById(Long id) {
		return collectLogDao.delete(id);
	}

	@Override
	public CollectLog findEntityById(Long id) {
		return (CollectLog)collectLogDao.findById(id);
		
	}

	@Override
	public boolean modify(CollectLog entity) {
		return collectLogDao.saveOrUpdate(entity);
	}

	@Override
	public Long queryForCount(Map map) {
		return collectLogDao.getRecordCount(map);
	}

	@Override
	public List<CollectLog> queryForList(Map map, Integer startNo,Integer pageSize){
		List<CollectLog> list = collectLogDao.queryForPager(map,startNo, pageSize);
		return list;
	}

	@Override
	public CollectLog save(CollectLog entity) {
		CollectLog collectLog= (CollectLog) collectLogDao.save(entity);
		return StringUtils.isNotBlank(ObjectUtils.toString(collectLog.getPid())) ? collectLog : null;
	}

}
