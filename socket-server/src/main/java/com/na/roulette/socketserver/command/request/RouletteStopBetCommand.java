package com.na.roulette.socketserver.command.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.command.RouletteRequestCommandEnum;
import com.na.roulette.socketserver.command.send.RouletteTableCommand;
import com.na.roulette.socketserver.command.sendpara.RouletteStopBetResponse;
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
 * 轮盘停止投注
 * 
 * @author Administrator
 *
 */
@Cmd(paraCls = CommandReqestPara.class, name = "轮盘停止投注")
@Component
public class RouletteStopBetCommand implements ICommand {

	@Autowired
	private RouletteTableCommand tableCommand;
	
	@Autowired
	private IRoundService roundService;
	
	@Override
	public boolean exec(SocketIOClient client,
			CommandReqestPara commandReqestPara) {
		RouletteUser user = RouletteCache.getUserByClient(client);
		if (user.getUserPO().getUserTypeEnum() != UserTypeEnum.DEALER)
			throw SocketException.createError("user.type.error");

		RouletteGameTable gameTable = (RouletteGameTable) RouletteCache
				.getGameTableById(user.getUserPO().getTableId());
		if (null == gameTable)
			throw SocketException.createError("table.not.exist");

		if (gameTable.getRound().getRoundPO().getStatusEnum() != RoundStatusEnum.BETTING
				&& gameTable.getRound().getRoundPO().getStatusEnum() != RoundStatusEnum.PAUSE)
			throw SocketException.createError("Table:" + gameTable.getGameTablePO().getId()
					+ " Is In" + gameTable.getRound().getRoundPO().getStatusEnum().getDesc()
					+ "!");

		gameTable.getRound().getRoundPO().setStatusEnum(RoundStatusEnum.AWAITING_RESULT);
		gameTable.setInstantStateEnum(RouletteGameTableInstantStateEnum.AWAITING_RESULT);
		
		//清空个人限红和台红检查
		gameTable.setLimitRule(null);
		RouletteCache.getTableUserList(user.getUserPO().getTableId()).forEach( item -> {
			item.setLimitRule(null);
		});
		
		RouletteStopBetResponse rouletteStopBetResponse = new RouletteStopBetResponse();
		rouletteStopBetResponse.setShowTableStatus(gameTable.getShowStatus());
		roundService.update(gameTable.getRound().getRoundPO());
		tableCommand.sendAllTablePlayer(rouletteStopBetResponse, RouletteRequestCommandEnum.COMMOM_STOP_BET, gameTable.getGameTablePO().getId());
		return true;
	}

}
