package com.na.baccarat.socketserver.command.request;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.baccarat.socketserver.command.requestpara.LeaveRoomPara;
import com.na.baccarat.socketserver.command.send.DealerCommand;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.send.TableCommand;
import com.na.baccarat.socketserver.command.sendpara.LeaveRoomResponse;
import com.na.baccarat.socketserver.entity.User;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IBetOrderService;
import com.na.user.socketserver.service.IUserService;

/**
 * 离开房间
 * 
 * @author alan
 * @date 2017年4月27日 下午3:34:58
 */
@Cmd(name="离开桌子",paraCls=LeaveRoomPara.class)
@Component
public class LeaveRoomCommand implements ICommand {
	
	private Logger log = LoggerFactory.getLogger(LeaveRoomCommand.class);
	
	@Autowired
	private SocketIOServer socketIOServer;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IBetOrderService betOrderService;
	
	@Autowired
	private RoomCommand roomCommand;
	
	@Autowired
	private TableCommand tableCommand;
	
	@Autowired
	private DealerCommand dealerCommand;
	
	@Autowired
	private BaccaratClassHandler baccaratClassHandler;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		LeaveRoomPara para = (LeaveRoomPara) commandReqestPara;
		LeaveRoomResponse response = new LeaveRoomResponse();
		//获取当前实体桌
		User user = BaccaratCache.getUserByClient(client);
		if(user == null) {
			throw SocketException.createError("login.timeout");
		}
		
		UserPO userPO = user.getUserPO();
		
		baccaratClassHandler.baccaratAction(userPO).leave(para, client, response);
		
		BaccaratCache.removeUser(userPO.getId());
		
		client.del(SocketClientStoreEnum.TABLE_ID.get());
		client.del(SocketClientStoreEnum.GAME_CODE.get());
		return true;
	}
	
}