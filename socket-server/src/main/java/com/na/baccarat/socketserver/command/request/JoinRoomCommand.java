package com.na.baccarat.socketserver.command.request;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.common.util.concurrent.RateLimiter;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.baccarat.socketserver.command.requestpara.JoinRoomPara;
import com.na.baccarat.socketserver.command.sendpara.JoinRoomResponse;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.ErrorCode;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.CacheContainsException;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.util.ConvertUtil;

/**
 * 进入实体桌
 * 
 * @author alan
 * @date 2017年4月27日 下午3:34:58
 */
@Cmd(name="加入桌子",paraCls=JoinRoomPara.class)
@Component("joinCommand")
public class JoinRoomCommand implements ICommand {
	private Logger log = LoggerFactory.getLogger(JoinRoomCommand.class);
	//限速器。
	private RateLimiter rateLimiter;
	@Value("${na.limiter.join}")
	private double limiterRate;
	@Autowired
	private BaccaratClassHandler baccaratClassHandler;

	@PostConstruct
	public void init(){
		this.rateLimiter = RateLimiter.create(this.limiterRate);
	}
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		JoinRoomPara params = (JoinRoomPara) commandReqestPara;
		JoinRoomResponse response = new JoinRoomResponse();
		
		if (params == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		Long userId = ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get()));
		UserPO loginUserPO = AppCache.getLoginUser(userId);
		
		if(loginUserPO==null) {
			throw SocketException.createError(ErrorCode.RELOGIN, "user.not.exist");
		}
		
		if(loginUserPO.getUserTypeEnum()== UserTypeEnum.REAL) {
			SocketException.isTrue(!rateLimiter.tryAcquire(),"server.is.busy");
		}
		
		client.set(SocketClientStoreEnum.GAME_CODE.get(), BaccaratCache.getGame().getGamePO().getGameCode());
		loginUserPO.setGameCode(BaccaratCache.getGame().getGamePO().getGameCode());
		
		try {
			baccaratClassHandler.baccaratAction(loginUserPO).join(params, client, response);
		} catch(CacheContainsException a) {
			throw a;
		} catch(SocketException e) {
			BaccaratCache.removeUser(userId);
			loginUserPO.setInTable(false);
			throw e;
		} catch (Exception e) {
			BaccaratCache.removeUser(userId);
			loginUserPO.setInTable(false);
			throw e;
		}
		return true;
	}
	
}