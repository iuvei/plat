package com.na.roulette.socketserver.command.request;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.command.RouletteRequestCommandEnum;
import com.na.roulette.socketserver.command.requestpara.ClearRoundPara;
import com.na.roulette.socketserver.command.send.RouletteTableCommand;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.roulette.socketserver.entity.RouletteRound;
import com.na.roulette.socketserver.entity.RouletteUser;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IBetOrderService;
import com.na.user.socketserver.service.IUserService;


/**
 * 轮盘清除路子(荷官使用)
 * @author Administrator
 *
 */
@Cmd(paraCls = ClearRoundPara.class,name = "轮盘清除路子(荷官使用)")
@Component
public class RouletteClearRoundsCommand implements ICommand {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IBetOrderService betOrderService;
	
	@Autowired
	private RouletteTableCommand rouletteTableCommand;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		ClearRoundPara params = (ClearRoundPara) commandReqestPara;
		
		if(params.getTableId() == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		RouletteGameTable table = RouletteCache.getGameTableById(params.getTableId());
		RouletteRound round = table.getRound();
        RoundPO roundPO = round.getRoundPO();
		RouletteUser user = RouletteCache.getUserByClient(client);
		
		if(user == null) {
			throw SocketException.createError("user.not.exist");
		}
		
		if(RoundStatusEnum.FINISH != table.getRound().getRoundPO().getStatusEnum() 
				&& RoundStatusEnum.PAUSE != table.getRound().getRoundPO().getStatusEnum() ) {
			throw SocketException.createError("table.status.abnormal");
		}
		
		table.getRounds().clear();
		table.setRounds(new ArrayList<>());
        
        rouletteTableCommand.send(client, RouletteRequestCommandEnum.DEALER_CLEAR_ROUND, null);
		return true;
	}
	

}
