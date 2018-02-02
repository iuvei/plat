package com.na.remote.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.light.LightRpcService;
import com.na.remote.IGameUserRemote;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.common.enums.UserStatusEnum;
import com.na.user.socketserver.common.event.DisConnectionEvent;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.service.IUserService;
import com.na.user.socketserver.util.SocketUtil;

/**
 * @author andy
 * @date 2017年9月16日 上午11:42:23
 */
@LightRpcService(value="gameUserRemote")
public class GameUserRemoteImpl implements IGameUserRemote {
	@Autowired
	private SocketIOServer socketIOServer;
	@Autowired
	private IUserService userService;
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public boolean isOnline(Long userId) {
		UserPO userPO = AppCache.getLoginUser(userId);
		if (userPO != null) {
			if(AppCache.getDisConnectUser(userId) != null) {
				return true;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public void logOut(Long userId) {
		UserPO userPO = AppCache.getLoginUser(userId);
		SocketIOClient oldClient = SocketUtil.getClientByUser(socketIOServer, userPO);
		
		AppCache.addDisConnectUserMap(userPO);
		AppCache.addDisConnectClient(userPO.getId(), oldClient);
		//等待桌子结算完毕   清理断线用户
		userService.logout(userPO);
		
		//结算完毕  清除离线用户
		if(userPO.getTableId() != null) {
			applicationContext.publishEvent(new DisConnectionEvent(userPO.getTableId()));
		}
	}

	@Override
	public void lockUser(Long userId, Integer flag) {
		UserPO userPO = AppCache.getLoginUser(userId);
		if(userPO == null) {
			userPO = new UserPO();
			userPO.setId(userId);
		}
		if(flag == 1) {
			userPO.setUserStatus(UserStatusEnum.FREEZE.get());
		} else if(flag == 2) {
			userPO.setUserStatus(UserStatusEnum.NORMAL.get());
		}
		userService.updateStatus(userPO);
	}
}
