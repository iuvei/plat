package com.na.user.socketserver.listener.redis;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.entity.GamePO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IGameService;

/**
 * 更新VIP包房
 * 
 * @author alan
 * @date 2017年8月4日 上午10:19:08
 */
@Component
public class UpdateGameConfigListener {
	
	private Logger log = LoggerFactory.getLogger(UpdateGameConfigListener.class);
	
	@Autowired
	private  IGameService gameService;
	
    public void onMessage(Object message) {
    	Integer params = (Integer) message;
    	
		if (params == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		log.debug("【游戏配置热更新】 参数：" + params );
		
		Map<String,String> map = gameService.getConfigById(params);
		GamePO gamePO = AppCache.getGame(Integer.valueOf(map.get("game_id")));
		gamePO.getGameConfig().put(map.get("key"), map.get("value"));
    }
    
}
