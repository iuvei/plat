package com.na.baccarat.socketserver.command.request;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.baccarat.socketserver.command.requestpara.GameResultPara;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.sendpara.GameResultResponse;
import com.na.baccarat.socketserver.common.enums.GameTableInstantStateEnum;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.Round;
import com.na.remote.IPlatformUserRemote;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.event.DisConnectionEvent;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IRoundExtService;
import com.na.user.socketserver.service.IRoundService;
import com.na.user.socketserver.util.ActiveMQUtil;
import com.na.user.socketserver.util.BeanUtil;
import com.na.user.socketserver.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 结算
 * 
 * @author alan
 * @date 2017年4月27日 下午3:34:58
 */
@Cmd(paraCls=GameResultPara.class,name="结算")
@Component
public class GameResultCommand implements ICommand {
	
	private Logger log = LoggerFactory.getLogger(GameResultCommand.class);
	
	@Autowired
	private SocketIOServer socketIOServer;
	
	@Autowired
	private IRoundService roundService;
	
	@Autowired
	private IRoundExtService roundExtService;
	
	@Autowired
	private RoomCommand roomCommand;
	
	@Autowired
	private BaccaratClassHandler baccaratClassHandler;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private IPlatformUserRemote platformUserRemote;
	
	@Autowired
	private ActiveMQUtil activeMQUtil;

	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		GameResultPara params = (GameResultPara) commandReqestPara;
		
		if (params == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		Integer tableId = ConvertUtil.toInt(client.get(SocketClientStoreEnum.TABLE_ID.get()));
		log.debug("实体桌:" + tableId);
		GameTable table = BaccaratCache.getGameTableById(tableId) ;
		GameResultResponse gameResultResponse = new GameResultResponse();
		if(table != null) {
			
			Round round = table.getRound();
			RoundPO roundPO = round.getRoundPO();
			
			if(roundPO.getStatusEnum() == RoundStatusEnum.AWAITING_RESULT) {
				roundPO.setStatusEnum(RoundStatusEnum.FINISH);
				table.setInstantStateEnum(GameTableInstantStateEnum.FINISH);
				
				baccaratClassHandler.table(table).gameResult(params, client, gameResultResponse);
				
				log.debug("【百家乐结算】  用户订单结算完毕, 桌子:" + tableId);
				roundExtService.update(table.getRoundExt().getRoundExtPO());
				roundService.update(roundPO);
				//该局数据归入路子
				addRound(table);
				try {
					platformUserRemote.sendRound(roundPO.getId());
					activeMQUtil.sendResult(roundPO.getId(), null);
				} catch (RuntimeException e) {
					log.warn("", e);
				}
				
				gameResultResponse.setResult(roundPO.getResult());
				
				//清除当局用户投注细节
				round.setUserBetedInfos(new CopyOnWriteArrayList<>());
				roomCommand.send(client, RequestCommandEnum.COMMON_GAME_RESULT, gameResultResponse);
				
				//结算完毕  清除离线用户
				applicationContext.publishEvent(new DisConnectionEvent(table.getGameTablePO().getId()));
				log.info("桌：" + table.getGameTablePO().getName() + " 局ID： " + roundPO.getId() +" 结算完成");
				return true;
			} else {
				throw SocketException.createError("table.status.abnormal");
			}
		} else {
			throw SocketException.createError("table.not.exist");
		}
	}
	
	private void addRound(GameTable table) {
		Round round = BeanUtil.cloneTo(table.getRound());
		List<Round> rounds = table.getRounds();
		if(rounds != null && rounds.size() > 0) {
			if(rounds.get(rounds.size() - 1).getRoundPO().getId() != round.getRoundPO().getId()) {
				rounds.add(round);
			}
		} else {
			rounds = new CopyOnWriteArrayList<>();
			rounds.add(round);
			table.setRounds(rounds);
		}
	}

}