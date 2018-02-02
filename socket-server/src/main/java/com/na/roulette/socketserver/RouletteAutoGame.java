package com.na.roulette.socketserver;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.na.baccarat.socketserver.entity.Play;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.na.AutoGame;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.config.RouletteConfig;
import com.na.roulette.socketserver.entity.RouletteGame;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.roulette.socketserver.entity.RouletteRound;
import com.na.roulette.socketserver.entity.RouletteRoundExt;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.entity.GamePO;
import com.na.user.socketserver.entity.GameTablePO;
import com.na.user.socketserver.entity.RoundExtPO;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.service.IGameTableService;
import com.na.user.socketserver.service.IRoundExtService;
import com.na.user.socketserver.service.IRoundService;
import com.na.user.socketserver.util.PropertyUtil;


/**
 * 初始化 游戏自由数据
 */
@Component
public class RouletteAutoGame implements AutoGame {
	
	private final static Logger log = LoggerFactory.getLogger(RouletteAutoGame.class);
	
	@Autowired
	private IRoundService roundService;
	
	@Autowired
	private IRoundExtService roundExtService;
	
	@Autowired
	private IGameTableService gameTableService;
	
	@Override
	public void init() {
		log.debug("轮盘开始初始化");
		
		// 初始化游戏配置
		RouletteConfig.config = PropertyUtil.loadProps("config/game/roulette.properties");
		
		// 初始化游戏桌信息
		initTableInfo();
		
		log.debug("轮盘初始化完毕");
	}

	/**
	 * 初始化 台桌 基础配置数据
	 */
	void initTableInfo() {
		if(RouletteCache.getGameTableMap() == null) {
			RouletteCache.setGameTableMap(new HashMap<Integer, RouletteGameTable>());
		}
		
		//获取游戏
		GamePO gamePO = AppCache.getGame(GameEnum.ROULETTE);
		if(gamePO == null) {
			log.error("初始化错误   未找到轮盘游戏");
			throw new RuntimeException("初始化错误   未找到轮盘游戏");
		}
		RouletteCache.setGame(new RouletteGame(gamePO));
		
		initGameTable(gamePO);
		
		if(RouletteCache.getGameTableMap() != null) {
			RouletteCache.getGameTableMap().forEach( (key,value) -> {
				RoundPO round = roundService.initLastRound(value.getGameTablePO());
				RouletteRound rouletteRound = new RouletteRound(round);
				if(round.getId() != null) {
					RouletteRoundExt roundExt = new RouletteRoundExt();
					RoundExtPO roundExtPO = roundExtService.getRoundExt(round.getId());
					if (roundExtPO == null) {
						roundExtPO = new RoundExtPO();
						roundExtPO.setRoundId(round.getId());
					}
					value.setRoundExt(roundExt);
				}
				value.setRound(rouletteRound);
				
			});
		} else {
			log.warn("轮盘没有桌子");
		}
	}
	
	private void initGameTable(GamePO gamePO) {
		List<GameTablePO> gameTableList = gameTableService.getTableByGame(gamePO.getId(), 0);
		HashMap<Integer, RouletteGameTable> tableMap = new HashMap<>();
		gameTableList.forEach( gameTablePO -> {
			RouletteGameTable rouletteGameTable = new RouletteGameTable(gameTablePO);

			List<Play> playList = AppCache.getPlayList().stream().filter(play -> {
				return gameTablePO.getGameId() == play.getGameId();
			}).collect(Collectors.toList());
			rouletteGameTable.setPlayList(playList);

			tableMap.put(gameTablePO.getId(), rouletteGameTable);
		});

		RouletteCache.setGameTableMap(tableMap);
	}

}
