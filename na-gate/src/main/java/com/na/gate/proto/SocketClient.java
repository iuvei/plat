package com.na.gate.proto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.na.gate.cache.GateCache;
import com.na.gate.common.GateConfig;
import com.na.gate.entity.AccountSyncRecord;
import com.na.gate.entity.PlatformUserAdapter;
import com.na.gate.enums.RedisKeyEnum;
import com.na.gate.enums.SocketConnEnum;
import com.na.gate.enums.SyncTypeEnum;
import com.na.gate.listen.MerchantListen;
import com.na.gate.listen.MessageListen;
import com.na.gate.proto.base.Request;
import com.na.gate.proto.bean.AccountConfirmSuccessRequest;
import com.na.gate.proto.bean.BetItem;
import com.na.gate.proto.bean.LivePlayerLoginRequest;
import com.na.gate.proto.bean.PlayerLoginRequest;
import com.na.gate.proto.bean.PlayerLogoutRequest;
import com.na.gate.proto.event.OnConnect;
import com.na.gate.proto.event.OnDisConnect;
import com.na.gate.proto.handler.GateInitializer;
import com.na.gate.remote.IGameUserRemote;
import com.na.gate.service.IAccountSyncRecordService;
import com.na.gate.service.IPlatformUserAdapterService;
import com.na.gate.service.IPlatformUserLoginService;
import com.na.gate.service.ISyncBetOrderFailRecordService;
import com.na.gate.util.DateUtil;
import com.na.gate.util.HttpUtil;
import com.na.gate.vo.BetOrderResponse;
import com.na.gate.vo.SendAccountJson;
import com.na.gate.vo.SendBetOrderJson;
import com.na.manager.bean.Page;
import com.na.manager.bean.vo.BetOrderVO;
import com.na.manager.entity.AccountRecord;
import com.na.manager.remote.FindBetOrderRequest;
import com.na.manager.remote.IUserRemote;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

/**
 * Created by sunny on 2017/7/21 0021.
 */
@Component
public class SocketClient extends Thread {
	private Logger logger = LoggerFactory.getLogger(SocketClient.class);
	private AsyncEventBus eventBuss;
	private ChannelHandlerContext ctx;
	@Value("${platform.server.host}")
	private String host;

	@Value("${platform.server.port}")
	private int port;

	@Value("${spring.na.handShake.gameId}")
	private int gameId;

	// 心跳发送失败次数重连服务器
	@Value("${spring.na.heartbeat.resend.connection.time}")
	private Integer reSendContime;
	@Autowired
	private GateConfig gateConfig;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private MessageListen messageListen;
	@Autowired
	private MerchantListen merchantListen;
	@Autowired
	private IUserRemote userRemote;
	@Autowired
	private IGameUserRemote gameUserRemote;
	@Autowired
	private IPlatformUserAdapterService platformUserAdapterService;
	@Autowired
	private IPlatformUserLoginService platformUserLoginService;
	@Autowired
	private IAccountSyncRecordService accountSyncRecordService;
	@Autowired
	private ISyncBetOrderFailRecordService syncBetOrderFailRecordService;
	private Bootstrap bootstrap;
	private EventLoopGroup workerGroup;

	private SocketConnEnum socketConn = SocketConnEnum.SUCCESS;

	private AtomicInteger connectionTime = new AtomicInteger(0);

	@PostConstruct
	public void init() {
		this.setName("socket-client");
		this.setDaemon(true);

		eventBuss = new AsyncEventBus("system_bus",
				Executors.newFixedThreadPool(50, new DefaultThreadFactory("sytem-event-bus")));
		eventBuss.register(this);
		messageListen.setClient(this);
		eventBuss.register(messageListen);
		eventBuss.register(merchantListen);
		initBootstrap();
		this.start();
	}

	@Override
	public void run() {
		connect();
	}

	private void connect() {
		try {
			logger.info("开始连接...");
			bootstrap.connect().addListener(new FutureListener<Void>() {
				@Override
				public void operationComplete(Future<Void> future) throws Exception {
					if (future.isSuccess()) {
						logger.info("连接大厅服务器成功,{}:{},服务状态：{}", host, port, ctx.channel().isActive());
						socketConn = SocketConnEnum.SUCCESS;
					} else {
						socketConn = SocketConnEnum.FAIL;
						logger.error("连接大厅服务器失败,{}:{}", host, port);
					}
				}
			}).channel().closeFuture().sync().awaitUninterruptibly();
			logger.info("socket连接状态：" + this.ctx.channel().isActive());
			if (!this.ctx.channel().isActive()) {
				socketConn = SocketConnEnum.FAIL;
				connectionTime.set(0);
				onDisConnect(null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			socketConn = SocketConnEnum.FAIL;
		} finally {
			// workerGroup.shutdownGracefully();
		}
	}

	private void initBootstrap() {
		workerGroup = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		try {
			bootstrap.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
					.remoteAddress(host, port).handler(new GateInitializer(eventBuss));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Subscribe
	public void onConnect(OnConnect onConnect) {
		this.ctx = onConnect.getCtx();
	}

	@Subscribe
	public void onDisConnect(OnDisConnect onDisConnect) {
		logger.info("当前连接状态:" + socketConn.getDesc());
		if (socketConn == SocketConnEnum.FAIL) {
			do {
				try {
					socketConn = SocketConnEnum.CONNECTING;
					logger.info("开始关闭连接...");
					workerGroup.shutdownGracefully().sync().awaitUninterruptibly(10 * 1000);
					logger.info("重新初始化...");
					initBootstrap();
					logger.info("开始重新连接...");
					connect();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				} finally {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
					}
				}
			} while (socketConn != SocketConnEnum.SUCCESS);
		}
	}

	public void send(Request request) {
		if (!this.ctx.channel().isActive()) {
			if (connectionTime.incrementAndGet() >= reSendContime) {
				logger.info("心跳发送失败次数过多，重新初始化...");
				connectionTime.set(0);
				if (socketConn == SocketConnEnum.FAIL) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							logger.info("正在发起断线重连...");
							onDisConnect(null);
							// eventBuss.post(new OnDisConnect(this.ctx));
						}
					}).start();
				}
			}
			logger.error("连接断开,指令发送失败.[{}],失败次数[{}]", request, connectionTime);
			return;
		}
		connectionTime.set(0);
		this.ctx.channel().writeAndFlush(request);
	}
	
	public void send(List<BetItem> list) {
		if (!this.ctx.channel().isActive()) {
			if (connectionTime.incrementAndGet() >= reSendContime) {
				logger.info("心跳发送失败次数过多，重新初始化...");
				connectionTime.set(0);
				if (socketConn == SocketConnEnum.FAIL) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							logger.info("正在发起断线重连...");
							onDisConnect(null);
							// eventBuss.post(new OnDisConnect(this.ctx));
						}
					}).start();
				}
			}
			logger.error("连接断开,指令发送失败.[{}],失败次数[{}]", list, connectionTime);
			return;
		}
		connectionTime.set(0);
		this.ctx.channel().writeAndFlush(list);
	}

	public AsyncEventBus getEventBuss() {
		return this.eventBuss;
	}

	/**
	 * 非大厅用户登录游戏(ex:机器人登录)
	 * 
	 * @param userId
	 */
	public void login(Long userId) {
		PlatformUserAdapter platformUserAdapter = platformUserAdapterService.findByLiveUserId(userId);
		if (platformUserAdapter == null) { // 非大厅用户发送加人指令
			logger.info("非大厅用户{}登录...", userId);
			LivePlayerLoginRequest request = new LivePlayerLoginRequest();
			request.setCount(1);
			send(request);
		}
	}

	/**
	 * 非大厅用户登录游戏(ex:机器人登录)
	 * 
	 * @param userId
	 */
	public void syncRobotOnline(Long number) {
		logger.info("覆盖更新机器人在线人数：" + number);
		LivePlayerLoginRequest request = new LivePlayerLoginRequest();
		request.setCount(number.intValue());
		request.setType(SyncTypeEnum.COVER.get());
		send(request);
	}

	/**
	 * 通知客户端登录游戏
	 * 
	 * @param userId
	 */
	public void clientLogin(Long userId) {
		logger.info("大厅用户{}开始登陆游戏...",userId);
		PlayerLoginRequest request = new PlayerLoginRequest();
		request.setUserId(userId);
		send(request);
	}

	public void loginOut(Long userId) {
		PlatformUserAdapter platformUserAdapter = platformUserAdapterService.findByLiveUserId(userId);
		if (platformUserAdapter == null) { // 非大厅用户发送减人指令
			logger.info("非大厅用户{}退出...", userId);
			LivePlayerLoginRequest request = new LivePlayerLoginRequest();
			request.setCount(-1);
			send(request);
			
//			gameUserRemote.lockUser(userId, 2);
//			logger.info("服务器触发机器人{}用户解锁。",userId);
		} else {
			Date date = platformUserLoginService.findBy(userId);
			if (null != date) {
//				List<AccountSyncRecord> accountSyncRecords=accountSyncRecordService.queryList(userId, DateUtil.date2Str(date));
//				Map<Long, AccountSyncRecord> accountRecordMap = new HashMap<>();
//				for(AccountSyncRecord item : accountSyncRecords){
//					accountRecordMap.put(item.getId(), item);
//				}
				logger.info("大厅用户[{}]开始退出...", platformUserAdapter.toString());
				List<AccountRecord> list = userRemote.findAccountRecordBy(date, userId);
				List<AccountRecord> accountRecords = new ArrayList<>();
				List<AccountSyncRecord> syncRecords = new ArrayList<>();
				AccountSyncRecord accountSyncRecord = null;
				JSONObject json =null;
				for (AccountRecord item : list) {
//					if(!accountRecordMap.containsKey(item.getId()) && item.getType() !=4){
						item.setSn("AZR" + item.getId());
						item.setUserId(Long.valueOf(platformUserAdapter.getPlatformUserId()));
						item.setCurrentBalance(item.getPreBalance().add(item.getAmount()));
						item.setCreatedAt(DateUtil.getTimestamp(item.getTime()));
						if (!StringUtils.isEmpty(item.getRemark()) && item.getType() != 1 && item.getType() != 2) {
							json = JSONObject.parseObject(item.getRemark());
							item.setTableId(json.getString("tableId"));
							item.setRoundId("BZR" + json.getString("roundId"));
						}
						accountRecords.add(item);
						
						accountSyncRecord = new AccountSyncRecord(platformUserAdapter.getLiveUserId(),item.getId());
						syncRecords.add(accountSyncRecord);
//					}
				}
				
				BigDecimal balance = userRemote.getLiveUser(userId).getBalance();
				SendAccountJson sendAccountJson = null;
				try {
					sendAccountJson = new SendAccountJson(0,accountRecords);
					sendAccountJson.setUserId(platformUserAdapter.getPlatformUserId());
					sendAccountJson.setTimestamp(DateUtil.getTimestamp(new Date()));
					sendAccountJson.setGameId(String.valueOf(gameId + 1));
					sendAccountJson.setGameKey(gateConfig.getPlatformGameKey());
					sendAccountJson.setExit(1);
					logger.info("用户{}退出游戏发送账单{}", platformUserAdapter.getPlatformUserName(),JSONObject.toJSONString(accountRecords));
					int i=0;
					do {
						Thread.sleep(1000*60*4);
						String result = HttpUtil.doPost(gateConfig.getSettlementUrl(), JSON.toJSONString(sendAccountJson));
						logger.info("推送账单返回结果：" + result);
						json = JSONObject.parseObject(result);
						if (json.getInteger("code") == 0) {
							logger.info("退出游戏时真人余额：{}",balance);
							AccountConfirmSuccessRequest confirmRequest = new AccountConfirmSuccessRequest();
							confirmRequest.setUserId(Long.valueOf(platformUserAdapter.getPlatformUserId()));
							onAccountConfirmSucess(confirmRequest);
							
							//flag=1 以推送
							userRemote.updataFlag(list);
							
							if(!CollectionUtils.isEmpty(syncRecords)){
								accountSyncRecordService.batchAdd(syncRecords);
							}
						}
						i++;
						Thread.sleep(1000);
					} while (i<5 && json.getInteger("code") != 0);
				} catch (Exception e) {
					logger.error("发送账单异常", e);
				}
			}else{
				gameUserRemote.lockUser(platformUserAdapter.getLiveUserId(), 2);
				logger.info("服务器触发大厅{}用户解锁。",platformUserAdapter.getPlatformUserName());
			}
		}
	}

	@Subscribe
	public void onAccountConfirmSucess(AccountConfirmSuccessRequest req) {
		PlatformUserAdapter platformUserAdapter = platformUserAdapterService.findBy(String.valueOf(req.getUserId()), 3);
		logger.info("大厅玩家账单确认成功[{}]", platformUserAdapter.toString());

		BigDecimal balance = userRemote.drawAllMoney(platformUserAdapter.getLiveUserId());
		PlayerLogoutRequest request = new PlayerLogoutRequest();
		request.setUserId(req.getUserId());
		send(request);
		
//		gameUserRemote.lockUser(platformUserAdapter.getLiveUserId(), 2);
//		logger.info("大厅用户{}正常退出解锁。",platformUserAdapter.getPlatformUserName());
		
		GateCache.WAIT_LOGOUT_USER_MAP.invalidate(platformUserAdapter.getLiveUserId());

		if (platformUserAdapter != null) {
			logger.info("用户{} 转出金额{}", platformUserAdapter.getPlatformUserName(), balance);
			// 删除登录记录
			platformUserLoginService.delete(platformUserAdapter.getLiveUserId());
			// 删除登录token
			stringRedisTemplate.delete(RedisKeyEnum.PLATFORM_GAME_USER_TOKEN.get(req.getUserId() + ""));
			logger.info("大厅用户退出成功[{}]。", platformUserAdapter.toString());
		}
	}

	/**
	 * 事件注册.
	 * 
	 * @param listen
	 */
	public void register(Object listen) {
		this.eventBuss.register(listen);
	}

	public boolean sendBetOrderByRoundId(Long roundId) {
		try {
			FindBetOrderRequest request = new FindBetOrderRequest();
			request.setRoundId(roundId);
			request.setParentId(gateConfig.getPlatformProxyUserRoot());
//			request.setPath(gateConfig.getPlatformProxyUserPath());
			request.setPath(gateConfig.getPlatformProxyUserPath()+","+gateConfig.getPlatformMerchantPath());
			request.setPageSize(2000);
			sendBetOrder(request);
			
//			request.setParentId(gateConfig.getPlatformMerchantRoot());
//			request.setPath(gateConfig.getPlatformMerchantPath());
//			sendBetOrder(request);
			
			sendAwardAccountRecord(roundId);
		} catch (Exception e) {
			logger.error("推送投注记录失败",e);
			return false;
		}
		return true;
	}
	
	public void sendAwardAccountRecord(Long roundId) {
		List<AccountRecord> accountRecords = userRemote.findAccountRecordByRoundId(roundId);
		List<AccountRecord> awardRecords =new ArrayList<>();
		PlatformUserAdapter platformUserAdapter =null;
		List<AccountSyncRecord> syncRecords = new ArrayList<>();
		AccountSyncRecord accountSyncRecord = null;
		JSONObject json = null;
		Set<Long> liverUserIds = new HashSet<>();
		for (AccountRecord item : accountRecords) {
			if(item.getType() ==4 || item.getType()==5){ //结算派彩流水
				platformUserAdapter = GateCache.PLATFORM_USER_ADAPTER_MAP.get(item.getUserId());
				if(platformUserAdapter==null){
					platformUserAdapter = platformUserAdapterService.findByLiveUserId(item.getUserId());
				}
				item.setSn("AZR" + item.getId());
				item.setUserId(Long.valueOf(platformUserAdapter.getPlatformUserId()));
				item.setCurrentBalance(item.getPreBalance().add(item.getAmount()));
				item.setCreatedAt(DateUtil.getTimestamp(item.getTime()));
				json = JSONObject.parseObject(item.getRemark());
				item.setTableId(json.getString("tableId"));
				item.setRoundId("BZR" + json.getString("roundId"));
				
				awardRecords.add(item);
				liverUserIds.add(platformUserAdapter.getLiveUserId());
				
				accountSyncRecord =new AccountSyncRecord(platformUserAdapter.getLiveUserId(),item.getId());
				syncRecords.add(accountSyncRecord);
			}
		}
		if(!CollectionUtils.isEmpty(awardRecords)){
			SendAccountJson sendAccountJson = null;
			try {
				sendAccountJson = new SendAccountJson(1, awardRecords);
				sendAccountJson.setUserId("");
				sendAccountJson.setTimestamp(DateUtil.getTimestamp(new Date()));
				sendAccountJson.setGameId(String.valueOf(gameId + 1));
				sendAccountJson.setGameKey(gateConfig.getPlatformGameKey());
				sendAccountJson.setExit(0);
				
				logger.info("投注发送派奖账单{}",JSONObject.toJSONString(awardRecords));
				int i=0;
				do {
					String result = HttpUtil.doPost(gateConfig.getSettlementUrl(), JSON.toJSONString(sendAccountJson));
					logger.info("推送账单返回结果：" + result);
					json = JSONObject.parseObject(result);
					if(json.getInteger("code") == 0 && !CollectionUtils.isEmpty(syncRecords)){
						for(Long userId : liverUserIds){
	                		if(GateCache.WAIT_LOGOUT_USER_MAP.asMap().containsKey(userId)){
	                			logger.info("异常退出用户列表1：[{}]",GateCache.WAIT_LOGOUT_USER_MAP.asMap().toString());
	                			// 判断用户是否存在未结算的订单
	                	    	long unsettlementOrder= userRemote.findUnsettlementOrder(userId);
	                	    	if(unsettlementOrder==0){
	                	    		// 向大厅发送退出指令
	                	    		loginOut(Long.valueOf(userId));
	                	    		logger.info("异常退出用户列表2：[{}]",GateCache.WAIT_LOGOUT_USER_MAP.asMap().toString());
	                	    	}
	                		}
	                	}
						accountSyncRecordService.batchAdd(syncRecords);
						
						userRemote.updataFlag(accountRecords);
					}else{
						// 处理异常情况
						if(!GateCache.ROUNDSET.contains(roundId)){
							GateCache.ROUNDSET.add(roundId);
						}
					}
					i++;
					Thread.sleep(3000);
				} while (i<5 && json.getInteger("code") != 0);
				
			} catch (Exception e) {
				logger.error("发送账单异常", e);
			}
		}
	}
	
	/**
	 * 实时发送投注账单
	 * @param path
	 * @param content
	 * @return
	 */
	public boolean sendBetAccountRecord(String path, String content){
		String userIds[] =path.split("/");
		long userId = Long.valueOf(userIds[userIds.length-1]);
		PlatformUserAdapter platformUserAdapter = GateCache.PLATFORM_USER_ADAPTER_MAP.get(userId);
		if(platformUserAdapter ==null){
			platformUserAdapter = platformUserAdapterService.findByLiveUserId(userId);
		}
		List<AccountRecord> accountRecords = JSONObject.parseArray(content, AccountRecord.class);
		List<AccountSyncRecord> syncRecords = new ArrayList<>();
		AccountSyncRecord accountSyncRecord = null;
		JSONObject json = null;
		for (AccountRecord item : accountRecords) {
			item.setSn("AZR" + item.getId());
			item.setUserId(Long.valueOf(platformUserAdapter.getPlatformUserId()));
			item.setCurrentBalance(item.getPreBalance().add(item.getAmount()));
			item.setCreatedAt(DateUtil.getTimestamp(item.getTime()));
			if (!StringUtils.isEmpty(item.getRemark()) && item.getType() != 1 && item.getType() != 2) {
				json = JSONObject.parseObject(item.getRemark());
				item.setTableId(json.getString("tableId"));
				item.setRoundId("BZR" + json.getString("roundId"));
			}
			accountSyncRecord =new AccountSyncRecord(platformUserAdapter.getLiveUserId(),item.getId());
			syncRecords.add(accountSyncRecord);
		}
		SendAccountJson sendAccountJson = null;
		try {
			sendAccountJson = new SendAccountJson(1, accountRecords);
			sendAccountJson.setUserId(platformUserAdapter.getPlatformUserId());
			sendAccountJson.setTimestamp(DateUtil.getTimestamp(new Date()));
			sendAccountJson.setGameId(String.valueOf(gameId + 1));
			sendAccountJson.setGameKey(gateConfig.getPlatformGameKey());
			sendAccountJson.setExit(0);

			logger.info("path{}用户{}投注发送账单{}", path, platformUserAdapter.getPlatformUserName(),
					JSONObject.toJSONString(accountRecords));
			
			int i=0;
			do {
				String result = HttpUtil.doPost(gateConfig.getSettlementUrl(), JSON.toJSONString(sendAccountJson));
				logger.info("推送账单返回结果：" + result);
				json = JSONObject.parseObject(result);
				if(json.getInteger("code") == 0){
					userRemote.updataFlag(accountRecords);
					
					accountSyncRecordService.batchAdd(syncRecords);
				}
				i++;
				Thread.sleep(1000);
			} while (i<5 && json.getInteger("code") != 0);
			
			return true;
		} catch (Exception e) {
			logger.error("发送账单异常", e);
			return false;
		}
	}

	/**
	 * 推送荷官打卡记录
	 * 
	 * @param dataJson
	 */
	public void sendDealerClassRecord(String dataJson) {
		logger.info("荷官打卡记录：" + dataJson);
	}

	/**
     * 定时给大厅发送交易记录。
     */
    public boolean sendBetOrder(FindBetOrderRequest request){
    	boolean flag =true;
    	JSONObject json = null;
        for (int i=1;;i++) {
            try {
                request.setCurrentPage(i);
                Page<BetOrderVO> page = userRemote.findBetOrder(request);
                Set<Long> liverUserIds = new HashSet<>();
                List<PlatformUserAdapter> adapterList =new ArrayList<>();
                if(page==null ||  page.getData() ==null || CollectionUtils.isEmpty(page.getData())){
                	break;
                }
                for (BetOrderVO item: page.getData()) {
                	item.setGameId(gameId+item.getGameId());
                    if(GateCache.PLATFORM_USER_ADAPTER_MAP.containsKey(item.getUserId())){
                    	adapterList.add(GateCache.PLATFORM_USER_ADAPTER_MAP.get(item.getUserId()));
                    }else{
                    	liverUserIds.add(item.getUserId());
                    }
				}
                if(!CollectionUtils.isEmpty(liverUserIds)){
                	// 从数据库查询缓存中不存在的玩家数据
                	List<PlatformUserAdapter> adapterTempList = platformUserAdapterService.findByLiverUserIds(liverUserIds);
                	adapterList.addAll(adapterTempList);
                	adapterTempList.forEach(item->{
                		GateCache.PLATFORM_USER_ADAPTER_MAP.put(item.getLiveUserId(), item);
                	});
                }
                List<BetOrderResponse> list = new ArrayList<>();
                page.getData().forEach(item->{
                    BetOrderResponse bet = new BetOrderResponse();
                    BeanUtils.copyProperties(item,bet);
                    try {
                    	bet.setBetTime(DateUtil.getTimestamp(item.getBetTime()));
                    	bet.setSettleTime(DateUtil.getTimestamp(item.getSettleTime()));
                    } catch (Exception e) {
                    	e.printStackTrace();
                    }
                    Optional<PlatformUserAdapter>  opt = adapterList.stream().filter(p->{return p.getLiveUserId().equals(item.getUserId());}).findFirst();
                    if (opt.isPresent()){
                        PlatformUserAdapter adapter = opt.get();
                        bet.setUserId(adapter.getPlatformUserId());
                        bet.setParentId(adapter.getPlatformParentId());
                    }
                    list.add(bet);
                });
                SendBetOrderJson betOrderJson = new SendBetOrderJson(list,DateUtil.getTimestamp(new Date()),String.valueOf(gameId));
                betOrderJson.setGameKey(gateConfig.getPlatformGameKey());
                
                int time=0;
    			do {
    				String result =HttpUtil.doPost(gateConfig.getRecordUrl(), JSON.toJSONString(betOrderJson));
                    logger.info("推送游戏战绩返回结果："+result);
    				json = JSONObject.parseObject(result);
    				
    				time++;
    				Thread.sleep(3000);
    			} while (time<5 && json.getInteger("code") != 0);
                
                if(json.getInteger("code")!=0){
                	flag =false;
                	syncBetOrderFailRecordService.add(request.getRoundId());
                	logger.info("推送失败游戏局号{}入库",request.getRoundId());
                }else{
                	// 发送神秘大奖数据
                	List<BetItem> dataList = betOrderJson.getBetUserList(list);
                	int times = dataList.size()%1000==0?dataList.size()/1000:dataList.size()/1000+1;
                	for (int j = 1; j <= times; j++) {
                		send(dataList.subList((j-1)*1000, j*1000>dataList.size()?dataList.size():j*1000));
					}
                }
            }catch (Exception e){
            	e.printStackTrace();
                logger.error(e.getMessage(),e);
                flag =false;
                syncBetOrderFailRecordService.add(request.getRoundId());
            	logger.info("推送失败游戏局号{}入库",request.getRoundId());
                break;
            }
        }
        return flag;
    }

	public static void main(String[] args) throws Exception {
		List<BetOrderResponse> list = new ArrayList<>();
		for (int i = 0; i < 1; i++) {
			BetOrderResponse bo = new BetOrderResponse();
			bo.setBetId((332857510723584000L + i) + "");
			bo.setAddition("test");
			bo.setAgentName("test001");
			bo.setAmount(new BigDecimal("10000"));
			bo.setBetAmount(new BigDecimal("20000"));
			bo.setBetNum("T");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			TimeZone zone = TimeZone.getTimeZone("GMT+08:00"); // 获取中国时区
			sdf.setTimeZone(zone);
			bo.setBetTime(sdf.parse("2017-11-10 10:00:00").getTime());
			bo.setSettleTime(sdf.parse("2017-11-10 10:00:00").getTime());
			bo.setBetTypeId("2");
			bo.setBootsNum("20170710-1");
			bo.setGameId(2);
			bo.setGameName("百家乐");
			bo.setGameTable("m305");
			bo.setRoundNum(333 + "");
			bo.setItemKey("sdfsfsaf");
			bo.setSettleRate(new BigDecimal("2.3"));
			bo.setUserId("sdfadfasdf");
			bo.setUserName("asdfasdfasdfasdfasfdasfdasfdasdfasdf");
			bo.setResult(
					"{\"p\":[{\"m\":\"S\",\"n\":13},{\"m\":\"C\",\"n\":4},{\"m\":\"H\",\"n\":8}],\"result\":\"1006\",\"bpresult\":0,\"b\":[{\"m\":\"C\",\"n\":3},{\"m\":\"S\",\"n\":8},{\"m\":\"S\",\"n\":5}],\"betnums\":2}");
			list.add(bo);
		}
//		String s = JSONObject.toJSONString(list);
//		System.out.println(s);
//		SendBetOrderJson sendBetOrderJson = new SendBetOrderJson(list);
//		System.out.println(JSONObject.toJSON(sendBetOrderJson));
		// String s1 =
		// "{\"msg\":\"ok\",\"type\":\"join\",\"body\":{\"chipId\":1,\"isQuickChange\":false,\"nickName\":\"erwer1\",\"playList\":[{\"id\":1,\"name\":\"普通台\",\"tradeList\":[{\"id\":101,\"key\":\"B\",\"name\":\"庄\",\"rate\":0.95},{\"id\":102,\"key\":\"P\",\"name\":\"闲\",\"rate\":1.0},{\"id\":103,\"key\":\"T\",\"name\":\"和\",\"rate\":8.0},{\"id\":104,\"key\":\"BD\",\"name\":\"庄对\",\"rate\":11.0},{\"id\":105,\"key\":\"PD\",\"name\":\"闲对\",\"rate\":11.0},{\"id\":110,\"key\":\"BC\",\"name\":\"庄例\",\"rate\":3.5},{\"id\":111,\"key\":\"PC\",\"name\":\"闲例\",\"rate\":3.5}]},{\"id\":2,\"name\":\"免佣台\",\"tradeList\":[{\"id\":201,\"key\":\"B\",\"name\":\"庄\",\"rate\":1.0},{\"id\":202,\"key\":\"P\",\"name\":\"闲\",\"rate\":1.0},{\"id\":203,\"key\":\"T\",\"name\":\"和\",\"rate\":8.0},{\"id\":204,\"key\":\"BD\",\"name\":\"庄对\",\"rate\":11.0},{\"id\":205,\"key\":\"PD\",\"name\":\"闲对\",\"rate\":11.0},{\"id\":206,\"key\":\"N6\",\"name\":\"免佣6\",\"rate\":12.0}]},{\"id\":3,\"name\":\"B27\",\"tradeList\":[{\"id\":301,\"key\":\"B\",\"name\":\"庄\",\"rate\":1.0},{\"id\":302,\"key\":\"P\",\"name\":\"闲\",\"rate\":1.0},{\"id\":303,\"key\":\"T\",\"name\":\"和\",\"rate\":8.0},{\"id\":304,\"key\":\"BD\",\"name\":\"庄对\",\"rate\":11.0},{\"id\":305,\"key\":\"PD\",\"name\":\"闲对\",\"rate\":11.0},{\"id\":306,\"key\":\"B27\",\"name\":\"B27\",\"rate\":12.0}]}],\"seatList\":[{\"balance\":10751.25,\"nickName\":\"***er1\",\"seat\":1,\"userId\":81,\"userPicture\":\"https://s3-ap-southeast-1.amazonaws.com/nalive-test/head-portrait/7666780826804a958fd6a0690744d98f.png\"}],\"seatNum\":1,\"tid\":303,\"type\":\"1\",\"userBetList\":[]},\"signMsg\":\"9734837000d996fcd4af77e8048e0189\"}";
		// String s2 =
		// Base64Utils.encodeToString(ZLibUtils.compress(s1.getBytes()));
		// FileCopyUtils.copy(ZipUtils.compress(s1.getBytes()),new
		// File("d:/tt.zip"));
		// System.out.println(s1.getBytes().length+"-----"+s2.length());
		// System.out.println(s2);
		// System.out.println(new
		// String(ZipUtils.decompress(ZipUtils.compress(s1.getBytes())),"utf-8"));
		//
		// System.out.println(Base64Utils.encodeToString(ZLibUtils.compress("abc".getBytes("utf-8"))));
		
		List<BetItem> betList = new ArrayList<>();
		for (int i = 0; i < 48; i++) {
			betList.add(new BetItem(Long.valueOf(i),Double.valueOf(Long.valueOf(i))));
		}
		
		int times = betList.size()%10==0?betList.size()/10:betList.size()/10+1;
		System.out.println(times);
    	for (int j = 1; j <= times; j++) {
    		System.out.println(j*10>betList.size()?betList.size():j*10);
    		List<BetItem> d=betList.subList((j-1)*10, j*10>betList.size()?betList.size():j*10);
    		for (BetItem item : d) {
    			System.out.println(item.getUserId()+"--"+item.getBetTotalAmount());
			}
    		System.out.println("-------------------------");
		}
	}

}
