package com.gameportal.manage.betlog.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.betlog.model.GameInfo;
import com.gameportal.manage.betlog.model.SysParam;



public abstract interface ISysParamService {

	SysParam save(SysParam entity);
	
	boolean modify(SysParam entity);

	boolean deleteById(Long id);

	List<SysParam> queryForList(Map map, Integer startNo,
			Integer pageSize);
	
	 Long queryForCount(Map map);
	 
	 SysParam findEntityByKey(String key);
}
