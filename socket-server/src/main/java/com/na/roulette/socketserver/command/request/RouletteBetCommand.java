package com.na.roulette.socketserver.command.request;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderStatusEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderTableTypeEnum;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.baccarat.socketserver.entity.BetOrder;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.command.RouletteRequestCommandEnum;
import com.na.roulette.socketserver.command.requestpara.BetPara;
import com.na.roulette.socketserver.command.send.RouletteTableCommand;
import com.na.roulette.socketserver.command.sendpara.RouletteBetResponse;
import com.na.roulette.socketserver.common.limitrule.RouletteCheckLimitRule;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.roulette.socketserver.entity.RouletteRound;
import com.na.roulette.socketserver.entity.RouletteUser;
import com.na.roulette.socketserver.entity.RouletteUserBet;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.UserIsBetEnum;
import com.na.user.socketserver.common.enums.UserStatusEnum;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.entity.UserChipsPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IBetOrderService;
import com.na.user.socketserver.service.IUserService;


/**
 * 轮盘投注
 * @author Administrator
 *
 */
@Cmd(paraCls = BetPara.class,name = "轮盘投注")
@Component
@Transactional(rollbackFor = Exception.class)
public class RouletteBetCommand implements ICommand {
	
	@Autowired
	private IUserService userService;
	
	
	@Autowired
	private IBetOrderService betOrderService;
	
	@Autowired
	private RouletteTableCommand rouletteTableCommand;


	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		BetPara betPara = (BetPara) commandReqestPara;
		
		SocketException.isNull(betPara.getTableId(),"param.not.allow.empty");
        SocketException.isTrue(betPara.getBets()==null || betPara.getBets().isEmpty(),"please.bet");
        SocketException.isNull(betPara.getChipsCid()==null,"param.not.allow.empty");
        betPara.getBets().forEach(item->{
            if(item.amount.compareTo(BigDecimal.ZERO)<=0){
                throw SocketException.createError("param.not.allow.empty");
            }
        });
		
		RouletteGameTable table = RouletteCache.getGameTableById(betPara.getTableId());
		RouletteRound round = table.getRound();
        RoundPO roundPO = round.getRoundPO();
		RouletteUser user = RouletteCache.getUserByClient(client);
		
		if(user == null) {
        	UserPO userPO = AppCache.getUserByClient(client);
        	if(userPO == null){
        		throw SocketException.createError("user.not.exist");
        	}
        	user = new RouletteUser();
        	user.setUserPO(userPO);
        }
	
		SocketException.isTrue(roundPO.getStatusEnum()!= RoundStatusEnum.BETTING,"bet.already.stop");
		LiveUserPO userPO = (LiveUserPO) user.getUserPO();
        
        SocketException.isTrue(
        		userPO.getUserStatusEnum()==UserStatusEnum.LOCKED
                || userPO.getUserStatusEnum()==UserStatusEnum.FREEZE
                || userPO.getIsBetEnum()== UserIsBetEnum.LOCKED
                ,"user.not.allow.bet");
        SocketException.isTrue(
        		userPO.getUserTypeEnum()!=UserTypeEnum.REAL,
                "non.member.not.allow.bet"
        );
        
        if(((LiveUserPO)userPO).getIsBetEnum() != UserIsBetEnum.NORMAL) {
        	throw SocketException.createError("user.not.allow.bet");
        }
		//3.检测桌台限红
		//4.检测用户限红
        //5.检测最高赢额和最高余额
        //6.检测是否已经超时

        checkBetTimeout(table);

        checkUserEnoughBalance(betPara, user, table);

        //checkPersonMaxWinMoney(betPara,user);

		checkUserChip(betPara, user);

		checkTableLimit(betPara, user, table);

		BigDecimal total = saveBetOrder(betPara, user, roundPO, table,client);
		
		userService.updateUserBalance(userPO.getId(),BigDecimal.ZERO.subtract(total), "Bet");
        
        RouletteBetResponse betResponse = new RouletteBetResponse();
        betResponse.setUserId(userPO.getId());
        
        rouletteTableCommand.send(client, RouletteRequestCommandEnum.CLIENT_BET, betResponse);
		return true;
	}


    private BigDecimal saveBetOrder(BetPara params, RouletteUser user, RoundPO roundPO, RouletteGameTable table,SocketIOClient client) {
		LiveUserPO userPO = (LiveUserPO) user.getUserPO();
    	BigDecimal total = BigDecimal.ZERO;
		RouletteBetResponse betResponse = new RouletteBetResponse();
        betResponse.setUserId(userPO.getId());
        List<BetOrder> betOrderList = new ArrayList<>();
        for(BetPara.Bet bet : params.getBets()){
            TradeItem tradeItem = table.getTradeItemById(bet.key, bet.number);
            BetOrder betOrder = new BetOrder();
            betOrder.setBetIp(((InetSocketAddress)client.getRemoteAddress()).getAddress().getHostAddress());
            betOrder.setDeviceType(userPO.getDeviceType());
            betOrder.setDeviceInfo(userPO.getDeviceInfo());
            betOrder.setBootsId(roundPO.getBootId());
            betOrder.setGameId(roundPO.getGameId());
            betOrder.setGameTableId(roundPO.getGameTableId());
            betOrder.setBetTime(new Date());
            betOrder.setAmount(bet.amount);
            betOrder.setBetRate(tradeItem.getRate());
            betOrder.setBetType(params.getBetType());
            betOrder.setBootsNum(roundPO.getBootNumber());
            betOrder.setRoundId(roundPO.getId());
            betOrder.setRoundNum(roundPO.getRoundNumber());
            betOrder.setLoginName(userPO.getLoginName());
            betOrder.setUserId(userPO.getId());
            betOrder.setParentId(((LiveUserPO)userPO).getParentId());
            betOrder.setUserParentPath(((LiveUserPO)userPO).getParentPath());
            betOrder.setSource(BetOrderSourceEnum.SIDENOTE.get());
            betOrder.setTradeItemId(tradeItem.getId());
            betOrder.setTradeItemKey(tradeItem.getKey());
            betOrder.setPlayId(tradeItem.getPlayId());
            betOrder.setoPercentage(userPO.getIntoPercentage());
            betOrder.setUserPreBalance(userPO.getBalance());
            betOrder.setWashPercentage(userPO.getWashPercentage());
            betOrder.setStatus(BetOrderStatusEnum.CONFIRM.get());
            betOrder.setTableType(BetOrderTableTypeEnum.COMMON.get());
            betOrderList.add(betOrder);
            
            user.setTradeItemBetMoney(betOrder.getTradeItemId(),betOrder.getAmount());
            
            //用户投注细节
            RouletteUserBet userBetDetail = new RouletteUserBet();
            userBetDetail.setUid(userPO.getId());
            userBetDetail.setTableId(params.getTableId());
            userBetDetail.setTradeId(tradeItem.getId());
            userBetDetail.setRoundId(roundPO.getId());
            userBetDetail.setAmount(bet.amount);
            RouletteCache.addUserBetDetail(userBetDetail);

            userPO.setBalance(userPO.getBalance().subtract(betOrder.getAmount()));

            total = total.add(betOrder.getAmount());
            betResponse.addTradeItemId(params.getTableId());
        }
        betOrderService.batchAddBetOrders(userPO,betOrderList, table.getGameTablePO().getId(), roundPO.getId());
        return total;
	}
	

    /**
     * 检查用户限红。
     * @param betPara
     * @param user
     */
    private void checkUserChip(BetPara betPara, RouletteUser user) {
    	LiveUserPO liveUserPO = (LiveUserPO) user.getUserPO();
    	if(liveUserPO.getUserChipList() == null || liveUserPO.getUserChipList().size() < 1) {
    		throw SocketException.createError("please.select.chip");
    	}
        UserChipsPO chipPO = liveUserPO.getUserChipList().stream().filter(item->item.getId().equals(betPara.getChipsCid())).findFirst().get();
        if(user.getLimitRule() == null) {
        	user.setLimitRule(new RouletteCheckLimitRule());
        }
        user.getLimitRule().countUserChips(betPara.getBets(), chipPO, user);
    }
    
    /**
     * 该用户投注是否台红。
     * @param betPara
     * @param user
     * @param gameTable
     */
    private void checkTableLimit(BetPara betPara, RouletteUser user, RouletteGameTable gameTable) {
    	if(gameTable.getLimitRule() == null) {
    		gameTable.setLimitRule(new RouletteCheckLimitRule());
        }
    	gameTable.getLimitRule().countMaxWinMoney(betPara.getBets(), gameTable.getGameTablePO().getMin(), gameTable.getGameTablePO().getMax(),user);
    }
    /**
     * 检查用户的余额是否足够
     * @param betPara
     * @param user
     * @param gameTable
     */
    private void checkUserEnoughBalance(BetPara betPara, RouletteUser user, RouletteGameTable gameTable) {
        BigDecimal betTotal = BigDecimal.ZERO;
        for(BetPara.Bet bet : betPara.getBets()){
            betTotal = betTotal.add(bet.amount);
            if(betTotal.compareTo(user.getUserPO().getBalance())>0 || user.getUserPO().getBalance().compareTo(BigDecimal.ZERO)<0){
                throw SocketException.createError("user.balance.not.enough");
            }
        }
    }


    /**
     * 检查当前桌的倒计时是否超时。如果超时，将向该桌的所有用户发送停止下注指令。
     * @param gameTable
     */
    private void checkBetTimeout(RouletteGameTable gameTable) {
        Long timeLeft = gameTable.getGameTablePO().getCountDownSeconds() - (System.currentTimeMillis() - gameTable.getCoundDownDate().getTime()) / 1000;
        if (gameTable.getGameTablePO().getCountDownSeconds().compareTo(timeLeft.intValue()) > 0 && timeLeft.intValue() < 0){
            /*if (RouletteGameTableInstantStateEnum.BETTING == gameTable.getInstantStateEnum()) {
                RouletteRound round = gameTable.getRound();
                round.getRoundPO().setStatus(RoundStatusEnum.AWAITING_RESULT.get());
                roundService.update(round.getRoundPO());
            }*/
            throw SocketException.createError("bet.already.stop");
        }
    }

}
