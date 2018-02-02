package com.na.user.socketserver.command.request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.flow.UserClassHandler;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.requestpara.ReconnectPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.enums.UserStatusEnum;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IUserService;
import com.na.user.socketserver.util.SocketUtil;
import com.na.user.socketserver.util.des.EncryptKit;

/**
 * 用户断线重连指令
 */
@Cmd(paraCls = ReconnectPara.class,name = "断线重连指令")
@Component
public class ReconnectCommand implements ICommand {
	
	private final static Logger log = LoggerFactory.getLogger(ReconnectCommand.class);
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	protected SocketIOServer socketIOServer;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
    private UserClassHandler userClassHandler;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		ReconnectPara params = (ReconnectPara) commandReqestPara;
		
		if(params == null || params.getToken() == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		// Token执行登录
		UserPO user = decodeToken(params.getToken());
		if(user.getId()!=null){
			user = userService.login(user.getId());
		}

		// 判断登录结果
		SocketException.isNull(user, "user.not.exist");
		
		checkUserStatus(user);
		
		SocketIOClient oldClient = SocketUtil.getClientByUser(socketIOServer, user);
		if(oldClient != null) {
			oldClient.set(SocketClientStoreEnum.DISCONNECT_FLAG.get(), "true");
			
			//由于断线为异步 所以此处主动触发处理断线流程
			UserPO oldUser = AppCache.getUserByClient(oldClient);
			AppCache.addDisConnectUserMap(oldUser);
			AppCache.addDisConnectClient(oldUser.getId(), client);
			//等待桌子结算完毕   清理断线用户
			userService.logout(oldUser);
			
			oldClient.disconnect();
		}
		
		client.set(SocketClientStoreEnum.USER_ID.get(),user.getId()+"");
		
		userClassHandler.user(user).reconnect(client);
		
//		applicationContext.publishEvent(new ReconnectEvent(client));
//		AppCache.removeDisConnectUser(user.getId());
//		AppCache.removeDisConnectClient(user.getId());
		
		return true;
	}

	private void checkUserStatus(UserPO user) {
		if (user.getUserStatus() != 1) {
			String msg = null;
			// 0正常 1锁定 2冻结
			if (user.getUserStatusEnum() == UserStatusEnum.LOCKED) {
				msg = "user.status.lock";
			} else if (user.getUserStatusEnum() == UserStatusEnum.FREEZE) {
				msg = "user.status.frozen";
			} else {
				msg = "user.status.delete";
			}
			throw SocketException.createError(msg);
		}
	}

	/**
	 * 从cookies中抽取参数（ip、uid），并放入user对象中。
	 * @param loginInfoPara
	 */
	private UserPO decodeToken(String token) {
		UserPO user = new UserPO();
		String  key = null;
		try {
            key = URLDecoder.decode(token, "utf-8");
        } catch (UnsupportedEncodingException e) {
            key = token;
        }
		key = key.replaceAll(" ", "+");

		key = EncryptKit.decode(key);
		String[] arr = key.split("@");
//		if (arr.length != 4) {
//			throw SocketException.createError("cookie decode error!");
//		}
		user.setId( Long.valueOf(arr[2]) );
		return user;
	}

}
