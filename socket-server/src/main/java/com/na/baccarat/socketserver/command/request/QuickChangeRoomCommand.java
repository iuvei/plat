package com.na.baccarat.socketserver.command.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.command.requestpara.JoinRoomPara;
import com.na.baccarat.socketserver.command.requestpara.LeaveRoomPara;
import com.na.baccarat.socketserver.command.requestpara.QuickChangeRoomPara;
import com.na.roulette.socketserver.command.request.RouletteJoinTableCommand;
import com.na.roulette.socketserver.command.request.RouletteLeaveTableCommand;
import com.na.roulette.socketserver.command.requestpara.JoinTablePara;
import com.na.roulette.socketserver.command.requestpara.LeaveTablePara;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.entity.GamePO;
import com.na.user.socketserver.exception.SocketException;

/**
 * 快速换桌
 * @author alan
 * @date 2017年4月27日 下午3:34:58
 */
@Cmd(name="快速换桌",paraCls=QuickChangeRoomPara.class)
@Component
public class QuickChangeRoomCommand implements ICommand {
	
	private Logger log = LoggerFactory.getLogger(QuickChangeRoomCommand.class);
	
	@Autowired
	private SocketIOServer socketIOServer;
	
	@Autowired
	private LeaveRoomCommand leaveRoomCommand;
	
	@Autowired
	private JoinRoomCommand joinRoomCommand;
	
	@Autowired
	private RouletteLeaveTableCommand rouletteLeaveTableCommand;
	
	@Autowired
	private RouletteJoinTableCommand rouletteJoinTableCommand;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		QuickChangeRoomPara params = (QuickChangeRoomPara) commandReqestPara;
		
		if (params == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		if (params.getGameId() == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		if (params.getSource() == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		if (params.getChipId() == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		if (params.getTableId() == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		GamePO preGamePO = AppCache.getGame(params.getPreGameId());
		if(preGamePO == null) {
			throw SocketException.createError("game.not.support");
		}
		if(GameEnum.BACCARAT == preGamePO.getGameEnum()) {
			LeaveRoomPara leaveRoomPara = new LeaveRoomPara();
			leaveRoomPara.setQuickChangeRoom(true);
			leaveRoomCommand.exec(client, leaveRoomPara);
		} else if(GameEnum.ROULETTE == preGamePO.getGameEnum()) {
			LeaveTablePara leaveRoomPara = new LeaveTablePara();
			leaveRoomPara.setQuickChangeRoom(true);
			rouletteLeaveTableCommand.exec(client, leaveRoomPara);
		} else {
			throw SocketException.createError("game.not.support");
		}
		
		GamePO gamePO = AppCache.getGame(params.getGameId());
		if(gamePO == null) {
			throw SocketException.createError("game.not.support");
		}
		
		if(GameEnum.BACCARAT == gamePO.getGameEnum()) {
			JoinRoomPara joinRoomPara = new JoinRoomPara();
			joinRoomPara.setQuickChangeRoom(true);
			joinRoomPara.setSource(params.getSource());
			joinRoomPara.setChipId(params.getChipId());
			joinRoomPara.setTableId(params.getTableId());
			if(params.getVirtualTableId() != null) {
				joinRoomPara.setVirtualTableId(params.getVirtualTableId());
			}
			joinRoomCommand.exec(client, joinRoomPara);
			return true;
		} else if(GameEnum.ROULETTE == gamePO.getGameEnum()) {
			JoinTablePara joinTablePara = new JoinTablePara();
			joinTablePara.setQuickChangeRoom(true);
			joinTablePara.setChipId(params.getChipId());
			joinTablePara.setTableId(params.getTableId());
			rouletteJoinTableCommand.exec(client, joinTablePara);
			return true;
		} else {
			throw SocketException.createError("game.not.support");
		}
	}

}