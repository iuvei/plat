package com.na.roulette.socketserver.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.request.LogoutCommand;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.common.event.DisConnectionEvent;
import com.na.user.socketserver.entity.GameTablePO;
import com.na.user.socketserver.entity.UserPO;


/**
 * 删除用户数据任务
 * 
 * @author alan
 * @date 2017年5月5日 下午3:12:04
 */
@Component("rouletteDisConnectionEventListener")
public class DisConnectionEventListener implements ApplicationListener<DisConnectionEvent> {
	
	private Logger log = LoggerFactory.getLogger(DisConnectionEventListener.class);
	
	@Autowired
	private SocketIOServer socketIOServer;
	
	@Autowired
	private LogoutCommand logoutCommand;

	@Override
	public void onApplicationEvent(DisConnectionEvent event) {
		Integer tableId = (Integer) event.getSource();
        if(tableId == null ) {
        	log.error("没找到相应桌子");
            return ;
        }
        RouletteGameTable gameTable = RouletteCache.getGameTableById(tableId);
        if(gameTable == null || AppCache.getGame(gameTable.getGameTablePO().getGameId()).getGameEnum() != GameEnum.ROULETTE) {
        	return ;
        }
        GameTablePO gameTablePO = gameTable.getGameTablePO();
        
        log.debug("【任务】轮盘触发删除用户缓存数据任务");
		
        List<UserPO> userList = AppCache.getDisConnectUserMap().values().stream().filter( item -> {
        	if(item.isInTable()) {
        		if(item.getTableId() != null) {
        			return gameTablePO.getId().compareTo(item.getTableId()) == 0;
        		} else {
        			return false;
        		}
        	}
			return false;
        }).collect(Collectors.toList());
        
        for(UserPO item : userList) {
        	try {
        		RouletteCache.removeUser(item.getId());
        		item.setInTable(false);
        		item.setTableId(null);
				SocketIOClient client = AppCache.removeDisConnectClient(item.getId());
				if (client != null) {
					log.debug("【任务】删除该用户缓存数据 ： [" + item.getId() + "] " + item.getLoginName());
					AppCache.removeDisConnectUser(item.getId());
					logoutCommand.exec(client, null);
				}
			}catch (Exception e){
        		log.error(e.getMessage(),e);
			}
        }
	}
	

}
