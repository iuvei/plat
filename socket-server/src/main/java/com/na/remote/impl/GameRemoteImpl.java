package com.na.remote.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.request.LeaveRoomCommand;
import com.na.baccarat.socketserver.command.requestpara.LeaveRoomPara;
import com.na.baccarat.socketserver.listener.event.MultiDisConnectionEvent;
import com.na.roulette.socketserver.command.request.RouletteLeaveTableCommand;
import com.na.roulette.socketserver.command.requestpara.LeaveTablePara;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.request.LogoutCommand;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.event.DisConnectionEvent;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.util.SocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.na.light.LightRpcService;
import com.na.remote.IGameRemote;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @date 2017年9月16日 上午11:42:23
 */
@LightRpcService(value="gameRemote")
public class GameRemoteImpl implements IGameRemote {
	
	private Logger log = LoggerFactory.getLogger(GameRemoteImpl.class);

	@Autowired
	private LogoutCommand logoutCommand;
	@Autowired
	private SocketIOServer socketIOServer;
	@Autowired
	private LeaveRoomCommand leaveRoomCommand;
	@Autowired
	private RouletteLeaveTableCommand rouletteLeaveTableCommand;

	@Override
	public void clearRouletteDealer() {
		log.debug("清理轮盘荷官");
    	RouletteGameTable table = RouletteCache.getGameTableById(122);
    	table.setDealer(null);
    }

	@Override
    public void clearOnlineUser() {
		log.debug("清除所有在线用户");
		Map<Long, UserPO> userMap = AppCache.getCurrentLoginUserMap();
		userMap.values().parallelStream().forEach( userPO -> {
			SocketIOClient client = SocketUtil.getClientByUser(socketIOServer, userPO.getId());
			if(userPO.isInTable()) {
				String gameCode = client.get(SocketClientStoreEnum.GAME_CODE.get());
				if(BaccaratCache.getGame().getGamePO().getGameCode().equals(gameCode)) {
					LeaveRoomPara param = new LeaveRoomPara();
//					param.setForce(true);
					leaveRoomCommand.exec(client, param);
				} else if(RouletteCache.getGame().getGamePO().getGameCode().equals(gameCode)) {
					LeaveTablePara param = new LeaveTablePara();
//					param.setForce(true);
					rouletteLeaveTableCommand.exec(client, param);
				}
			}
			logoutCommand.exec(client, null);
			AppCache.removeDisConnectUser(userPO.getId());
			AppCache.removeDisConnectClient(userPO.getId());
		});

	}
}
