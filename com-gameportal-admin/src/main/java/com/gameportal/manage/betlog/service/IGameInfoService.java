package com.gameportal.manage.betlog.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.betlog.model.GameInfo;



public abstract interface IGameInfoService {

	GameInfo save(GameInfo entity);
	
	boolean modify(GameInfo entity);

	boolean deleteById(Long id);

	List<GameInfo> queryForList(Map map, Integer startNo,
			Integer pageSize);
	
	 Long queryForCount(Map map);
	 
	 GameInfo findEntityById(Long id);
}
