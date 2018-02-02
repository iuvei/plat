package com.na.user.socketserver.command.flow.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.flow.user.BaccaratDealerAction;
import com.na.baccarat.socketserver.command.sendpara.LeaveRoomResponse;
import com.na.remote.IPlatformUserRemote;
import com.na.roulette.socketserver.command.request.RouletteLeaveTableCommand;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.requestpara.LoginInfoPara;
import com.na.user.socketserver.command.send.UserCommand;
import com.na.user.socketserver.command.sendpara.LoginInfoResponse;
import com.na.user.socketserver.command.sendpara.ReconnectResponse;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.entity.DealerClassRecordPO;
import com.na.user.socketserver.entity.DealerUserPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IDealerClassRecordService;

@Component
public class DealerUser extends User {

	private Logger log = LoggerFactory.getLogger(DealerUser.class);
	
	@Autowired
	protected SocketIOServer socketIOServer;
	
	@Autowired
	private UserCommand userCommand;
	
	@Autowired
	private RouletteLeaveTableCommand rouletteLeaveTableCommand;
	
	@Autowired
	private BaccaratDealerAction baccaratDealerUser;
	
	@Autowired
	private IDealerClassRecordService dealerClassRecordService;
	
	@Autowired
	private IPlatformUserRemote platformUserRemote;
	
	@Override
	public void login(LoginInfoPara params, SocketIOClient client, LoginInfoResponse response) {
		UserPO userPO = AppCache.getUserByClient(client);
		if(userPO == null) {
			throw SocketException.createError("user.not.exist");
		}
		
		//记录荷官交接班
		dealerClassRecordService.add((DealerUserPO)userPO);
		
		response.setUser(userPO);
		String token = generateToken(userPO.getId());
		response.setToken(token);
		userCommand.loginSuceess(client,response);
	}

	@Override
	public void reconnect(SocketIOClient client) {
		UserPO userPO = AppCache.getUserByClient(client);
		if(userPO == null) {
			throw SocketException.createError("user.not.exist");
		}
		
		UserPO leaveSeatUser = AppCache.removeDisConnectUser(userPO.getId());
		if (leaveSeatUser!=null) {
			AppCache.removeDisConnectClient(userPO.getId());
			log.debug("【登陆】用户为断线用户, userID:" + userPO.getId());
			
			if(leaveSeatUser.getGameCode() != null) {
				client.set(SocketClientStoreEnum.GAME_CODE.get(), leaveSeatUser.getGameCode());
			}
			if(leaveSeatUser.getTableId() != null) {
				client.set(SocketClientStoreEnum.TABLE_ID.get(), leaveSeatUser.getTableId() + "");
			}
			
			//断线重连,用旧的缓存,新生成的为null
			leaveSeatUser.setLoginId(userPO.getLoginId());
			AppCache.addLoginUser(leaveSeatUser);
			//抛弃新的引用对象
			userPO = null;
			
			ReconnectResponse response = new ReconnectResponse();
  			userCommand.send(client, RequestCommandEnum.CLIENT_RECONNECT, response);
		}
	}

	@Override
	public void logout(CommandReqestPara commandReqestPara, SocketIOClient client) {
		UserPO user = AppCache.getUserByClient(client);
		if (null == user)
			throw SocketException.createError("login.again");
		
		GameEnum gameEnum = GameEnum.get(client.get(SocketClientStoreEnum.GAME_CODE.get()));
		
		try {
			if(GameEnum.BACCARAT == gameEnum) {
				baccaratDealerUser.leave(null, client, new LeaveRoomResponse());
			} else if(GameEnum.ROULETTE == gameEnum) {
				rouletteLeaveTableCommand.exec(client, commandReqestPara);
			}
		} catch(Exception e) {
			throw e;
		} finally {
			DealerClassRecordPO dealerClassRecordPO = dealerClassRecordService.dealerLogout(((DealerUserPO)user).getDealerClassRecordId());
			
			try {
				platformUserRemote.sendDealerClassRecord(JSONObject.toJSONString(dealerClassRecordPO));
			} catch (Exception e) {
				log.error("远程调用异常", e);
			}
			userService.logout(user);
			AppCache.removeLoginUser(user);
			userCommand.logoutSuccess(client);
			user = null;
		}
	}
	
}
