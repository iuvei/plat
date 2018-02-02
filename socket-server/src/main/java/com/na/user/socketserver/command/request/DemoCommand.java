package com.na.user.socketserver.command.request;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.sendpara.GameTableJson;
import com.na.baccarat.socketserver.common.enums.AccountRecordTypeEnum;
import com.na.baccarat.socketserver.entity.AccountRecord;
import com.na.baccarat.socketserver.entity.LoginStatus;
import com.na.baccarat.socketserver.util.SnowflakeIdWorker;
import com.na.manager.remote.AddLiveUserRequest;
import com.na.remote.IPlatformUserRemote;
import com.na.remote.IUserRemote;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.requestpara.DemoPara;
import com.na.user.socketserver.command.send.UserCommand;
import com.na.user.socketserver.command.sendpara.LoginInfoResponse;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.common.enums.LiveUserSource;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.config.SocketIoConfig;
import com.na.user.socketserver.entity.GamePO;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.filter.CheckerIPFilter;
import com.na.user.socketserver.service.IAccountRecordService;
import com.na.user.socketserver.service.IUserService;
import com.na.user.socketserver.util.Md5Util;
import com.na.user.socketserver.util.des.EncryptKit;

/**
 * 试玩账户
 */
@Cmd(paraCls = DemoPara.class,name = "试玩")
@Component
public class DemoCommand implements ICommand {
	
	private Logger log = LoggerFactory.getLogger(DemoCommand.class);
	
	@Autowired
	private IUserService userServer;
	
	@Autowired
	private UserCommand userCommand;
	
	@Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private SocketIoConfig socketIoConfig;
    
    @Autowired
    private IAccountRecordService accountRecordService;
    
    @Autowired
    private CheckerIPFilter checkerIPFilter;
    
    @Resource(name = "accountRecordSnowflakeIdWorker")
    private SnowflakeIdWorker accountRecordSnowflakeIdWorker;
    
    @Autowired
    private IUserRemote userRemote;
    
    @Autowired
    private IPlatformUserRemote platformUserRemote;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		DemoPara param = (DemoPara) commandReqestPara;
		
		client.set(SocketClientStoreEnum.LANG.get(), param.getLanguage());
		
		int type = 1;
		if(param != null && param.getType() != null && param.getType().compareTo(2) == 0) {
			type = 2;
		}
		
		//检测IP黑白名单
		String clientIp = client.getRemoteAddress().toString();
		clientIp = (String) clientIp.subSequence(clientIp.indexOf("/") + 1, clientIp.lastIndexOf(":"));
		if (clientIp.equals("127.0.0.1") ||
				clientIp.equals("0:0:0:0:0:0:0:1")) {
			//根据网卡取本机配置的IP
			InetAddress inet = null;
			try {
				inet = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				log.error(e.getMessage(),e);
				log.debug("获取本地IP失败");
			}
			clientIp = inet.getHostAddress();
		}
		if(!checkerIPFilter.doFilter(clientIp, param.getDeviceTypeEnum())) {
			throw SocketException.createError("ip.cannot.login");
		} else {
			log.debug("IP黑白名单检查通过   类型: " + param.getDeviceType() + ", IP:" + clientIp);
		}
		
		//1.查找试玩账户
		LiveUserPO demo = null;
		
		UserPO checkUserPO = AppCache.getUserByClient(client);
		if(checkUserPO != null) {
			throw SocketException.createError("client.already.login");
		}
		
		String agentName = "";
		if(type == 1) {
			agentName = socketIoConfig.getDemoAgentName();
		} else if (type == 2) {
			agentName = socketIoConfig.getApiDemoAgentName();
		}
		
		List<LiveUserPO> demoUserList = userServer.getLowerLevelByName(agentName);
		LiveUserPO demoAgent = userServer.getLiveUserByLoginName(agentName);
		if(demoUserList == null || demoUserList.size() < 1) {
			//获取Token  自动创建账户
			demo = addDemoAccount(demoAgent);
		} else {
			Optional<LiveUserPO> userOptional = demoUserList.stream().filter( item -> {
				if(AppCache.getLoginUser(item.getId()) != null) {
					return false;
				}
				return true;
			}).findFirst();
			
			if(demoUserList.size() > socketIoConfig.getDemoAccountNumber()) {
				throw SocketException.createError("demo.current.not.use");
			}
			
			if(userOptional.isPresent()) {
				demo = userOptional.get();
				resetAccount(demo, demoAgent);
			}
			
			if(demo == null) {
				//获取Token  自动创建账户
				demo = addDemoAccount(demoAgent);
			}
		}
		
		if(demo != null) {
			demo.setUserChipList(userServer.getUserChips(demo.getChips()));
			
			GamePO gameInfo = AppCache.getGame(GameEnum.BACCARAT);
			LoginStatus newLoginStatusPara = new LoginStatus();
			newLoginStatusPara.setLoginType(demo.getUserType());
			newLoginStatusPara.setUid(demo.getId());
			newLoginStatusPara.setGid(gameInfo.getId());
			userServer.addLoginStatus(newLoginStatusPara);

			client.set(SocketClientStoreEnum.USER_ID.get(),demo.getId()+"");
			demo.setDeviceType(param.getDeviceType());
			demo.setDeviceInfo(param.getDeviceInfo());
			
			AppCache.addLoginUser(demo);
			
			try {
				platformUserRemote.login(demo.getId());
			} catch (Exception e) {
				log.error("远程调用异常", e);
			}
			
			LoginInfoResponse loginInfoResponse = new LoginInfoResponse();
			
			//控制用户可以看到哪些游戏
			List<Integer> gameModules = Arrays.asList(1,2,3,4,5);
			loginInfoResponse.setGameModules(gameModules);
			loginInfoResponse.setUserChips(demo.getUserChipList());
			loginInfoResponse.setGameTableList(getGameTableList());
			loginInfoResponse.setUser(demo);
			String token = generateToken(demo);
			loginInfoResponse.setToken(token);
			userCommand.loginSuceess(client,loginInfoResponse);
			
			return true;
		} else {
			throw SocketException.createError("demo.gain.failure");
		}
	}
	
	/**
	 * 重置试玩账户
	 */
	private void resetAccount(UserPO demo, UserPO demoAgent) {
		BigDecimal difference = null;
		BigDecimal initBalance = new BigDecimal(socketIoConfig.getDemoAccountInitBalance());
		if(demo.getBalance().compareTo(initBalance) > 0) {
			difference = demo.getBalance().subtract(initBalance);
			
			//代理转入流水
			AccountRecord acountRecord = new AccountRecord();
			acountRecord.setUserId(demoAgent.getId());
			acountRecord.setPreBalance(demoAgent.getBalance());
			acountRecord.setAmount(difference);
			acountRecord.setType(AccountRecordTypeEnum.IN.get());
			accountRecordService.add(acountRecord);
			
			//试玩转出流水
			acountRecord = new AccountRecord();
			acountRecord.setUserId(demo.getId());
			acountRecord.setPreBalance(demo.getBalance());
			acountRecord.setAmount(difference);
			acountRecord.setType(AccountRecordTypeEnum.OUT.get());
			accountRecordService.add(acountRecord);
			
			demoAgent.setBalance(demoAgent.getBalance().add(difference));
			userServer.demoUpdate(demoAgent);
			
			demo.setBalance(new BigDecimal(socketIoConfig.getDemoAccountInitBalance()));
			demo.setHeadPic(null);
			
			demo.setPassword(Md5Util.digest("MD5", getRandomPassword(6).getBytes(), getRandomPassword(24).getBytes(), 3));
			userServer.demoUpdate(demo);
		} else if(demo.getBalance().compareTo(initBalance) < 0) {
			difference = initBalance.subtract(demo.getBalance());
			
			//代理转出流水
			AccountRecord acountRecord = new AccountRecord();
			acountRecord.setUserId(demoAgent.getId());
			acountRecord.setPreBalance(demoAgent.getBalance());
			acountRecord.setAmount(difference);
			acountRecord.setType(AccountRecordTypeEnum.OUT.get());
			accountRecordService.add(acountRecord);
			
			//试玩转入流水
			acountRecord = new AccountRecord();
			acountRecord.setUserId(demo.getId());
			acountRecord.setPreBalance(demo.getBalance());
			acountRecord.setAmount(difference);
			acountRecord.setType(AccountRecordTypeEnum.IN.get());
			accountRecordService.add(acountRecord);
			
			demoAgent.setBalance(demoAgent.getBalance().subtract(difference));
			userServer.demoUpdate(demoAgent);
			
			demo.setBalance(new BigDecimal(socketIoConfig.getDemoAccountInitBalance()));
			userServer.demoUpdate(demo);
		}
	}
	
	private List<GameTableJson> getGameTableList() {
		List<GameTableJson> gameTableList = new ArrayList<>();
		BaccaratCache.getGameTableMap().forEach((key, item)->{
			GameTableJson gameTableJson = new GameTableJson(item);
			gameTableList.add(gameTableJson);
		});
		RouletteCache.getGameTableMap().forEach((key,gameTable)->{
			GameTableJson gameTableJson = new GameTableJson(gameTable);
			gameTableList.add(gameTableJson);
		});
		return gameTableList;
	}
	
	/**
	 * 生成Token
	 * @return
	 */
	private String generateToken(UserPO userPO) {
//		stringRedisTemplate.opsForValue().set(redisKey);
		
		StringBuffer src = new StringBuffer("@@");
		src.append(userPO.getId() + "@");
		String token = EncryptKit.encode(src.toString());
		return token;
	}
	
	/**
	 * 生成随机字符串
	 * @param length
	 * @return
	 */
	private String getRandomPassword(int length){
		String randomStr = "0123456789abcdefghijklmnopqrstuvwxyz=";
		
	    StringBuffer sb = new StringBuffer();
	    int len = randomStr.length();
	    for (int i = 0; i < length; i++) {
	    	int digits = (int) Math.round(Math.random() * (len-1));
	        sb.append(randomStr.charAt(digits));
	    }
	    return sb.toString();
	}
	
	/**
	 * 生成试玩账户用户名
	 */
	public String genrateDemoName() {
		String demoUserName = "NA_";
		
		//生成随机数字和字母,
        String val = "";
        Random random = new Random();  
        
        //参数length，表示生成几位随机数
        for(int i = 0; i < 9; i++) {  
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }
	    
		demoUserName += val;
		return demoUserName;
	}
	
	
	
	/**
	 * 自动添加试玩用户
	 */
	public LiveUserPO addDemoAccount(LiveUserPO demoAgent) {
		if(demoAgent == null) {
			throw SocketException.createError("demo.data.error");
		}
		
		try {
			AddLiveUserRequest addLiveUserRequest = new AddLiveUserRequest();
			addLiveUserRequest.setParentId(demoAgent.getId());
			
			String username = genrateDemoName();
			addLiveUserRequest.setLoginName(username);
			addLiveUserRequest.setNickName(username);
			addLiveUserRequest.setPlayer(true);
			addLiveUserRequest.setWashPercentage(BigDecimal.ZERO);
			addLiveUserRequest.setIntoPercentage(BigDecimal.ZERO);
			addLiveUserRequest.setSource(LiveUserSource.PROXY.get());
			
			Long userId = userRemote.addLiveUser(addLiveUserRequest);
			userRemote.saveMoney(userId, new BigDecimal(socketIoConfig.getDemoAccountInitBalance()));
			
			LiveUserPO demoUser = userServer.getUserById(userId);
			return demoUser;
		} catch (Exception e) {
			log.error("",e);
			throw SocketException.createError("demo.gain.failure");
		}
		
	}

}
