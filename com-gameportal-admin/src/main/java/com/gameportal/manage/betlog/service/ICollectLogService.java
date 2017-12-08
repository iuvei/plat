package com.gameportal.manage.betlog.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.betlog.model.CollectLog;
import com.gameportal.manage.betlog.model.GameInfo;
import com.gameportal.manage.betlog.model.SysParam;



public abstract interface ICollectLogService {

	CollectLog save(CollectLog entity);
	
	boolean modify(CollectLog entity);

	boolean deleteById(Long id);

	List<CollectLog> queryForList(Map map, Integer startNo,
			Integer pageSize);
	
	 Long queryForCount(Map map);
	 
	 CollectLog findEntityById(Long id);
}
