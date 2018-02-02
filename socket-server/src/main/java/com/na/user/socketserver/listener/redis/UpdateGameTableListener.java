package com.na.user.socketserver.listener.redis;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.entity.BeingMiGameTable;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.Play;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.entity.GameTablePO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IGameTableService;

/**
 * Redis订阅监听器
 * 异常桌台结算
 * 
 * @author alan
 * @date 2017年6月9日 下午2:11:15
 */
@Component
public class UpdateGameTableListener{
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
    private RedisTemplate<String,String> redisTemplate;
	
	@Autowired
	private IGameTableService gameTableService;
	
    public void onMessage(Object message) {
    	
    	Integer params = (Integer) message;
    	
		if (params == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		//更新缓存
		GameTablePO gameTablePo = gameTableService.findGametableByTid(params);
		
		if (gameTablePo == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		if(gameTablePo.getGameId().compareTo(3) == 0){
			if(RouletteCache.getGameTableById(gameTablePo.getId()) == null){
				RouletteGameTable rouletteGameTable = new RouletteGameTable(gameTablePo);

				List<Play> playList = AppCache.getPlayList().stream().filter(play -> {
					return gameTablePo.getGameId() == play.getGameId();
				}).collect(Collectors.toList());
				rouletteGameTable.setPlayList(playList);
				RouletteCache.getGameTableMap().put(gameTablePo.getId(), rouletteGameTable);
			}else{
				RouletteGameTable gameTable = RouletteCache.getGameTableById(gameTablePo.getId());
				gameTable.getGameTablePO().setCountDownSeconds(gameTablePo.getCountDownSeconds());
				gameTable.getGameTablePO().setName(gameTablePo.getName());
				gameTable.getGameTablePO().setStatus(gameTablePo.getStatus());
				gameTable.getGameTablePO().setIsMany(gameTablePo.getIsMany());
				gameTable.getGameTablePO().setType(gameTablePo.getType());
				gameTable.getGameTablePO().setMiCountDownSeconds(gameTablePo.getMiCountDownSeconds());
			}
		}else if(gameTablePo.getGameId().compareTo(1) == 0){
			if(BaccaratCache.getGameTableById(gameTablePo.getId()) == null){
				GameTable gameTable = null;
				if(gameTablePo.isBeingTable()){
					gameTable = new BeingMiGameTable();
					gameTable.setGameTablePO(gameTablePo);
				}else{
					gameTable = new GameTable();
					gameTable.setGameTablePO(gameTablePo);
				}
				
				List<Play> playList = AppCache.getPlayList().stream().filter(play -> {
					return gameTablePo.getGameId() == play.getGameId();
				}).collect(Collectors.toList());
				gameTable.setPlayList(playList);
				BaccaratCache.getGameTableMap().put(gameTablePo.getId(), gameTable);
			}else{
				GameTable gameTable = BaccaratCache.getGameTableById(gameTablePo.getId());
				gameTable.getGameTablePO().setCountDownSeconds(gameTablePo.getCountDownSeconds());
				gameTable.getGameTablePO().setName(gameTablePo.getName());
				gameTable.getGameTablePO().setStatus(gameTablePo.getStatus());
				gameTable.getGameTablePO().setIsMany(gameTablePo.getIsMany());
				gameTable.getGameTablePO().setType(gameTablePo.getType());
				gameTable.getGameTablePO().setMiCountDownSeconds(gameTablePo.getMiCountDownSeconds());
			}
		}
    }
}
