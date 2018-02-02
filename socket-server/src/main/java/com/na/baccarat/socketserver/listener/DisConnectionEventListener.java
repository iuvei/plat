package com.na.baccarat.socketserver.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;
import com.na.baccarat.socketserver.entity.BeingMiGameTable;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.User;
import com.na.baccarat.socketserver.entity.VirtualGameTable;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.request.LogoutCommand;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.common.event.DisConnectionEvent;
import com.na.user.socketserver.entity.GameTablePO;
import com.na.user.socketserver.entity.UserPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;


/**
 * 删除用户数据任务
 * 
 * @author alan
 * @date 2017年5月5日 下午3:12:04
 */
@Component
public class DisConnectionEventListener implements ApplicationListener<DisConnectionEvent> {
	
	private Logger log = LoggerFactory.getLogger(DisConnectionEventListener.class);
	
	@Autowired
	private SocketIOServer socketIOServer;
	
	@Autowired
	private LogoutCommand logoutCommand;

	@Async
	@Override
	public void onApplicationEvent(DisConnectionEvent event) {
		Integer tableId = (Integer) event.getSource();
        if(tableId == null ) {
        	log.error("没找到相应桌子");
            return ;
        }
        
        GameTable gametale = BaccaratCache.getGameTableById(tableId);
        if(gametale == null || AppCache.getGame(gametale.getGameTablePO().getGameId()).getGameEnum() != GameEnum.BACCARAT) {
        	return ;
        }
        GameTablePO gameTablePO = gametale.getGameTablePO();
        
        log.debug("【任务】触发删除用户缓存数据任务");
		
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
        
        boolean flag = false;
        if(gameTablePO.isBeingTable()) {
        	flag = true;
		}
        
        for(UserPO item : userList) {
        	try {
				User user = BaccaratCache.getLoginUser(item.getId());
				if (user == null) {
					continue;
				}

				if (flag) {
					BeingMiGameTable gameTable = (BeingMiGameTable) BaccaratCache.getGameTableById(gameTablePO.getId());
					if (user.getSource() == BetOrderSourceEnum.SEAT) {
						for (Entry<Integer, Long> entry : gameTable.getPlayers().entrySet()) {
							if (entry.getValue().compareTo(item.getId()) == 0) {
								gameTable.getPlayers().remove(entry.getKey());
								break;
							}
						}
					} else if (user.getSource() == BetOrderSourceEnum.SIDENOTE) {
						gameTable.getBesideUser().remove(item.getId());
					}
				} else {
					VirtualGameTable virtualGameTable = BaccaratCache.getVirtualTableById(user.getVirtualTableId());
					if (user.getSource() == BetOrderSourceEnum.SEAT) {
						BaccaratCache.leaveVirtualTableSeat(virtualGameTable.getVirtualGameTablePO().getGameTableId(), 
								virtualGameTable.getVirtualGameTablePO().getId(), user.getSeat(), user.getUserPO().getId());
						BaccaratCache.freshVirtualTable(virtualGameTable);
					} else if (user.getSource() == BetOrderSourceEnum.SIDENOTE) {
						virtualGameTable.getBesideUser().remove(item.getId());
						BaccaratCache.freshVirtualTable(virtualGameTable);
					}
				}
				
				BaccaratCache.removeUser(item.getId());
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
