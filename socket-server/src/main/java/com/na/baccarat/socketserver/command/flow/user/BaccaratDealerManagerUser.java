package com.na.baccarat.socketserver.command.flow.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.requestpara.JoinRoomPara;
import com.na.baccarat.socketserver.command.requestpara.LeaveRoomPara;
import com.na.baccarat.socketserver.command.send.DealerCommand;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.send.TableCommand;
import com.na.baccarat.socketserver.command.sendpara.JoinRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.LeaveRoomResponse;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.user.socketserver.command.flow.game.GameAction;
import com.na.user.socketserver.command.flow.user.DealerManagerUser;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.entity.DealerUserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.util.ConvertUtil;

@Component
public class BaccaratDealerManagerUser extends DealerManagerUser implements GameAction {
	
	private Logger log = LoggerFactory.getLogger(BaccaratDealerManagerUser.class);
	
	@Autowired
	private RoomCommand roomCommand;
	@Autowired
	private DealerCommand dealerCommand;
	@Autowired
	private TableCommand tableCommand;
	
	@Override
	public void join(JoinRoomPara params, SocketIOClient client, JoinRoomResponse response) {
		com.na.baccarat.socketserver.entity.User loginUser = BaccaratCache.getLoginUser(ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get())));
		DealerUserPO loginUserPO = (DealerUserPO) loginUser.getUserPO();
		
		Integer tableId = params.getTableId();
		GameTable table;
		if(tableId != null) {
			table = BaccaratCache.getGameTableById(params.getTableId());
			response.setTableId(tableId);
		} else {
			throw SocketException.createError("please.select.table");
		}
		log.debug("荷官主管" + loginUserPO.getLoginName() + "加入实体桌" + table.getGameTablePO().getId() + " : " + table.getGameTablePO().getName());
		
		if(table.getCheckers() != null) {
			throw SocketException.createError("table.exist.checker");
		}
		
		table.setCheckers(loginUser);
		loginUserPO.setTableId(tableId);
		loginUserPO.setInTable(true);
		roomCommand.send(client, RequestCommandEnum.COMMON_JOIN_ROOM, response);
		//发送桌子信息
		tableCommand.sendTableStatus(client);
	}
	
	@Override
	public void leave(LeaveRoomPara params, SocketIOClient client,
			LeaveRoomResponse response) {
		com.na.baccarat.socketserver.entity.User loginUser = BaccaratCache.getLoginUser(ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get())));
		DealerUserPO loginUserPO = (DealerUserPO) loginUser.getUserPO();
		
		GameTable table = BaccaratCache.getGameTableById(ConvertUtil.toInt(client.get(SocketClientStoreEnum.TABLE_ID.get())));
		if (loginUserPO.getId() == table.getCheckers().getUserPO().getId()) {
			//将实体桌的荷官主管置为空
			table.setCheckers(null);
		} else {
			throw SocketException.createError("checker.not.equals");
		}
	}

}
