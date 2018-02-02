package com.na.roulette.socketserver.command.send;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.baccarat.socketserver.command.sendpara.NewGameResponse;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.command.RouletteRequestCommandEnum;
import com.na.roulette.socketserver.command.sendpara.RouletteSendDealerResponse;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.roulette.socketserver.entity.RouletteRound;
import com.na.roulette.socketserver.entity.RouletteUser;
import com.na.user.socketserver.command.send.UserCommand;
import com.na.user.socketserver.command.sendpara.CommandResponse;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.service.IUserService;
import com.na.user.socketserver.util.SocketUtil;

/**
 * 发出给荷官用户相关指令集合。 Created by sunny on 2017/4/28 0028.
 */
@Component
public class RouletteDealerCommand {

	@Autowired
	private SocketIOServer socketIOServer;
	@Autowired
	private IUserService userService;
	@Autowired
	private UserCommand userCommand;


	/**
	 * 新一局命令响应。
	 * 
	 * @param client
	 * @param round
	 */
	public void newGameSuccess(SocketIOClient client, RouletteRound round, RouletteGameTable gameTable){
    	RoundPO roundPO = round.getRoundPO();
    	NewGameResponse res = new NewGameResponse();
        res.setRoundId(roundPO.getId());
        res.setRoundNumber(roundPO.getRoundNumber());
        res.setGameId(roundPO.getGameId());
        res.setRoundStatus(roundPO.getStatus());
        res.setGameTableId(roundPO.getGameTableId());
        res.setShowTableStatus(gameTable.getShowStatus());

        CommandResponse response = CommandResponse.createSuccess(RouletteRequestCommandEnum.COMMOM_BEW_GAME,res);

        socketIOServer.getAllClients().forEach(item->{
        	int roundTid = roundPO.getGameTableId();
        	RouletteUser user = RouletteCache.getUserByClient(item);
            if(null == user || null == user.getUserPO().getTableId())
            	return;
            if( user.getUserPO().getTableId() != roundTid)
            	return;
            if(item.isChannelOpen() && user.getUserPO().getTableId().equals(roundTid)){
                SocketUtil.send(item,ResponseCommandEnum.OK,response);
            }
        });
    }

	/**
	 * 发送荷官消息
	 */
	public void sendMessage(SocketIOClient client, RouletteSendDealerResponse sendResponse) {
		CommandResponse response = CommandResponse.createSuccess(
				RequestCommandEnum.SEND_DEALER_MESSAGE, sendResponse);
		SocketUtil.send(client, ResponseCommandEnum.OK, response);
	}

	/**
	 * 荷官离开房间(推出按钮)
	 * 
	 * @param client
	 */
	public void dealerLeaveRoom(SocketIOClient client) {
		/*
		 * User dealer = AppCache.getUserByClient(client); if(dealer == null ||
		 * UserTypeEnum.DEALER!=dealer.getTypeEnum() || null ==
		 * dealer.getTableId()) return; GameTable gameTable = (GameTable)
		 * AppCache.getGameTableById(dealer.getTableId());
		 * dealer.setLastLogoutDate(new Date()); userService.logout(dealer);
		 * gameTable.setInstantStateEnum(GameTableInstantStateEnum.INACTIVE);
		 * gameTable.setStatus(0); gameTable.setDealer(null);
		 * gameTable.setRound(new Round()); //游戏状态在投注中处理-todo LevelTableResponse
		 * levelTableResponse = new LevelTableResponse();
		 * levelTableResponse.setLoginId(dealer.getLoginId());
		 * levelTableResponse.setTableId(dealer.getTableId());
		 * levelTableResponse.setUserType(dealer.getTypeId());
		 * userCommand.levelRoom(levelTableResponse);
		 * AppCache.removeUser(dealer);
		 */

	}

}
