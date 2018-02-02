package com.na.roulette.socketserver.command.request;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.command.send.RouletteDealerCommand;
import com.na.roulette.socketserver.command.send.RouletteTableCommand;
import com.na.roulette.socketserver.common.enums.RouletteGameTableInstantStateEnum;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.roulette.socketserver.entity.RouletteRound;
import com.na.roulette.socketserver.entity.RouletteUser;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IRoundService;

/**
 * 轮盘新一局
 * 
 * @author v
 * @date 2017年5月11日 上午10:16:28
 */
@Cmd(paraCls = CommandReqestPara.class, name = "轮盘新一局")
@Component
public class RouletteNewGameCommand implements ICommand {

	@Autowired
    private IRoundService roundService;
	@Autowired
	private RouletteTableCommand tableCommand;
	@Autowired
	private RouletteDealerCommand dealerCommand;

	@Override
	public boolean exec(SocketIOClient client,
			CommandReqestPara commandReqestPara) {
		
		RouletteUser user = RouletteCache.getUserByClient(client);
		if (user == null || user.getUserPO().getUserTypeEnum() != UserTypeEnum.DEALER)
			throw SocketException.createError("user.type.error");
		RouletteGameTable gameTable = RouletteCache.getGameTableById(user.getUserPO().getTableId());

		if (gameTable == null)
			throw SocketException.createError("table.not.exist");

		if (gameTable.getInstantStateEnum() != RouletteGameTableInstantStateEnum.INACTIVE
				&& gameTable.getInstantStateEnum() != RouletteGameTableInstantStateEnum.NEWGAME
				&& gameTable.getInstantStateEnum() != RouletteGameTableInstantStateEnum.FINISH)
			throw SocketException.createError("table.status.abnormal");
		RouletteRound currentRound = gameTable.getRound();
		RoundPO newRoundPO = new RoundPO();
		RouletteRound newRound = new RouletteRound(newRoundPO);
		
		gameTable.setInstantStateEnum(RouletteGameTableInstantStateEnum.NEWGAME);
		newRoundPO.setStatusEnum(RoundStatusEnum.NEWGAME);
		newRoundPO.setRoundNumber(currentRound.getRoundPO().getRoundNumber() + 1);
		newRoundPO.setGameId(gameTable.getGameTablePO().getGameId());
		newRoundPO.setGameTableId(gameTable.getGameTablePO().getId());
		newRoundPO.setStartTime(new Date());
		newRoundPO.setBootId("");
		newRoundPO.setBootNumber(0);
		newRoundPO.setBootStartTime(new Date());
		
		
		gameTable.setRound(newRound);
		roundService.add(gameTable.getRound().getRoundPO());
		
		tableCommand.sendTableStatus(client);
        dealerCommand.newGameSuccess(client,gameTable.getRound(), gameTable);
		//gameTable.initTradeItemBetMoneyMap();
		return true;
	}

}
