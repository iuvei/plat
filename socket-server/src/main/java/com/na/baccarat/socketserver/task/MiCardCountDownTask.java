package com.na.baccarat.socketserver.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.send.DealerCommand;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.sendpara.SendDealerResponse;
import com.na.baccarat.socketserver.command.sendpara.UserCardResponse;
import com.na.baccarat.socketserver.command.sendpara.UserCardResponse.CardInfo;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.User;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.entity.GamePO;
import com.na.user.socketserver.util.ConvertUtil;
import com.na.user.socketserver.util.SocketUtil;

/**
 * 咪牌倒计时任务
 * 
 * @author alan
 * @date 2017年5月5日 下午3:12:04
 */
@Component
public class MiCardCountDownTask implements Job {
	
	private Logger log = LoggerFactory.getLogger(MiCardCountDownTask.class);
	
	@Autowired
	private SocketIOServer socketIOServer;
	
	@Autowired
	private DealerCommand dealerCommand;
	
	@Autowired
	private RoomCommand roomCommand;

	
	@Override
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.debug("执行咪牌倒计时线程");
		JobDataMap dataMap = arg0.getJobDetail().getJobDataMap();
		
		List<UserCardResponse.CardInfo> bankCardList = (List<CardInfo>) dataMap.get("bankCardList");
		List<UserCardResponse.CardInfo> playCardList = (List<CardInfo>) dataMap.get("playCardList");
		SocketIOClient dealerClient = (SocketIOClient) dataMap.get("dealerClient");
		HashSet<User> bankUserList =  (HashSet<User>) dataMap.get("bankUserList");
		HashSet<User> playUserList =  (HashSet<User>) dataMap.get("playUserList");
		GameTable table = (GameTable) dataMap.get("table");
		
		this.sendOpenCard(table, playCardList, playUserList, dealerClient);
		this.sendOpenCard(table, bankCardList, bankUserList, dealerClient);
	}
	
	public void sendOpenCard(GameTable table, List<UserCardResponse.CardInfo> cardList, HashSet<User> userList, SocketIOClient dealerClient) {
		GamePO gamePO = AppCache.getGame(table.getGameTablePO().getGameId());
		//获取延时发送秒数
		int delaySecond = ConvertUtil.toInt(gamePO.getGameConfig().get("twodelayTime"), 5);
		
		List<UserCardResponse.CardInfo> copyCardList = new ArrayList<>(cardList);
		//设置开牌
		int flag = 0;
		for(int i = 0; i < copyCardList.size() ; i++) {
			if(!table.getCards()[copyCardList.get(i).index].getOpenStatus()) {
				table.getCards()[copyCardList.get(i).index].setOpenStatus(true);
			} else {
				cardList.remove(i - flag++);
			}
		}
		
		//通知用户开牌
		if(cardList.size() > 0) {
			try {
				UserCardResponse userCardResponse = new UserCardResponse();
				userCardResponse.setCardList(cardList);
				userCardResponse.setTableId(table.getGameTablePO().getId());
				
				new Thread( new Runnable() {
					@Override
					public void run() {
						try {
							Set<User> allTableUserList = BaccaratCache.getTableUserList(table.getGameTablePO().getId());
							Thread.sleep(delaySecond * 1000);
							Set<User> notMiUserList = allTableUserList.stream().filter( item -> {
								return !userList.contains(item);
							}).collect(Collectors.toSet());
							Collection<SocketIOClient> userClients = SocketUtil.getClientByUserList(socketIOServer, notMiUserList);
							for (SocketIOClient item : userClients) {
								roomCommand.send(item, RequestCommandEnum.CLIENT_USER_CARD, userCardResponse);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
				
			}catch (Exception e){
				log.error(e.getMessage(),e);
			}
			
			//通知荷官开牌
			SendDealerResponse sendResponse = new SendDealerResponse();
			List<Integer> indexList = new ArrayList<>();
			for(UserCardResponse.CardInfo cardInfo : cardList) {
				indexList.add(cardInfo.index);
			}
			
			sendResponse.setCommand("fanpai");
			sendResponse.setCardList(indexList);
			dealerCommand.sendMessage(dealerClient, sendResponse);
		}
	}

}
