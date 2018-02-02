package com.na.gate.listen;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.na.gate.cache.GateCache;
import com.na.gate.common.GateConfig;
import com.na.gate.common.HandShakeConfig;
import com.na.gate.entity.PlatformUserAdapter;
import com.na.gate.enums.RedisKeyEnum;
import com.na.gate.enums.ReponseStatusEnum;
import com.na.gate.proto.SocketClient;
import com.na.gate.proto.bean.CreatePlayerResult;
import com.na.gate.proto.bean.ExceptionLoginoutRequest;
import com.na.gate.proto.bean.ExceptionResponse;
import com.na.gate.proto.bean.HandShakeRequest;
import com.na.gate.proto.bean.HeartbeatRequest;
import com.na.gate.proto.bean.MerchantJson;
import com.na.gate.proto.bean.MerchantRequest;
import com.na.gate.proto.bean.PengMerchantQueueResponse;
import com.na.gate.proto.bean.PlayerData;
import com.na.gate.proto.event.OnConnect;
import com.na.gate.proto.event.OnDisConnect;
import com.na.gate.remote.IGameUserRemote;
import com.na.gate.service.IPlatformUserAdapterService;
import com.na.gate.service.IPlatformUserLoginService;
import com.na.gate.vo.PlatformUserToken;
import com.na.manager.remote.IUserRemote;

/**
 * Created by sunny on 2017/7/26 0026.
 */
@Component
public class MessageListen {
    private Logger logger = LoggerFactory.getLogger(MessageListen.class);
    private SocketClient client;

    @Autowired
    private IPlatformUserAdapterService platformUserAdapterService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IUserRemote userRemote;

    @Autowired
    private IPlatformUserLoginService platformUserLoginService;
    @Autowired
    private HandShakeConfig handShakeConfig;
    @Autowired
    private GateConfig gateConfig;
    
    private ReadWriteLock LOCK_CURRENT_PLAYER_CACHE = new ReentrantReadWriteLock();
    @Autowired
    private IGameUserRemote gameUserRemote;

    private Timer heartTimer = new Timer();
    
    //是否正在发送心跳
    private volatile boolean isSending = false;

    @Subscribe
    public void OnConnect(OnConnect onConnect){
        logger.info("服务器连接上……");
        HandShakeRequest req = new HandShakeRequest();
        req.setAuthCode(handShakeConfig.getAuthCode());
        req.setGameId(handShakeConfig.getGameId());
        req.setGameNameCn(handShakeConfig.getGameNameCn());
        req.setGameNameEn(handShakeConfig.getGameNameEn());
        req.setIp(handShakeConfig.getSocketServerIp());
        req.setPort(handShakeConfig.getSocketServerPort());
        req.setServerId(handShakeConfig.getServerId());
        req.setServerName(handShakeConfig.getServerName());
        client.send(req);

        HeartbeatRequest heartbeat = new HeartbeatRequest();
        
        if(!isSending){
        	isSending = true;
	        heartTimer.scheduleAtFixedRate(new java.util.TimerTask() {
	            @Override
	            public void run() {
	                client.send(heartbeat);
	            }
	        },1000, gateConfig.getHeartbeatTime());
        }
    }

    @Subscribe
    public void onException(ExceptionResponse response){
        logger.info("指令{}发生异常:{}",response.getCmd(),response.getMsg());
    }

    @Subscribe
    public void onDisConnect(OnDisConnect onDisConnect){
        heartTimer.purge();
    }

    @Subscribe
    public void onPlayerData(PlayerData req){
    	logger.info("onPlayerData:"+JSONObject.toJSONString(req));
    	try {
    		createPlayer(req);
		} catch (Exception e) {
			logger.error("创建玩家账号异常。",e);
			CreatePlayerResult response = new CreatePlayerResult(req.getUserId(), ReponseStatusEnum.ERROR.get(),ReponseStatusEnum.ERROR.getDesc());
        	client.send(response);
		}
    }
    
    private void createPlayer(PlayerData req){
    	MerchantJson merchantRequest = new MerchantJson();
        merchantRequest.setId(req.getUserId()+"");
        merchantRequest.setRole("10000");
        merchantRequest.setHeadPic(req.getHeadPic());
        merchantRequest.setNickname(req.getNickName());
        merchantRequest.setParentId(req.getParentId());
        merchantRequest.setUsername(req.getUserName());
        if(req.getLiveMix()==null || "-1".equals(req.getLiveMix())){
        	merchantRequest.setLiveMix(BigDecimal.ZERO);
        }else{
        	merchantRequest.setLiveMix(new BigDecimal(req.getLiveMix()));
        }
        PlatformUserAdapter parentAdapter = platformUserAdapterService.findByMerchantUserId(req.getParentId());
        if(parentAdapter ==null){
        	logger.info("{}上级不存在。",req.getUserName());
    		if(GateCache.pindingPlayerCache.asMap().containsKey(req.getParentId())){
    			try {
            		LOCK_CURRENT_PLAYER_CACHE.writeLock().lock();;
            		GateCache.pindingPlayerCache.getIfPresent(req.getParentId()).add(req);
    			} catch (Exception e) {
            		logger.error(e.getMessage());
            	}finally{
            		LOCK_CURRENT_PLAYER_CACHE.writeLock().unlock();
            	}
        	}else{
        		List<PlayerData> playerList = new ArrayList<>();
        		playerList.add(req);
        		try {
            		LOCK_CURRENT_PLAYER_CACHE.writeLock().lock();;
            		GateCache.pindingPlayerCache.put(req.getParentId(), playerList);
            	} catch (Exception e) {
            		logger.error(e.getMessage());
            	}finally{
            		LOCK_CURRENT_PLAYER_CACHE.writeLock().unlock();
            	}
        	}
        	
        	// 请求上级商户信息
        	MerchantRequest request = new MerchantRequest();
    		request.setJson(req.getParentId());
        	client.send(request);
        }else{
	        PlatformUserAdapter adapter = platformUserAdapterService.add(merchantRequest);
//	        LiveUser liveUser = userRemote.getLiveUser(adapter.getLiveUserId());
//	        if(liveUser.getUserStatus() != UserStatus.ENABLED.get()){
//				CreatePlayerResult response = new CreatePlayerResult(req.getUserId(), ReponseStatusEnum.ERROR.get(),"用户已被锁定，请稍后重试！");
//	        	client.send(response);
//	        }else{
	        	if("1".equals(GateCache.platMaintenance)){
					CreatePlayerResult response = new CreatePlayerResult(req.getUserId(), ReponseStatusEnum.ERROR.get(),"平台例行维护，请您耐心等待！");
		        	client.send(response);
	        	}else{
			        GateCache.WAIT_LOGOUT_USER_MAP.invalidate(adapter.getLiveUserId());
			        logger.info("等待异常退出玩家列表：{}",GateCache.WAIT_LOGOUT_USER_MAP.asMap().toString());
			        
			        Date date = platformUserLoginService.findBy(adapter.getLiveUserId());
			        //已清算，记录登录时间，已大厅金额为准
					if(req.getFlag()==1){
				        BigDecimal balance = new BigDecimal(req.getBalance());
				        if(balance.compareTo(BigDecimal.ZERO)>0){
				            userRemote.saveMoney(adapter.getLiveUserId(),balance);
				            logger.info("真人玩家{} 存入金额{}",adapter.getPlatformUserName(),balance);
				        }
				        if(date!=null){
				        	platformUserLoginService.delete(adapter.getLiveUserId());
				        }
				        platformUserLoginService.add(adapter.getLiveUserId());
					}else{
						//没有登录时间，但大厅未清算，以大厅金额为准
						if (date == null) {
							BigDecimal balance = new BigDecimal(req.getBalance());
							if (balance.compareTo(BigDecimal.ZERO) > 0) {
								userRemote.saveMoney(adapter.getLiveUserId(), balance);
								logger.info("真人玩家{} 存入金额{}", adapter.getPlatformUserName(), balance);
							}
							platformUserLoginService.add(adapter.getLiveUserId());
						}
					}
					PlatformUserToken token = new PlatformUserToken(adapter.getPlatformUserId(),adapter.getLiveUserId(),req.getAuthCode()+"");
					stringRedisTemplate.boundValueOps(RedisKeyEnum.PLATFORM_GAME_USER_TOKEN.get(req.getUserId()+"")).set(JSONObject.toJSONString(token));
					
					
					//通知客户端登录游戏
					CreatePlayerResult response = new CreatePlayerResult(req.getUserId(), ReponseStatusEnum.SUCCESS.get(),ReponseStatusEnum.SUCCESS.getDesc());
		        	client.send(response);
		        	
		        	client.clientLogin(req.getUserId());
		        	
		        	List<PlayerData> playerList = GateCache.pindingPlayerCache.getIfPresent(req.getParentId());
		    		if(!CollectionUtils.isEmpty(playerList)){
		    			try {
		    				LOCK_CURRENT_PLAYER_CACHE.writeLock().lock();;
		    				playerList.remove(req);
		    			} catch (Exception e) {
		    				logger.error(e.getMessage());
		    			} finally{
		    				LOCK_CURRENT_PLAYER_CACHE.writeLock().unlock();
		    			}
		    		}
				}
	        }
//        }
    }

    @Subscribe
    public void onMerchantRequest(MerchantRequest req){
//    	logger.info(req.toString());
        MerchantJson merchantJson = JSONObject.parseObject(req.getJson(),MerchantJson.class);
        if("00".equals(merchantJson.getParentId()) || "01".equals(merchantJson.getParentId())){ //00的是最顶级的管理员、01的是直属平台，比如直属平台的商户
        	platformUserAdapterService.add(merchantJson);
        }else{
	        PlatformUserAdapter parentAdapter=platformUserAdapterService.findByMerchantUserId(merchantJson.getParentId());
			if (parentAdapter == null) {
				logger.info("{}上级不存在。", merchantJson.getUsername());
				for (String parentId : merchantJson.getLevelIndex().split(",")) {
					if("01".equals(parentId)){
						continue;
					}
					parentAdapter = platformUserAdapterService.findByMerchantUserId(parentId);
					if (parentAdapter == null) {
						PengMerchantQueueResponse pengMerchant = new PengMerchantQueueResponse(parentId, merchantJson);
						GateCache.pendingMerchantCache.put(parentId, pengMerchant);
						client.getEventBuss().post(pengMerchant);
						break;
					}
				}
			} else {
				platformUserAdapterService.add(merchantJson);
				List<PlayerData> playerList = GateCache.pindingPlayerCache.getIfPresent(merchantJson.getId());
				if(playerList !=null && playerList.size()>0){
					for(PlayerData player:playerList){
						//检查代理下是否存在未处理玩家,进行处理
						createPlayer(player);
					}
				}
				PengMerchantQueueResponse nextMerchant = GateCache.pendingMerchantCache.getIfPresent(merchantJson.getId());
				if (nextMerchant != null) {
					logger.info("nextMerchant:"+JSONObject.toJSONString(nextMerchant));
					PengMerchantQueueResponse pengMerchant = new PengMerchantQueueResponse(merchantJson.getId(),
							nextMerchant.getOrginData());
					if (nextMerchant.getLowerId() != null) {
						GateCache.pendingMerchantCache.put(nextMerchant.getLowerId(), pengMerchant);
						client.getEventBuss().post(pengMerchant);
					}
				}
			}
        }
    }
    
    @Subscribe
    public void onExceptionLoginoutRequest(ExceptionLoginoutRequest req){
    	// 发送账单
    	PlatformUserAdapter platformUserAdapter = platformUserAdapterService.findBy(String.valueOf(req.getUserId()),3);
    	logger.info("玩家异常退出[{}]",platformUserAdapter.toString());
    	// 判断用户是否存在未结算的订单
    	long unsettlementOrder= userRemote.findUnsettlementOrder(platformUserAdapter.getLiveUserId());
    	logger.info("未结算订单记录数：{}笔",unsettlementOrder);
    	if(platformUserAdapter !=null && unsettlementOrder==0){
    		boolean online =gameUserRemote.isOnline(platformUserAdapter.getLiveUserId());
    		logger.debug("用户{}在线状态{}",platformUserAdapter.getPlatformUserName(),online);
    		if(online){
    			gameUserRemote.logOut(platformUserAdapter.getLiveUserId());
    		}
    		client.loginOut(platformUserAdapter.getLiveUserId());
    	}else{
    		// 加入本地缓存，等待该用户所有订单结算完毕后，再发送退出指令
    		logger.info("{}异常退出失败,加入缓存。",platformUserAdapter.getPlatformUserName());
    		GateCache.WAIT_LOGOUT_USER_MAP.put(platformUserAdapter.getLiveUserId(), platformUserAdapter);
    		// 通知游戏服务器锁定该用户
//    		gameUserRemote.lockUser(platformUserAdapter.getLiveUserId(), 1);
    	}
    }


    public void setClient(SocketClient client) {
        this.client = client;
    }
}
