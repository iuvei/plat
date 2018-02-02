package com.na.user.socketserver.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.send.DealerCommand;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.request.LogoutCommand;
import com.na.user.socketserver.common.SpringContextUtil;
import com.na.user.socketserver.common.enums.LiveUserTypeEnum;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.common.event.DisConnectionEvent;
import com.na.user.socketserver.config.SocketIoConfig;
import com.na.user.socketserver.constant.Constant;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.service.IUserService;

/**
 * 客户端连接监听器
 * 
 * @author alan
 * @date 2017年4月27日 下午3:27:42
 */
@Component
@PropertySource("classpath:config/socketio.properties")
public class ConnectListener {
	
	private final static Logger log = LoggerFactory.getLogger(ConnectListener.class);
	
	@Autowired
	private SocketIoConfig socketIoConfig;
	@Autowired
	private ApplicationContext applicationContext;
	
	public LogoutCommand getLogoutCommand(){
		return (LogoutCommand)SpringContextUtil.getBean("logoutCommand");
	}
	
	public DealerCommand getDealerCommand(){
		return (DealerCommand)SpringContextUtil.getBean("dealerCommand");
	}
	
	public IUserService getUserService(){
		return (IUserService)SpringContextUtil.getBean("userServiceImpl");
	}
	
	/**
	 * 接收建立连接事件
	 * 
	 * @param client
	 */
	public void onConnect(SocketIOClient client) {
		log.info(String.format("【客户端】：[%s] -> 内容： 客户端已连接",client.getRemoteAddress().toString()));
		
		//添加更换秘钥Task
		if(!client.has(Constant.SECRET_KEY)) {
			client.set(Constant.SECRET_KEY, socketIoConfig.getDefaultKey());
			long stamp = System.currentTimeMillis() + socketIoConfig.getChangeKeyInterval() * 1000;
			client.set(Constant.CHANGE_KEY_TIMETSTAMP, stamp + "");
		}
	}
	
	/**
	 * 断开连接触发事件
	 * 
	 * @param client
	 */
	public void onDisConnect(SocketIOClient client) {
		
		if(client != null) {
			UserPO user = AppCache.getUserByClient(client);
			if(null == user)
				return;
			
			if(client.get(SocketClientStoreEnum.DISCONNECT_FLAG.get()) == null) {
				if(UserTypeEnum.REAL == user.getUserTypeEnum()) {
					if(LiveUserTypeEnum.PLAYER == ((LiveUserPO)user).getTypeEnum()) {
						AppCache.addDisConnectUserMap(user);
						AppCache.addDisConnectClient(user.getId(), client);
						//等待桌子结算完毕   清理断线用户
						getUserService().logout(user);
						//试玩用户退出清理内存数据
						if(((LiveUserPO)user).getParentPath().contains("/7/") && user.isInTable()) {
							//结算完毕  清除离线用户
							applicationContext.publishEvent(new DisConnectionEvent(user.getTableId()));
						}
					}
				} else if(UserTypeEnum.DEALER == user.getUserTypeEnum()) {
					AppCache.addDisConnectUserMap(user);
					AppCache.addDisConnectClient(user.getId(), client);
					//等待桌子结算完毕   清理断线用户
					getUserService().logout(user);
//					getLogoutCommand().exec(client, null);
				}
			} else {
				client.del(SocketClientStoreEnum.DISCONNECT_FLAG.get());
			}
			
//			client.del(Constant.SECRET_KEY);
//			client.del(Constant.SECRET_NEW_KEY);
			
			String loginName = user.getLoginName();
			log.info(String.format("【客户端】：[%s] -> 用户: [%s] Name:%s,内容： 客户端安全断开连接",client.getRemoteAddress().toString(), user.getId(), loginName));
		}
	}

}
