package com.na.roulette.socketserver.command.request;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.command.RouletteRequestCommandEnum;
import com.na.roulette.socketserver.command.send.RouletteTableCommand;
import com.na.roulette.socketserver.command.sendpara.RouletteStartBetResponse;
import com.na.roulette.socketserver.common.enums.RouletteGameTableInstantStateEnum;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.roulette.socketserver.entity.RouletteUser;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IRoundService;

/**
 * 轮盘开始投注
 * @author Administrator
 *
 */
@Cmd(paraCls = CommandReqestPara.class,name = "轮盘开始投注")
@Component
public class RouletteStartBetCommand implements ICommand{
	
	@Autowired
	private RouletteTableCommand tableCommand;

	@Autowired
	private IRoundService roundService;
	
	@Override
	public boolean exec(SocketIOClient client,
			CommandReqestPara commandReqestPara) {
		RouletteUser user = RouletteCache.getUserByClient(client);
		if(user.getUserPO().getUserTypeEnum() != UserTypeEnum.DEALER)
			throw SocketException.createError("user.type.error");
		
		RouletteGameTable gameTable = (RouletteGameTable) RouletteCache
				.getGameTableById(user.getUserPO().getTableId());
		if(null == gameTable)
			throw SocketException.createError("table.not.exist");
		
		gameTable.getRound().getRoundPO().setStatusEnum(RoundStatusEnum.BETTING);
		gameTable.setInstantStateEnum(RouletteGameTableInstantStateEnum.BETTING);
		if (gameTable.getGameTablePO().getCountDownSeconds() != null) {
			gameTable.setCoundDownDate(new Date());
		}
		
		RouletteStartBetResponse response = new RouletteStartBetResponse();
		response.setTableId(gameTable.getGameTablePO().getId());
		response.setCountDown(gameTable.getGameTablePO().getCountDownSeconds());
		response.setShowTableStatus(gameTable.getShowStatus());
		roundService.update(gameTable.getRound().getRoundPO());
		tableCommand.sendAllTablePlayer(response, RouletteRequestCommandEnum.DEALER_START_BET, gameTable.getGameTablePO().getId());
		return true;
	}

}
