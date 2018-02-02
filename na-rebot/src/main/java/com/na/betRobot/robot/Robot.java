package com.na.betRobot.robot;

import com.alibaba.fastjson.JSONObject;
import com.na.betRobot.ServerClient;
import com.na.betRobot.cmd.para.*;
import com.na.betRobot.cmd.para.UserBetResultResponse;
import com.na.betRobot.config.QuartzConfig;
import com.na.betRobot.entity.LiveUser;
import com.na.betRobot.entity.UserChips;
import com.na.betRobot.entity.enums.VirtualGameTableType;
import com.na.betRobot.exception.RobotException;
import com.na.betRobot.task.RobotBetTask;
import com.na.betRobot.util.SpringContextUtil;
import com.na.test.batchbet.common.*;
import com.na.test.batchbet.para.*;
import com.na.test.batchbet.para.BetPara.Bet;
import com.na.test.batchbet.para.JoinRoomResponse.TradeItemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


/**
 * 机器人实体
 * 
 * @author alan
 * @date 2017年8月15日 下午4:12:40
 */
public class Robot implements RobotAction {
	
	private final static Logger log = LoggerFactory.getLogger(Robot.class);
	private static AtomicInteger beginMiSeat = new AtomicInteger(0);
	private static Map<Integer, AtomicInteger> normalSeat = new HashMap<Integer, AtomicInteger>();
	
	private LiveUser liveUser;
	
	private LoginInfoResponse loginInfoResponse;
	
	private JoinRoomResponse joinRoomResponse;
	
	private GameTableJson gameTableJson;
	
	private UserChips userChips;
	
	private Integer virtualTableId;
	//SelectRoom返回的虚拟桌总数
	private Integer totalVirtualCount;
	//虚拟房间页面计数
	private Integer totalVirtualPage = 1;
	//同一页房间计数
	private boolean changeRoom = false;
	//进入桌子方式
	private BetOrderSourceEnum source;
	
	//本局投注玩法
	private JoinRoomResponse.PlayInfo playInfo;
	//本剧投注项
	private JoinRoomResponse.TradeItemInfo tradeItemInfo;
	//只能进入的桌子
	private int[] robotGametables;
	private int changeTableMax = 100;
	private int changeTableMin = 0;
	
	private AtomicLong sn = new AtomicLong(0);
	private AtomicLong returnSn = new AtomicLong(0);
	private static AtomicLong totalReturnSn = new AtomicLong(0);
	private static long totalCostTime = 0;
	
	private long costTime = 0;
	
	public Robot(LiveUser liveUser,int[] robotGametables,String changeTableProbability) {
		this.liveUser = liveUser;
		this.robotGametables = robotGametables;
		if(changeTableProbability!=null) {
			String[] temp = changeTableProbability.split(":");
			changeTableMax = Integer.valueOf(temp[0]);
			changeTableMin = Integer.valueOf(temp[1]);
		}
	}
	
	@Override
	public void loginInfo(CommandResponse response, ServerClient client) {
		JSONObject body = (JSONObject) response.getBody();
		LoginInfoResponse loginInfoResponse = body.toJavaObject(LoginInfoResponse.class);
		
		this.loginInfoResponse = loginInfoResponse;
        log.debug( this.liveUser.getLoginName() + ",userId: {}尝试加入桌子……",loginInfoResponse.getUserId());
        sendJoin(client);
	}
	
	public void sendJoin(ServerClient client) {
		if(new Random().nextInt(100) > 90) {
			this.gameTableJson = null;
		}
		
		if(this.gameTableJson == null) {
			List<GameTableJson> gameTableList = this.loginInfoResponse.getGameTableList().stream().filter( item -> {
				boolean isExist = false;
				for(int tableId : robotGametables){
					isExist = tableId==item.getId();
					if(isExist)break;
				}
				return item.getGameId().compareTo(1) == 0 && isExist;
			}).collect(Collectors.toList());
			GameTableJson gameTable = gameTableList.get(new Random().nextInt(gameTableList.size()));
			if(gameTable == null) {
				throw new RobotException("没有可用桌子");
			}
			this.gameTableJson = gameTable;
			this.totalVirtualCount = null;
			this.totalVirtualPage = 1;
		}
		
		List<UserChips> userChipsList  = this.loginInfoResponse.getUserChips().stream().filter( item -> {
			return item.getGameId().compareTo(1) == 0;
		}).collect(Collectors.toList());
		UserChips userChips = userChipsList.get(new Random().nextInt(userChipsList.size()));
		if(userChips == null) {
			throw new RobotException("没有可用个人限红");
		}
		this.userChips = userChips;
		
		if(this.gameTableJson.getType().compareTo(GameTableTypeEnum.MI_BEING.get()) != 0 ) {
			SelectRoomPara selectRoomPara = new SelectRoomPara();
			selectRoomPara.setTableId(this.gameTableJson.getId());
			selectRoomPara.setTypeEnum(VirtualGameTableType.COMMON);
			if(this.totalVirtualCount != null) {
				int page = (totalVirtualCount % 12) == 0 ? totalVirtualCount / 12 : (totalVirtualCount / 12) + 1 ;
				if(page > this.totalVirtualPage++) {
					selectRoomPara.setPageNum(this.totalVirtualPage);
				} else {
					this.totalVirtualPage = 1;
					selectRoomPara.setPageNum(this.totalVirtualPage);
				}
			} else {
				this.totalVirtualPage = 1;
				selectRoomPara.setPageNum(1);
			}
			this.source = BetOrderSourceEnum.SEAT;
			client.send(RequestCommandEnum.SELECT_ROOM, selectRoomPara);
			
//			JoinRoomPara joinRoomPara = new JoinRoomPara();
//			joinRoomPara.setChipId(userChips.getId());
//			this.source = BetOrderSourceEnum.SEAT;
//			synchronized (normalSeat) {
//				if(normalSeat.containsKey(this.gameTableJson.getId())) {
//					if (normalSeat.get(this.gameTableJson.getId()).get() < 4) {
//						normalSeat.get(this.gameTableJson.getId()).incrementAndGet();
//					} else {
//						normalSeat.put(this.gameTableJson.getId(), new AtomicInteger(0));
//					}
//				} else {
//					normalSeat.put(this.gameTableJson.getId(), new AtomicInteger(1));
//				}
//			}
//			joinRoomPara.setSource(this.source);
//			joinRoomPara.setTableId(this.gameTableJson.getId());
//			
//			client.send(RequestCommandEnum.COMMON_JOIN_ROOM, joinRoomPara);
		} else {
			JoinRoomPara joinRoomPara = new JoinRoomPara();
			joinRoomPara.setChipId(userChips.getId());
			synchronized (beginMiSeat) {
				if (beginMiSeat.get() < 5) {
					beginMiSeat.incrementAndGet();
					this.source = BetOrderSourceEnum.SEAT;
				} else {
					this.source = BetOrderSourceEnum.SIDENOTE;
				}
			}
			joinRoomPara.setSource(this.source);
			joinRoomPara.setTableId(this.gameTableJson.getId());
			client.send(RequestCommandEnum.COMMON_JOIN_ROOM, joinRoomPara);
		}
	}

	@Override
	public void logout(CommandResponse response, ServerClient client) {
		client.disconnect();
		log.debug(this.liveUser.getLoginName() + "注销");
	}

	@Override
	public void join(CommandResponse response, ServerClient client) {
		JSONObject body = (JSONObject) response.getBody();
		JoinRoomResponse joinRoomResponse = body.toJavaObject(JoinRoomResponse.class);
		log.debug(this.liveUser.getLoginName() + "已经加入桌子:" + joinRoomResponse.getTableId());
		this.joinRoomResponse = joinRoomResponse;
	}

	@Override
	public void sendLogin(ServerClient client) {
		LoginInfoPara loginInfoPara = new LoginInfoPara();
        loginInfoPara.setLoginName(liveUser.getLoginName());
        loginInfoPara.setPwd("123456");
        loginInfoPara.setDeviceType(1);
        loginInfoPara.setDeviceInfo("robot");
        client.send(RequestCommandEnum.COMMON_LOGIN, loginInfoPara);
	}

	@Override
	public void startBet(CommandResponse response, ServerClient client) {
		com.alibaba.fastjson.JSONObject body = (com.alibaba.fastjson.JSONObject)response.getBody();
        StartBetResponse startBetResponse = body.toJavaObject(StartBetResponse.class);
        joinRoomResponse.setCountDown(startBetResponse.getCountDown());
        
		//选择本局投注玩法
        JoinRoomResponse.PlayInfo playInfo = joinRoomResponse.getPlayList().stream().filter(item -> {
        	return item.id == 1;
        }).findAny().orElse(null);
        if(playInfo == null) {
        	playInfo = joinRoomResponse.getPlayList().get(new Random().nextInt(joinRoomResponse.getPlayList().size()));
        }
		if(playInfo != null) {
			this.playInfo = playInfo;
		}
		log.debug(this.toString() + "【开始下注】 倒计时：" + startBetResponse.getCountDown());
		
		//根据下注时间  随机抽取固定秒数下注
		List<Integer> betSeconds = new ArrayList<>();
		for(int i = new Random().nextInt(7) + 3; i < startBetResponse.getCountDown(); i = i + new Random().nextInt(10) + 6) {
			betSeconds.add(i);
		}
		log.debug(this.toString() + "下注时间:" + betSeconds);
		SchedulerFactoryBean schedulerFactoryBean = SpringContextUtil.getBean(SchedulerFactoryBean.class);
		
		this.tradeItemInfo = null;
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("betSeconds", betSeconds);
		dataMap.put("robot", this);
		dataMap.put("client", client);
		QuartzConfig.addJob(schedulerFactoryBean, RobotBetTask.class, "RobotBetTask" + liveUser.getId(), "0/1 * * * * ?", dataMap);
	}
	
	public void bet(ServerClient client) {
		BetPara betPara = new BetPara();
		betPara.setChipsCid(this.userChips.getId());
		betPara.setBetType(BetOrderBetTypeEnum.COMMON.get());
		betPara.setSource(this.source.get());
		betPara.setTableId(this.gameTableJson.getId());
		
		List<Bet> bets = new ArrayList<>();

		//获取一个不超过个人最大限红的金额和交易项
		String[] moneys = this.userChips.getJtton().split(",");
		double money = 0;
		JoinRoomResponse.TradeItemInfo tradeItem = null;
		do {
			int rate = new Random().nextInt(100);
			
			if(rate >= 15) {
				List<TradeItemInfo> infoList = this.playInfo.tradeList.stream().filter( item -> {
					if(item.key.equals("B") || item.key.equals("P")) {
						return true;
					}
					return false;
				}).collect(Collectors.toList());
				if(infoList != null && !infoList.isEmpty()) {
					tradeItem = infoList.get(new Random().nextInt(infoList.size()));
					this.tradeItemInfo = tradeItem;
				}
				money = Double.valueOf(moneys[new Random().nextInt(moneys.length)]);
			} else if (rate <= 7) {
				tradeItem = this.playInfo.tradeList.stream().filter( item -> {
					if(item.key.equals("T")) {
							return true;
					}
					return false;
				}).findAny().orElse(null);
				money = Double.valueOf(moneys[new Random().nextInt(moneys.length)]);
			} else if (rate > 7 && rate <= 12) {
				List<TradeItemInfo> infoList = this.playInfo.tradeList.stream().filter( item -> {
					if(item.key.equals("BD") || item.key.equals("PD") || item.key.equals("BC") || item.key.equals("PC")) {
						return true;
					}
					return false;
				}).collect(Collectors.toList());
				if(infoList != null && !infoList.isEmpty()) {
					tradeItem = infoList.get(new Random().nextInt(infoList.size()));
				}
				String[] moneys2 = {"10","20","50","100","200","500"};
				money = Double.valueOf(moneys2[new Random().nextInt(moneys2.length)]);
			}else if (rate > 12 && rate < 15) {
				List<TradeItemInfo> infoList = this.playInfo.tradeList.stream().filter( item -> {
					if(item.key.equals("N6") || item.key.equals("B27")) {
						return true;
					}
					return false;
				}).collect(Collectors.toList());
				if(infoList != null && !infoList.isEmpty()) {
					tradeItem = infoList.get(new Random().nextInt(infoList.size()));
				}
				String[] moneys2 = {"10","20","50","100","200","500"};
				money = Double.valueOf(moneys2[new Random().nextInt(moneys2.length)]);
			}
			
			if(tradeItem == null) {
				log.debug("投注比例" + rate);
				tradeItem = this.playInfo.tradeList.get(new Random().nextInt(this.playInfo.tradeList.size()));
				money = Double.valueOf(moneys[new Random().nextInt(moneys.length)]);
			}
			
		}while (money*tradeItem.rate>=this.userChips.getMax());
		//下注
		Bet bet = new Bet();
		bet.tradeId = tradeItem.id;
		bet.amount = new BigDecimal(money);
		bets.add(bet);
		log.debug(this.liveUser.getLoginName() + "【投注】  交易项 :" + tradeItem.key + ",金额:" + bet.amount);
		
		betPara.setBets(bets);
		
		client.send(RequestCommandEnum.CLIENT_BET, betPara);
	}

	@Override
	public void stopBet(CommandResponse response, ServerClient client) {
		SchedulerFactoryBean schedulerFactoryBean = SpringContextUtil.getBean(SchedulerFactoryBean.class);
		QuartzConfig.removeJob(schedulerFactoryBean, "RobotBetTask" + liveUser.getId());
	}

	@Override
	public void gameResult(CommandResponse response, ServerClient client) {
		com.alibaba.fastjson.JSONObject body = (com.alibaba.fastjson.JSONObject)response.getBody();
		UserBetResultResponse userBetResultResponse = body.toJavaObject(UserBetResultResponse.class);
		log.debug("【结算】 " + this.liveUser.getLoginName());
		//加入一个随机值  小概率事件会自动换桌
		if(new Random().nextInt(changeTableMax) < changeTableMin) {
			log.debug("【自动换桌】 " + this.liveUser.getLoginName());
			try {
				Thread.sleep(new Random().nextInt(3000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			this.changeRoom = true;
			client.send(RequestCommandEnum.COMMON_LEAVE_ROOM, null);
		}
	}

	@Override
	public void userBalanceChange(CommandResponse response, ServerClient client) {
		com.alibaba.fastjson.JSONObject body = (com.alibaba.fastjson.JSONObject)response.getBody();
		UserBalanceChangeResponse userBetResultResponse = body.toJavaObject(UserBalanceChangeResponse.class);
		liveUser.setBalance(userBetResultResponse.getBalance());
	}

	@Override
	public void dealerLeaveTable(CommandResponse response, ServerClient client) {
		
	}

	@Override
	public void selectRoom(CommandResponse response, ServerClient client) {
		com.alibaba.fastjson.JSONObject body = (com.alibaba.fastjson.JSONObject)response.getBody();
		SelectRoomResponse selectRoomResponse = body.toJavaObject(SelectRoomResponse.class);
		SelectRoomResponse.VirtualTableInfo info = selectRoomResponse.getTableList().get(0);
		
		this.totalVirtualCount = selectRoomResponse.getTotal();
		
		int searchCount = 0;
		int maxPlayerNumber = 4;
		while(info.playerNumber.compareTo(maxPlayerNumber) > 0 && searchCount < 11) {
			if(!selectRoomResponse.getTableList().isEmpty() && selectRoomResponse.getTableList().size() > searchCount) {
				info = selectRoomResponse.getTableList().get(searchCount);
				searchCount++;
			} else {
				searchCount = 15;
			}
		}
		
		if(searchCount >= 11) {
			sendJoin(client);
		} else {
			
			this.virtualTableId = info.tableId;
			
			JoinRoomPara joinRoomPara = new JoinRoomPara();
			joinRoomPara.setChipId(userChips.getId());
			this.source = BetOrderSourceEnum.SEAT;
			joinRoomPara.setSource(this.source);
			joinRoomPara.setTableId(this.gameTableJson.getId());
			joinRoomPara.setVirtualTableId(info.tableId);
			
			client.send(RequestCommandEnum.COMMON_JOIN_ROOM, joinRoomPara);
		}
	}

	@Override
	public void userCard(CommandResponse response, ServerClient client) {
		com.alibaba.fastjson.JSONObject body = (com.alibaba.fastjson.JSONObject)response.getBody();
		UserCardResponse userCardResponse = body.toJavaObject(UserCardResponse.class);

		if(gameTableJson.getType().compareTo(GameTableTypeEnum.MI_BEING.get()) == 0 && userCardResponse.getIsMiCard()) {
			if(new Random().nextInt(100) > 30) {
				try {
					Thread.sleep(new Random().nextInt(userCardResponse.getCountDownSecond() - 8) + 5 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				MiCardOverPara miCardOverPara = new MiCardOverPara();
				List<Integer> indexs = new ArrayList<>();
				
				userCardResponse.getCardList().forEach( item -> {
					indexs.add(item.index);
				});
				
				miCardOverPara.setIndex(indexs);
				client.send(RequestCommandEnum.CLIENT_MICARD_OVER, miCardOverPara);
			}
		}
	}

	@Override
	public void sendHreatBeat(ServerClient client) {
		HeartBeatPara heartBeatPara = new HeartBeatPara();
		StringBuilder sb = new StringBuilder();
		heartBeatPara.setStartTimeStamp(System.currentTimeMillis());
		client.send(RequestCommandEnum.HEART_BEAT, heartBeatPara);
	}

	public LiveUser getLiveUser() {
		return liveUser;
	}

	@Override
	public void leaveRoom(CommandResponse response, ServerClient client) {
		com.alibaba.fastjson.JSONObject body = (com.alibaba.fastjson.JSONObject)response.getBody();
		LeaveRoomResponse userCardResponse = body.toJavaObject(LeaveRoomResponse.class);
		if(!changeRoom) {
			log.debug(this.liveUser.getLoginName() + "离开房间:" + this.liveUser.getTableId());
			client.send(RequestCommandEnum.COMMON_LOGOUT, null);
		} else {
			log.debug(this.liveUser.getLoginName() + "离开房间:" + this.liveUser.getTableId() + ",准备换桌");
			this.totalVirtualPage = 1;
			sendJoin(client);
			this.changeRoom = false;
		}
	}

	@Override
	public String toString() {
		return "Robot [" + liveUser.getLoginName() + "]";
	}
	
	
	
}
