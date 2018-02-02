package com.na.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.na.manager.bean.vo.GameTableAndGameNameVo;
import com.na.manager.common.redis.RedisClient;
import com.na.manager.common.redis.RedisEventData;
import com.na.manager.dao.IGameTableMapper;
import com.na.manager.dao.II18NMapper;
import com.na.manager.entity.GameTable;
import com.na.manager.service.IGameTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.NESTED,rollbackFor = Exception.class)
public class GameTableServiceImpl implements IGameTableService{

	@Autowired
	private IGameTableMapper gameTableMapper;
	@Autowired
	private II18NMapper i18NMapper;
	@Autowired
	private RedisClient redisClient;
	
	@Override
	public List<GameTable> listAllTable() {
		return gameTableMapper.findAllGameTable();
	}

	@Override
	public List<GameTable> listGameTables(Integer gid, String name,
			Integer status, Integer type, Integer page, Integer pagesize) {
		return gameTableMapper.listGameTables(gid,name,status,type,page*pagesize,pagesize);
		
	}

	@Override
	public Integer countGameTables(Integer gid, String name, Integer status,
			Integer type) {
		return gameTableMapper.countGameTables(gid,name,status,type);
	}

	@Override
	public Boolean saveGameTable(Integer gid, String name, Integer status,
			Integer type, Integer countDownSeconds, Integer isMany,
			Integer isBegingMi, Integer min, Integer max) {
		//String key = "gametable.name." + new Date().getTime() + Math.random()*9000+1000;
		int count = gameTableMapper.saveGameTable(gid,name,status,type,countDownSeconds,isMany,isBegingMi,min,max);

		if(count == 1) return true;
		return false;
	}

	@Override
	public Boolean updateTableStatus(Integer tid,Integer status) {
		int count = gameTableMapper.updateTableStatus(tid,status);
		if(count == 1){
			RedisEventData redisEventData = new RedisEventData();
			redisEventData.setRedisEventData(tid);
			redisEventData.setRedisEventType(RedisEventData.RedisEventType.UpdateGameTable.get());
			redisClient.publishGameServer(JSON.toJSONString(redisEventData));
			return true;
		} 
		return false;
	}

	@Override
	public Boolean updateGameTable(Integer id,Integer gameId, String name, Integer status,
			Integer type, Integer countDownSeconds, Integer isMany,
			Integer isBegingMi, Integer min, Integer max) {
		GameTable gameTable = new GameTable();
		gameTable.setId(id);
		gameTable.setCountDownSeconds(countDownSeconds);
		gameTable.setGameId(gameId);
		gameTable.setMiCountdownSeconds(isBegingMi);
		gameTable.setIsMany(isMany);
		gameTable.setMax(max);
		gameTable.setMin(min);
		gameTable.setName(name);
		gameTable.setStatus(status);
		gameTable.setType(type);
		int count = gameTableMapper.updateTable(gameTable);
		if(count == 1){
			RedisEventData redisEventData = new RedisEventData();
			redisEventData.setRedisEventData(gameTable.getId());
			redisEventData.setRedisEventType(RedisEventData.RedisEventType.UpdateGameTable.get());
			redisClient.publishGameServer(JSON.toJSONString(redisEventData));
			return true;
		} 
		return false;
	}

	@Override
	public List<Object> listAbnormalTables(Integer gameId, String name) {
		
		List<GameTableAndGameNameVo> list =  gameTableMapper.listAbnormalTables(gameId, name);
		List<Object> leafList = new ArrayList<>();
		Map<String, Object> leafMap;
		for (GameTableAndGameNameVo data : list) {
			leafMap = new HashMap<>();
			leafMap.put("data", data);
			leafMap.put("leaf", false);
			leafList.add(leafMap);
		}
		return leafList;
	}

	

}
