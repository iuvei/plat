package com.na.baccarat.socketserver.command.request;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.baccarat.socketserver.command.requestpara.ReqRdsPara;
import com.na.baccarat.socketserver.command.sendpara.ReqRdsResponse;
import com.na.baccarat.socketserver.common.enums.GameTableInstantStateEnum;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.baccarat.socketserver.entity.Game;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.User;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.sendpara.CommandResponse;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.util.SocketUtil;

/**
 * 获取路子信息
 * @author Administrator
 */
@Component
@Cmd(paraCls = ReqRdsPara.class, name = "获取路子信息指令")
public class ReqRdsCommand implements ICommand {

	
	private Logger log = LoggerFactory.getLogger(ReqRdsCommand.class);
	
	@Override
	public boolean exec(SocketIOClient client,
			CommandReqestPara commandReqestPara) {

		return exec(client, (ReqRdsPara) commandReqestPara);
	}

	private boolean exec(SocketIOClient client, ReqRdsPara commandReqestPara) {

		ReqRdsResponse reqRdsResponse = getDataMap();
		CommandResponse response = CommandResponse.createSuccess(RequestCommandEnum.DEALER_REQUEST_ROUNDS_INFO, reqRdsResponse);
		log.info("【Client SessionId】" + client.getSessionId() + "Send " + RequestCommandEnum.DEALER_REQUEST_ROUNDS_INFO.get() + " request to client :" +JSONObject.toJSONString(response));
		SocketUtil.send(client, ResponseCommandEnum.OK,response);
		return true;
	}

	private ReqRdsResponse getDataMap() {
		Game game = BaccaratCache.getGame();

		Map<Integer, GameTable> hashMap = BaccaratCache.getGameTableMap();
		ReqRdsResponse reqRdsResponse = new ReqRdsResponse();
		for (Integer index : hashMap.keySet()) {
			GameTable gameTable = (GameTable) hashMap.get(index);
			reqRdsResponse.setTid(gameTable.getGameTablePO().getId());
			reqRdsResponse.setBid(gameTable.getRound().getRoundPO().getBootId());
			User dealer = gameTable.getDealer();
			if (dealer != null) {
				reqRdsResponse.setDid( dealer.getUserPO().getId());
				reqRdsResponse.setDna(dealer.getUserPO().getNickName());
				reqRdsResponse.setDpic(dealer.getUserPO().getHeadPic());
			}
			if (gameTable.getRound() != null) {
				reqRdsResponse.setStu(gameTable.getRound().getRoundPO().getStatus());
			} else {
				reqRdsResponse.setStu(GameTableInstantStateEnum.INACTIVE.get());
			}
			reqRdsResponse.setRds(gameTable.getRounds());
			reqRdsResponse.setRid(gameTable.getRound().getRoundPO().getId());
			reqRdsResponse.setTp(gameTable.getGameTablePO().getType());
			if(gameTable.getGameTablePO().getType() == 1){
				//todo
				int openMaxTime = toInt(game.getGamePO().getGameConfig().get("openCardMaxTime"), -1);
				int miMaxTime = toInt(game.getGamePO().getGameConfig().get("miCardMaxTime"), -1);
				long curropenMaxTime = 0l;
				long currmiMaxTime = 0l;
				if(gameTable.getRound().getRoundPO().getStatusEnum()== RoundStatusEnum.BETTING&&openMaxTime!=-1){
					
					long curr = gameTable.getGameTablePO().getCountDownSeconds() - Math.round((System.currentTimeMillis() - gameTable.getCoundDown().get()) / 1000);
					if (gameTable.getRound().getRoundPO().getStatusEnum() == RoundStatusEnum.BETTING) {
						reqRdsResponse.setHt(curr);
					} else {
						reqRdsResponse.setHt(gameTable.getGameTablePO().getCountDownSeconds().longValue());
					}	
				}else if(gameTable.getRound().getRoundPO().getStatusEnum()== RoundStatusEnum.AWAITING_RESULT&&miMaxTime!=-1){
					
					reqRdsResponse.setTms(currmiMaxTime);
				}
				
			}else if(gameTable.getGameTablePO().getType() == 2){
				int miMaxTime = gameTable.getGameTablePO().getMiCountDownSeconds();	
				long currmiMaxTime = 0l;
				if(gameTable.getRound().getRoundPO().getStatusEnum()== RoundStatusEnum.AWAITING_RESULT&&miMaxTime!=-1){
					
					if(gameTable.getMiCardDownSeat().get()>0){
						currmiMaxTime = miMaxTime - Math.round((System.currentTimeMillis() - gameTable.getMiCardDownSeat().get()) / 1000);
						if(currmiMaxTime<0){
							currmiMaxTime=0l;
						}
					}else {
						currmiMaxTime = miMaxTime;
					}
					reqRdsResponse.setTms(currmiMaxTime);
				}
				long curr = gameTable.getGameTablePO().getCountDownSeconds() - Math.round((System.currentTimeMillis() - gameTable.getCoundDown().get()) / 1000);
				if (GameTableInstantStateEnum.get(gameTable.getRound().getRoundPO().getStatus()) == GameTableInstantStateEnum.BETTING) {
					reqRdsResponse.setHt(curr);
				} else {
					reqRdsResponse.setHt(gameTable.getGameTablePO().getCountDownSeconds().longValue());
				}	
				
			}else{
				long curr = gameTable.getGameTablePO().getCountDownSeconds() - Math.round((System.currentTimeMillis() - gameTable.getCoundDown().get()) / 1000);
				if (GameTableInstantStateEnum.get(gameTable.getRound().getRoundPO().getStatus()) == GameTableInstantStateEnum.BETTING) {
					reqRdsResponse.setHt(curr);
				} else {
					reqRdsResponse.setHt(gameTable.getGameTablePO().getCountDownSeconds().longValue());
				}	
			}
			
			reqRdsResponse.setGid(AppCache.getGame(GameEnum.BACCARAT).getId());
				
		}
		
		return reqRdsResponse;
	}

	private  int toInt(Object obj,int invault) {
		if (obj != null && !"".equals(obj)) {
			return Integer.parseInt(obj.toString());
		}
		return invault;
	}
}
