package com.na.user.socketserver.command.flow.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.sendpara.GameTableJson;
import com.na.remote.IPlatformUserRemote;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.requestpara.LoginInfoPara;
import com.na.user.socketserver.command.send.UserCommand;
import com.na.user.socketserver.command.sendpara.LoginInfoResponse;
import com.na.user.socketserver.common.enums.ErrorCode;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.enums.UserStatusEnum;
import com.na.user.socketserver.common.event.DisConnectionEvent;
import com.na.user.socketserver.common.event.ReconnectEvent;
import com.na.user.socketserver.config.SocketIoConfig;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.UserAnnounce;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IUserAnnounceService;

@Component
public class PlayerUser extends User {
	
	private Logger log = LoggerFactory.getLogger(PlayerUser.class);
	
	@Autowired
    private IUserAnnounceService userAnnounceService;
	
	@Autowired
	private UserCommand userCommand;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private IPlatformUserRemote platformUserRemote;
	
	@Autowired
	private SocketIoConfig socketIoConfig;

	@Override
	public void login(LoginInfoPara params, SocketIOClient client, LoginInfoResponse response) {
		LiveUserPO liveUserPO = (LiveUserPO) AppCache.getUserByClient(client);
		
		//控制用户可以看到哪些游戏
		List<Integer> gameModules = new ArrayList<>();
		if(liveUserPO.getParentPath().contains("/" + socketIoConfig.getOnlyRoomLine() + "/")) {
			gameModules.add(3);
		} else {
			gameModules.addAll(Arrays.asList(1,2,3,4,5));
		}
		response.setGameModules(gameModules);
		
		// 加载用户筹码信息
		if(StringUtils.isEmpty(liveUserPO.getChips())) {
			throw SocketException.createError("user.not.chips");
		}
		liveUserPO.setUserChipList(userService.getUserChips(liveUserPO.getChips()));
		response.setUserChips(liveUserPO.getUserChipList());
		response.setGameTableList(getGameTableList());
		//查询公告信息
		listUserAnnounce(liveUserPO,response);
		
		response.setUser(liveUserPO);
		String token = generateToken(liveUserPO.getId());
		log.debug("Token :" + token);
		response.setToken(token);
		userCommand.loginSuceess(client,response);
	}
	
	private List<GameTableJson> getGameTableList() {
		List<GameTableJson> gameTableList = new ArrayList<>();
		BaccaratCache.getGameTableMap().forEach((key, item)->{
			GameTableJson gameTableJson = new GameTableJson(item);
			gameTableList.add(gameTableJson);
		});
		Collections.sort(gameTableList, new Comparator<GameTableJson>() {  
            @Override  
            public int compare(GameTableJson o1, GameTableJson o2) {  
                return o1.getId() - o2.getId();  
            }  
        });
		
		RouletteCache.getGameTableMap().forEach((key,gameTable)->{
			GameTableJson gameTableJson = new GameTableJson(gameTable);
			gameTableList.add(gameTableJson);
		});
		return gameTableList;
	}
	
	private void listUserAnnounce(LiveUserPO liveUserPO,LoginInfoResponse loginInfoResponse) {
		List<UserAnnounce> userAnnounceList = userAnnounceService.getAllUserAnnouce();
		List<LoginInfoResponse.UserAnnounce> userAnnounceTemps = new ArrayList<>();
		for (UserAnnounce userAnnounce:userAnnounceList) {
            if(userAnnounce == null) continue;
            String[] userIds = userAnnounce.getUserId().split(",");
            List<Long> uidList = null;
            //代理团队
            if(userAnnounce != null && userAnnounce.getType() == 1 && userIds.length > 0){
                uidList = userService.listUserIdByParentPath(userIds);
            }
            //代理直线
            if(userAnnounce != null && userAnnounce.getType() == 2 && userIds.length > 0){
                uidList = userService.listUserIdBySuperiorID(userIds);
            }
            if(uidList != null){
                LoginInfoResponse.UserAnnounce temp = loginInfoResponse.new UserAnnounce();
                temp.title = userAnnounce.getAnnounceContent().getContentTitle();
                temp.content = userAnnounce.getAnnounceContent().getContentDesc();
                temp.uidList = uidList;
                userAnnounceTemps.add(temp);
            }
        }
		loginInfoResponse.setUserAnnounceList(userAnnounceTemps.stream().filter(item ->{
            return item != null && item.uidList.contains(liveUserPO.getId());
        }).collect(Collectors.toList()));
	}
	
	@Override
	public void reconnect(SocketIOClient client) {
		UserPO userPO = AppCache.getUserByClient(client);
		if(userPO==null) {
			throw SocketException.createError(ErrorCode.RELOGIN, "user.not.exist");
		}
		
		UserPO leaveSeatUser = AppCache.removeDisConnectUser(userPO.getId());
		if (leaveSeatUser!=null) {
			AppCache.removeDisConnectClient(userPO.getId());
			log.debug("【登陆】用户为断线用户, userID:" + userPO.getId());
			
			if(leaveSeatUser.getGameCode() != null) {
				client.set(SocketClientStoreEnum.GAME_CODE.get(), leaveSeatUser.getGameCode());
			}
			
			//断线重连,用旧的缓存,新生成的为null
			leaveSeatUser.setLoginId(userPO.getLoginId());
			leaveSeatUser.setBalance(userPO.getBalance());
			applicationContext.publishEvent(new ReconnectEvent(client));
			AppCache.addLoginUser(leaveSeatUser);
			//抛弃新的引用对象
			userPO = null;
		}
	}

	@Override
	public void logout(CommandReqestPara commandReqestPara, SocketIOClient client) {
		UserPO user = AppCache.getUserByClient(client);
		if (null == user)
			throw SocketException.createError("login.again");
		
		if(user.isInTable()) {
			throw SocketException.createError("plz.exit.table");
		}
		//FIXME 首先锁定用户
//		user.setUserStatus(UserStatusEnum.FREEZE.get());
//		userService.updateStatus(user);
		
		if(user.getTableId() != null) {
			applicationContext.publishEvent(new DisConnectionEvent(user.getTableId()));
		}
		
		userService.logout(user);
		AppCache.removeLoginUser(user);
		userCommand.logoutSuccess(client);
		try {
			platformUserRemote.exit(user.getId());
		} catch (Exception e) {
			log.error("远程调用异常", e);
		}
		log.debug("[" + user.getId() + "]," + user.getLoginName() + "用户退出成功");
		user = null;
	}

}
