package com.na.baccarat.socketserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.na.AutoGame;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.config.BaccaratConfig;
import com.na.baccarat.socketserver.entity.BeingMiGameTable;
import com.na.baccarat.socketserver.entity.Game;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.Play;
import com.na.baccarat.socketserver.entity.Round;
import com.na.baccarat.socketserver.entity.RoundExt;
import com.na.baccarat.socketserver.entity.VirtualGameTable;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.common.enums.VirtualGameTableType;
import com.na.user.socketserver.entity.GamePO;
import com.na.user.socketserver.entity.GameTablePO;
import com.na.user.socketserver.entity.RoundExtPO;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.entity.VirtualGameTablePO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IGameTableService;
import com.na.user.socketserver.service.IRoundExtService;
import com.na.user.socketserver.service.IRoundService;
import com.na.user.socketserver.service.IVirtualGameTableService;
import com.na.user.socketserver.util.PropertyUtil;

/**
 * 初始化 游戏自由数据
 */
@Component
public class BaccaratAutoGame implements AutoGame {
	
	private final static Logger log = LoggerFactory.getLogger(BaccaratAutoGame.class);
	
	@Autowired
	private IRoundService roundService;
	
	@Autowired
	private IRoundExtService roundExtService;
	
	@Autowired
	private IGameTableService gameTableService;
	
	@Autowired
	private IVirtualGameTableService virtualGameTableService;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public void init() {
		log.debug("百家乐开始初始化");
		// 初始化游戏配置
		BaccaratConfig.config = PropertyUtil.loadProps("config/game/baccarat.properties");
		
		// 初始化游戏桌信息
		initTableInfo();
		
		log.debug("百家乐初始化完毕");
	}
	
	/**
	 * 初始化 台桌 基础配置数据
	 */
	void initTableInfo() {
		//初始化游戏桌内存
		if(BaccaratCache.getGameTableMap() == null) {
			BaccaratCache.setGameTableMap(new HashMap<Integer, GameTable>());
		}
		//初始化多台用户
		BaccaratCache.setMultipleUserMap(new ConcurrentHashMap<>());
		
		//获取游戏
		GamePO game = AppCache.getGame(GameEnum.BACCARAT);
		if(game == null) {
			log.error("初始化错误   未找到百家乐游戏");
			throw new RuntimeException("初始化错误   未找到百家乐游戏");
		}
		BaccaratCache.setGame(new Game(game));
		
		//初始化实体桌
		Map<Integer, GameTable> gameTableMap = initTablesByGame(game);
		BaccaratCache.setGameTableMap(gameTableMap);
		
		//初始化虚拟桌
		initVirtualTable(game, gameTableMap);
		
		if(BaccaratCache.getGameTableMap() != null) {
			BaccaratCache.getGameTableMap().forEach( (key,value) -> {
				Round round = new Round();
				RoundPO roundpo = roundService.initLastRound(value.getGameTablePO());
				round.setRoundPO(roundpo);
				if(roundpo.getId() != null) {
					RoundExt roundExt = new RoundExt();
					RoundExtPO roundExtPO = roundExtService.getRoundExt(roundpo.getId());
					if (roundExtPO == null) {
						roundExtPO = new RoundExtPO();
						roundExtPO.setRoundId(roundpo.getId());
					}
					roundExt.setRoundExtPO(roundExtPO);
					value.setRoundExt(roundExt);
				} else {
					throw SocketException.createError("百家乐桌子初始化失败: 缺少局ID");
				}
				
				value.setRound(round);
			});
		} else {
			log.warn("百家乐没有桌子");
		}
		
	}
	/**
	 * 根据游戏加载桌子
	 * @param game
	 * @return
	 */
	private HashMap<Integer, GameTable> initTablesByGame(GamePO game) {
		HashMap<Integer, GameTable> gameTableMap = new HashMap<>();
		List<GameTablePO> tableList = gameTableService.getTableByGame(game.getId(), 0);
		for(GameTablePO table : tableList) {
			GameTable gameTable = null;
			if(table.isBeingTable()) {
				gameTable = new BeingMiGameTable();
				gameTable.setGameTablePO(table);
			} else {
				gameTable = new GameTable();
				gameTable.setGameTablePO(table);
			}
			List<Play> playList = AppCache.getPlayList().stream().filter(item -> {
				return table.getGameId() == item.getGameId();
			}).collect(Collectors.toList());
			gameTable.setPlayList(playList);
			gameTableMap.put(table.getId(), gameTable);
		}
		return gameTableMap;
	}
	
	private void initVirtualTable(GamePO game, Map<Integer, GameTable> gameTableMap) {
		//初始化redis数据
		BaccaratCache.clearRedisData();
		
		//初始化虚拟桌
		Map<Integer, VirtualGameTable> virtualTableMap = new HashMap<>();
		Map<Integer,List<Integer>> table2VirtualMap = new HashMap<>();
		for(GameTable tableItem : gameTableMap.values()) {
			GameTablePO gameTablePO = tableItem.getGameTablePO();
			
			if(gameTablePO.isBeingTable()) {
				continue;
			}
			
			List<Integer> vTableIdList = virtualGameTableService.getVirtualTableIdList(gameTablePO.getGameId(), gameTablePO.getId());
			if(vTableIdList == null || vTableIdList.size() < 1) {
				log.warn("【初始化】 " + gameTablePO.getName() + "桌无对应虚拟桌");
			}
			table2VirtualMap.put(gameTablePO.getId(), vTableIdList);
			List<VirtualGameTablePO> vTableList = virtualGameTableService.getTableList(gameTablePO.getGameId(), gameTablePO.getId());
			for(VirtualGameTablePO item : vTableList) {
				VirtualGameTable vTable = new VirtualGameTable();
				vTable.setVirtualGameTablePO(item);
				virtualTableMap.put(item.getId(), vTable);
			}
		}
		BaccaratCache.setTableId2VirtualId(table2VirtualMap);
		BaccaratCache.setVirtualTableMap(virtualTableMap);
		
		Map<Integer,List<Integer>> virtualTypeMap = new HashMap<>();
		for(VirtualGameTableType typeEnum : VirtualGameTableType.values()) {
			virtualTypeMap.put(typeEnum.get(), virtualGameTableService.getVirtualTypeList(game.getId(), typeEnum));
		}
		BaccaratCache.setVirtualTableType(virtualTypeMap);
		
		//初始化虚拟桌座位信息
		Map<String,Long> virtualTableSeatMap = new HashMap<>();
		virtualTableMap.forEach( (k,v) -> {
			for(int i : BaccaratConfig.seatNums) {
				Map<String,Object> keyMap = new HashMap<>();
				keyMap.put("tableId", v.getVirtualGameTablePO().getGameTableId());
				keyMap.put("virtualTableId", k);
				keyMap.put("seatNumber", i);
				long score = v.getVirtualGameTablePO().getGameTableId() * 100000 + k * 10 + i;
				
				virtualTableSeatMap.put(JSONObject.toJSONString(keyMap), score * -1);
			}
		});
		BaccaratCache.setVirtualTableSeat(virtualTableSeatMap);
	}

}
