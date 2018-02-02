package com.na.baccarat.socketserver.command.flow.table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.baccarat.socketserver.command.requestpara.BetPara;
import com.na.baccarat.socketserver.command.requestpara.CutCardPara;
import com.na.baccarat.socketserver.command.requestpara.GameResultPara;
import com.na.baccarat.socketserver.command.requestpara.JoinRoomPara;
import com.na.baccarat.socketserver.command.requestpara.LeaveRoomPara;
import com.na.baccarat.socketserver.command.requestpara.NewBootPara;
import com.na.baccarat.socketserver.command.requestpara.SetCardPara;
import com.na.baccarat.socketserver.command.requestpara.ShuffleEndPara;
import com.na.baccarat.socketserver.command.requestpara.ShufflePara;
import com.na.baccarat.socketserver.command.send.DealerCommand;
import com.na.baccarat.socketserver.command.send.GoodRoadsCommand;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.send.TableCommand;
import com.na.baccarat.socketserver.command.sendpara.BetResponse;
import com.na.baccarat.socketserver.command.sendpara.CutCardResponse;
import com.na.baccarat.socketserver.command.sendpara.GameResultResponse;
import com.na.baccarat.socketserver.command.sendpara.JoinRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.LeaveRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.NewBootResponse;
import com.na.baccarat.socketserver.command.sendpara.NewGameResponse;
import com.na.baccarat.socketserver.command.sendpara.SetCardResponse;
import com.na.baccarat.socketserver.command.sendpara.ShuffleEndResponse;
import com.na.baccarat.socketserver.command.sendpara.ShuffleResponse;
import com.na.baccarat.socketserver.command.sendpara.StartBetResponse;
import com.na.baccarat.socketserver.command.sendpara.StopBetResponse;
import com.na.baccarat.socketserver.command.sendpara.TableBetResponse;
import com.na.baccarat.socketserver.common.enums.GameTableInstantStateEnum;
import com.na.baccarat.socketserver.common.enums.PlayEnum;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.baccarat.socketserver.common.limitrule.CheckLimitRule;
import com.na.baccarat.socketserver.common.playrule.AbstractPlayRule;
import com.na.baccarat.socketserver.common.playrule.B27PlayRule;
import com.na.baccarat.socketserver.common.playrule.FreeCommissionPlayRule;
import com.na.baccarat.socketserver.common.playrule.NormalPlayRule;
import com.na.baccarat.socketserver.config.BaccaratConfig;
import com.na.baccarat.socketserver.entity.Game;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.Round;
import com.na.baccarat.socketserver.entity.RoundExt;
import com.na.baccarat.socketserver.entity.User;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.entity.RoundExtPO;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IRoundExtService;
import com.na.user.socketserver.service.IRoundService;
import com.na.user.socketserver.util.ConvertUtil;
import com.na.user.socketserver.util.DateUtil;

public abstract class Table {
	
	private Logger log = LoggerFactory.getLogger(Table.class);
	
	@Autowired
	SocketIOServer socketIOServer;
	
	@Autowired
	IRoundExtService roundExtService;
	
	@Autowired
	TableCommand tableCommand;
	
	@Autowired
	RoomCommand roomCommand;
	
	@Autowired
	DealerCommand dealerCommand;
	
	@Autowired
	IRoundService roundService;
	
	@Autowired
	GoodRoadsCommand goodRoadsCommand;
	
	@Qualifier("schedulerFactoryBean")
	@Autowired
    SchedulerFactoryBean schedulerFactoryBean;
	
	@Autowired
	BaccaratClassHandler baccaratClassHandler;
	
	public abstract void join(JoinRoomPara params, SocketIOClient client, JoinRoomResponse response);
	
	public abstract void leave(LeaveRoomPara params, SocketIOClient client, LeaveRoomResponse response);
	
	public abstract void gameResult(GameResultPara params, SocketIOClient client, GameResultResponse response);
	
	public abstract void stopBet(CommandReqestPara params, SocketIOClient client, StopBetResponse response);
	
	public abstract void setCard(SetCardPara params, SocketIOClient client, SetCardResponse response);
	
	public abstract void cutCard(CutCardPara params, SocketIOClient client, CutCardResponse response);
		
	public abstract void bet(BetPara params, SocketIOClient client, BetResponse response);
	
	 
	public void shuffle(ShufflePara params, SocketIOClient client,
			ShuffleResponse response) {
		GameTable table = BaccaratCache.getGameTableById(ConvertUtil.toInt(client.get(SocketClientStoreEnum.TABLE_ID.get())));
		
//		if(GameTableInstantStateEnum.INACTIVE == table.getInstantStateEnum() || GameTableInstantStateEnum.FINISH == table.getInstantStateEnum()) {
		if(table.getRound().getRoundPO().getStatusEnum() == RoundStatusEnum.INACTIVE|| 
				table.getRound().getRoundPO().getStatusEnum() == RoundStatusEnum.FINISH) {
			
			table.getRound().getRoundPO().setStatusEnum(RoundStatusEnum.SHUFFLE);
			table.setInstantStateEnum(GameTableInstantStateEnum.SHUFFLE);
			
			//roundService.update(table.getRound().getRoundPO());
			
			if(table.getRounds() != null)
				table.getRounds().clear();
			
			response.setTableId(table.getGameTablePO().getId());
			response.setShowTableStatus(table.getShowStatus());
			//发送给所有虚拟桌玩家  洗牌中
			tableCommand.sendAllTablePlayer(response, RequestCommandEnum.DEALER_SHUFFLE, table.getGameTablePO().getId());
		} else {
			throw SocketException.createError("table.status.abnormal");
		}
	}
	
	public void shuffleEnd(ShuffleEndPara params, SocketIOClient client,
			ShuffleEndResponse response) {
		GameTable table = BaccaratCache.getGameTableById(ConvertUtil.toInt(client.get(SocketClientStoreEnum.TABLE_ID.get())));
		
		if(RoundStatusEnum.SHUFFLE == table.getRound().getRoundPO().getStatusEnum()) {
			//发送给所有虚拟桌用户
			Game game = BaccaratCache.getGame();
			Integer cutCardTime = ConvertUtil.toInt(game.getGamePO().getGameConfig().get("cutCardTime"), -1);
			response.setCutCardSecond(cutCardTime);
			response.setShowTableStatus(table.getShowStatus());
			
			roomCommand.send(client, RequestCommandEnum.DEALER_SHUFFLE_END, response);
			
			//发送用户切牌指令
			tableCommand.sendAllTableOtherClient(client, null, RequestCommandEnum.CLIENT_CUT_CARD, table.getGameTablePO().getId());
		} else {
			throw SocketException.createError("table.status.abnormal");
		}
	}
	
	public void newBoot(NewBootPara params, SocketIOClient client,
			NewBootResponse response) {
		GameTable gameTable = BaccaratCache.getGameTableById(ConvertUtil.toInt(client.get(SocketClientStoreEnum.TABLE_ID.get())));
		
		if(gameTable == null)
			throw SocketException.createError("table.not.exist");
		
		Date startTime = new Date();
		gameTable.setBootStartTime(startTime);
		gameTable.getCoundDown().set(-11);
		gameTable.getMiCardDownSeat().set(-11);
		gameTable.setChangeDealerNum(0);
		if(gameTable.getRounds() != null) {
			gameTable.getRounds().clear();
		} else {
			gameTable.setRounds(new CopyOnWriteArrayList<>());
		}
		
		Round round = gameTable.getRound();
		if(round == null) {
			round = new Round();
			round.setRoundPO(new RoundPO());
		}
		RoundPO roundPO = round.getRoundPO();
		
		
		String _now = DateUtil.format(new Date(), DateUtil.yyyyMMdd);
		
		if (null != roundPO.getBootId() && roundPO.getBootId().indexOf(_now) >= 0) {
			roundPO.setStatusEnum(RoundStatusEnum.IDLE);
			roundPO.setBootNumber(roundPO.getBootNumber() + 1);
			roundPO.setBootStartTime(gameTable.getBootStartTime());
		} else {
			roundPO.setStatusEnum(RoundStatusEnum.IDLE);
			roundPO.setBootNumber(1);
			roundPO.setBootStartTime(startTime);
		}
		roundPO.setGameTableId(gameTable.getGameTablePO().getId());
		roundPO.setGameId(gameTable.getGameTablePO().getGameId());
		roundPO.setRoundNumber(0);
		roundPO.setResult("");
		roundPO.setBootId(_now + "-" + roundPO.getBootNumber());
		roundPO.setStartTime(startTime);
		
		response.setBid(roundPO.getBootId());
		response.setTid(gameTable.getGameTablePO().getId());
		response.setbNum(roundPO.getBootNumber());
		response.setNum(roundPO.getRoundNumber());
		response.setShowTableStatus(gameTable.getShowStatus());
	}
	
	public void newGame(CommandReqestPara params, SocketIOClient client,
			NewGameResponse response) {
		GameTable gameTable = BaccaratCache.getGameTableById(ConvertUtil.toInt(client.get(SocketClientStoreEnum.TABLE_ID.get())));
		
        if(gameTable==null){
            throw SocketException.createError("table.not.exist");
        }
        
//        if(gameTable.getRound().getRoundPO().getStatusEnum() == RoundStatusEnum.INACTIVE) {
//        	throw SocketException.createError("please.first.click.newboot");
//        }
        
        if(gameTable.getRound().getRoundPO().getStatusEnum() == RoundStatusEnum.IDLE 
        		|| gameTable.getRound().getRoundPO().getStatusEnum() == RoundStatusEnum.FINISH) {
        	Round currentRound = gameTable.getRound();
            if(currentRound == null)
            	throw SocketException.createError("round.not.exist");
            
            RoundPO currentRoundPO = currentRound.getRoundPO();
            if(currentRoundPO.getRoundNumber() >= BaccaratConfig.maxGameNum)
                throw SocketException.createError("round.number.too.many");
            
            gameTable.setInstantStateEnum(GameTableInstantStateEnum.NEWGAME);
            
            RoundPO newRoundPO = new RoundPO();
            Round newRound = new Round(newRoundPO);
            newRoundPO.setStatusEnum(RoundStatusEnum.NEWGAME);
            newRoundPO.setRoundNumber(currentRoundPO.getRoundNumber()+1);
            newRoundPO.setBootId(currentRoundPO.getBootId());
            newRoundPO.setGameId(currentRoundPO.getGameId());
            newRoundPO.setGameTableId(currentRoundPO.getGameTableId());
            newRoundPO.setBootNumber(currentRoundPO.getBootNumber());
            newRoundPO.setBootStartTime(currentRoundPO.getBootStartTime());
            newRoundPO.setStartTime(new Date());
            
            roundService.add(newRoundPO);
            
            RoundExtPO roundExtPO = new RoundExtPO();
            RoundExt roundExt = new RoundExt(roundExtPO);
            roundExtPO.setRoundId(newRoundPO.getId());
            roundExtService.add(roundExtPO);
            
            gameTable.setCards(null);
            gameTable.setRoundExt(roundExt);
            gameTable.setRound(newRound);
//            gameTable.initTradeItemBetMoneyMap();
            
            tableCommand.sendTableStatus(client);
            
            response.setBootId(newRoundPO.getBootId());
            response.setBootNumber(newRoundPO.getBootNumber());
            response.setRoundId(newRoundPO.getId());
            response.setRoundNumber(newRoundPO.getRoundNumber());
            response.setGameId(newRoundPO.getGameId());
            response.setRoundStatus(newRoundPO.getStatus());
            response.setGameTableId(newRoundPO.getGameTableId());
            response.setShowTableStatus(gameTable.getShowStatus());

            tableCommand.sendAllTablePlayer(response, RequestCommandEnum.DEALER_NEW_GAME, gameTable.getGameTablePO().getId());
    	} else {
        	throw SocketException.createError("table.status.abnormal");
        }
	}
	
	public void startBet(CommandReqestPara params, SocketIOClient client,
			StartBetResponse response) {
		GameTable table = BaccaratCache.getGameTableById(ConvertUtil.toInt(client.get(SocketClientStoreEnum.TABLE_ID.get())));
		
		if(table != null) {
			if(table.getRound().getRoundPO().getStatusEnum() == RoundStatusEnum.NEWGAME) {
				table.getRound().getRoundPO().setStatusEnum(RoundStatusEnum.BETTING);
				table.setInstantStateEnum(GameTableInstantStateEnum.BETTING);
				
				response.setTableStatus(GameTableInstantStateEnum.BETTING.get());
				response.setTableId(table.getGameTablePO().getId());
				if (table.getGameTablePO().getCountDownSeconds() != null) {
					table.setCoundDownDate(new Date());
				}
				response.setCountDown(table.getGameTablePO().getCountDownSeconds());
				response.setShowTableStatus(table.getShowStatus());
				
				table.setLimitRule(new CheckLimitRule());
				
				roundService.update(table.getRound().getRoundPO());
				tableCommand.sendAllTablePlayer(response, RequestCommandEnum.DEALER_START_BET, table.getGameTablePO().getId());
			} else {
				throw SocketException.createError("table.status.abnormal");
			}
		} else {
			throw SocketException.createError("table.not.exist");
		}
	}
	
	/**
     * 给虚桌其他用户推送下注信息。
     * @param client
     * @param user
     * @param total
     */
	protected void sendTableBetSuccess(SocketIOClient client, User user, Collection<User> userCollection, BetPara betPara, BigDecimal total) {
    	TableBetResponse tableBetSuccess = new TableBetResponse();
    	TableBetResponse.Bet tbet = new TableBetResponse.Bet();
        tbet.userId = user.getUserPO().getId();
        tbet.balance = user.getUserPO().getBalance();
        tbet.betAmount = total;
        tableBetSuccess.setBet(tbet);
        
        List<TableBetResponse.TradeInfo> tradeItemBetMoney = new ArrayList<>();
        TableBetResponse.TradeInfo tradeInfo;
        for(BetPara.Bet item : betPara.getBets()) {
        	tradeInfo = tableBetSuccess.new TradeInfo();
        	tradeInfo.item = item.tradeId;
        	tradeInfo.number = item.amount;
        	tradeItemBetMoney.add(tradeInfo);
        }
        tableBetSuccess.setTradeItemBetMoney(tradeItemBetMoney);
        
        roomCommand.send(userCollection, RequestCommandEnum.SERVER_BET_OTHER, tableBetSuccess);
    }
	
	/**
     * 检查当前桌的倒计时是否超时。如果超时，将向该桌的所有用户发送停止下注指令。
     * @param gameTable
     */
	protected void checkBetTimeout(GameTable gameTable) {
		Long timeLeft = Long.MAX_VALUE;
		if(gameTable.getCoundDownDate() != null) {
			timeLeft = gameTable.getGameTablePO().getCountDownSeconds() - (System.currentTimeMillis() - gameTable.getCoundDownDate().getTime()) / 1000;
		}
        if (gameTable.getGameTablePO().getCountDownSeconds().compareTo(timeLeft.intValue()) > 0 && timeLeft.intValue() < 0){
            throw SocketException.createError("bet.already.stop");
        }
    }
    
    /**
     * 该用户投注是否台红。
     * @param betPara
     * @param user
     * @param gameTable
     */
    protected void checkTableLimit(BetPara betPara, User user, GameTable gameTable) {
    	gameTable.getLimitRule().countMaxWinMoney(betPara.getBets(), gameTable.getGameTablePO().getMin(), gameTable.getGameTablePO().getMax());
    }
	
	/**
	 * 随机分配虚拟桌座位
	 */
	protected Integer randomSeat(Map<Integer, Long> userMap, User user,Integer tableId) {
		//清除无效用户
		for(Entry<Integer, Long> entry: userMap.entrySet()) {
			if(!BaccaratCache.isLoginByUserId(entry.getValue())) {
				userMap.remove(entry.getKey());
			}
		}
		//如果已存在桌子则回到原座位
		if(userMap.containsValue(user.getUserPO().getId())) {
			for(Entry<Integer,Long> item : userMap.entrySet()) {
				if(item.getValue().compareTo(user.getUserPO().getId()) == 0) {
					return item.getKey();
				}
			}
		} else {
			if(userMap.size() <= BaccaratConfig.seatNum) {
				for(int s: BaccaratConfig.seatNums) {
					if(!userMap.containsKey(s)) {
						userMap.put(s, user.getUserPO().getId());
						return s;
					}
				}
			}
		}
		throw SocketException.createError("table.person.enough",tableId);
	}
	
	/**
	 * 隐藏部分用户昵称
	 * @param nickName
	 * @return
	 */
	protected String hideNickName(String nickName) {
		if(nickName==null){
			return "";
		}
		if(nickName.length()>3){
			nickName = "***"+nickName.substring(nickName.length()-2, nickName.length());
		} else {
			int a = 5 - nickName.length();
			String str = "";
			for (int i = 0 ; i < a;i++) {
				str += "*";
			}
			nickName = str + nickName;
		}
		return nickName;
	}
	
	/**
	 * 根据卡牌信息生成游戏结果
	 * @param roundExt
	 * @return
	 */
	protected Map<PlayEnum,AbstractPlayRule> genResult(RoundExt roundExt) {
		Map<PlayEnum,AbstractPlayRule> resultMap = new HashMap<PlayEnum, AbstractPlayRule>();
		AbstractPlayRule normalPlayRule = new NormalPlayRule();
		normalPlayRule.getResultStr(roundExt.getRoundExtPO());
		normalPlayRule.getResultEnum();
		resultMap.put(PlayEnum.NORMAL, normalPlayRule);
		
		AbstractPlayRule freeCommissionPlayRule = new FreeCommissionPlayRule();
		freeCommissionPlayRule.setGameReuslt(normalPlayRule.getGameReuslt());
		freeCommissionPlayRule.getResultEnum();
		resultMap.put(PlayEnum.FREE_COMMISSION, freeCommissionPlayRule);
		
		AbstractPlayRule b27PlayRule = new B27PlayRule();
		b27PlayRule.setGameReuslt(normalPlayRule.getGameReuslt());
		b27PlayRule.getResultEnum();
		resultMap.put(PlayEnum.B27, b27PlayRule);
		return resultMap;
	}
	
	/**
	 * 获取展示 
	 *  p 闲牌形 b 庄牌形 最多3张牌
	 *	m:'H':'♥','D':'♦','C':'♣','S':'♠'
	 *	n: 1-13  1表示A   13 表示K
	 *	bpresult 该局结果  0庄 1闲 2和 3 庄、庄对 4 庄、闲对 5 和、庄对 6 和、闲对 7闲、庄对 8 闲、闲对 9庄、庄对、闲对  10 和、庄对、闲对 11 闲、庄对、闲对
	 *	result 位数
	 *	 *	1  	 	1庄 2闲 3和    点数输赢
	 *	 *	2		0无  1庄例牌  2闲例牌   3庄闲例牌
	 *	 *	3 		0无  1庄对  2闲对  3庄闲对
	 *	 *	4 		庄点数
	 *	betnums 靴数
	 * @param result
	 * @param round
	 * @param roundExt
	 * @return
	 */
	protected String getShowResult(String result, Round round, RoundExt roundExt) {
		Map<String,Object> resultMap = new HashMap<>();
		
		char first = result.charAt(0);
		char third = result.charAt(2);
		int bpresult = -1;
		if(first == '1') {
			if(third == '1') {
				bpresult = 3;
			} else if (third == '2') {
				bpresult = 4;
			} else if (third == '3') {
				bpresult = 9;
			} else {
				bpresult = 0;
			}
		} else if (first == '2') {
			if(third == '1') {
				bpresult = 7;
			} else if (third == '2') {
				bpresult = 8;
			} else if (third == '3') {
				bpresult = 11;
			} else {
				bpresult = 1;
			}
		} else if (first == '3') {
			if(third == '1') {
				bpresult = 5;
			} else if (third == '2') {
				bpresult = 6;
			} else if (third == '3') {
				bpresult = 10;
			} else {
				bpresult = 2;
			}
		} else {
			throw SocketException.createError("result.error");
		}
		
		resultMap.put("bpresult", bpresult);
		resultMap.put("betnums", round.getRoundPO().getBootNumber());
		List<Map<String,Object>> bankerCardList = new ArrayList<>();
		List<Map<String,Object>> playerCardList = new ArrayList<>();
		Map<String,Object> card = new HashMap<>();
		RoundExtPO roundExtPO = roundExt.getRoundExtPO();
		
		card.put("m", roundExtPO.getBankCard1Mode());
		card.put("n", roundExtPO.getBankCard1Number());
		bankerCardList.add(card);
		
		card = new HashMap<>();
		card.put("m", roundExtPO.getBankCard2Mode());
		card.put("n", roundExtPO.getBankCard2Number());
		bankerCardList.add(card);
		
		if(roundExtPO.getBankCard3Mode() != null) {
			card = new HashMap<>();
			card.put("m", roundExtPO.getBankCard3Mode());
			card.put("n", roundExtPO.getBankCard3Number());
			bankerCardList.add(card);
		}
		
		card = new HashMap<>();
		card.put("m", roundExtPO.getPlayerCard1Mode());
		card.put("n", roundExtPO.getPlayerCard1Number());
		playerCardList.add(card);
		
		card = new HashMap<>();
		card.put("m", roundExtPO.getPlayerCard2Mode());
		card.put("n", roundExtPO.getPlayerCard2Number());
		playerCardList.add(card);
		
		if(roundExtPO.getPlayerCard3Mode() != null) {
			card = new HashMap<>();
			card.put("m", roundExtPO.getPlayerCard3Mode());
			card.put("n", roundExtPO.getPlayerCard3Number());
			playerCardList.add(card);
		}
		
		resultMap.put("b", bankerCardList);
		resultMap.put("p", playerCardList);
		resultMap.put("result", result);
		return JSONObject.toJSONString(resultMap);
	}

	//计算用户总投注额
	protected BigDecimal calUserBetTotalMoney(List userBetMap) {
		BigDecimal totalBetMoney = BigDecimal.ZERO;
		if(userBetMap!=null){
//			for(BigDecimal item: userBetMap.values()){
//				totalBetMoney = totalBetMoney.add(item);
//			}
            return BigDecimal.ONE;
		}
		return totalBetMoney;
	}
	
}
