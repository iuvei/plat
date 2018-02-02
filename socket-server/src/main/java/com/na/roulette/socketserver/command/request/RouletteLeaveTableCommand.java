package com.na.roulette.socketserver.command.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.command.RouletteRequestCommandEnum;
import com.na.roulette.socketserver.command.requestpara.LeaveTablePara;
import com.na.roulette.socketserver.command.send.RouletteTableCommand;
import com.na.roulette.socketserver.command.send.RouletteUserCommand;
import com.na.roulette.socketserver.command.sendpara.RouletteLeaveTableResponse;
import com.na.roulette.socketserver.command.sendpara.RouletteLevelTableResponse;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.roulette.socketserver.entity.RouletteUser;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.DealerUserTypeEnum;
import com.na.user.socketserver.common.enums.LiveUserTypeEnum;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.entity.DealerUserPO;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IUserService;


/**
 * 轮盘离开房间
 * 
 * @author alan
 * @date 2017年5月12日 下午12:10:08
 */
@Cmd(paraCls = LeaveTablePara.class,name = "轮盘离开房间")
@Component
public class RouletteLeaveTableCommand implements ICommand {
	
	private Logger log = LoggerFactory.getLogger(RouletteLeaveTableCommand.class);

	@Autowired
	private IUserService userService;
	
	@Autowired
	private RouletteTableCommand tableCommand;
	
	@Autowired
    private RouletteUserCommand rouletteUserCommand;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		LeaveTablePara param = (LeaveTablePara) commandReqestPara;
		
		RouletteUser user = RouletteCache.getUserByClient(client);
		if(user == null)
			throw SocketException.createError("user.not.exist");
		
		UserPO userPO = user.getUserPO();
		
		RouletteGameTable gameTable = (RouletteGameTable) RouletteCache
				.getGameTableById(user.getUserPO().getTableId());
		if(null == gameTable)
			throw SocketException.createError("table.not.exist");
		
		RouletteLeaveTableResponse response = new RouletteLeaveTableResponse();
		
		response.setLoginName(userPO.getLoginName());
		response.setUserType(userPO.getUserType());
		response.setLoginId(userPO.getLoginId());
		
		if (UserTypeEnum.REAL == userPO.getUserTypeEnum()) {
			LiveUserPO liveUserPO = (LiveUserPO) userPO;
			if(LiveUserTypeEnum.PLAYER == liveUserPO.getTypeEnum()) {
				gameTable.getPlayers().remove(userPO.getId());
				
				response.setRountId(gameTable.getRound().getRoundPO().getGameId());
				tableCommand.sendAllTablePlayer(response,RouletteRequestCommandEnum.COMMON_OTHER_LEAVE_TABLE,gameTable.getGameTablePO().getId());
			}
		} else if (UserTypeEnum.DEALER == userPO.getUserTypeEnum()) {
			DealerUserPO dealerUserPO = (DealerUserPO) userPO;
			if(DealerUserTypeEnum.DEALER == dealerUserPO.getTypeEnum()) {
				if (gameTable.getDealer() != null && userPO.getId() == gameTable.getDealer().getUserPO().getId()) {
					
					gameTable.setDealer(null);
					
					RouletteLevelTableResponse levelTableResponse = new RouletteLevelTableResponse();
			        levelTableResponse.setLoginId(userPO.getLoginId());
			        levelTableResponse.setTableId(userPO.getTableId());
			        levelTableResponse.setUserType(userPO.getUserType());
			        rouletteUserCommand.dealerLevelRoom(levelTableResponse);
					
				} else {
					throw SocketException.createError("dealer.not.equals");
				}
			} else if(DealerUserTypeEnum.CHECKER == dealerUserPO.getTypeEnum()) {
				if (userPO.getId() == gameTable.getCheckers().getUserPO().getId()) {
					//将实体桌的荷官主管置为空
					gameTable.setCheckers(null);
				}

				RouletteLevelTableResponse levelTableResponse = new RouletteLevelTableResponse();
				levelTableResponse.setLoginId(userPO.getLoginId());
				levelTableResponse.setTableId(userPO.getTableId());
				levelTableResponse.setUserType(userPO.getUserType());
				rouletteUserCommand.dealerLevelRoom(levelTableResponse);
			}
			
		} else {
			throw SocketException.createError("user.type.error");
		}
		
		
		
		userPO.setTableId(null);
		userPO.setInTable(false);
		client.del(SocketClientStoreEnum.GAME_CODE.get());
		RouletteCache.removeUser(user);
		if(param != null) {
			response.setForce(param.isForce());
		} else {
			response.setForce(false);
		}
		log.debug("用户" + userPO.getLoginName() + "离开房间" + gameTable.getGameTablePO().getName());
		
		if(param == null || ((param != null && !param.isQuickChangeRoom()) && (param != null && !param.isReconnect()))) {
			tableCommand.send(client, RouletteRequestCommandEnum.COMMON_LEAVE_TABLE, response);
		}
		
		return true;
	}
	
}
