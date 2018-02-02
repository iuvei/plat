package com.na.gate.remote;

import com.na.light.LightRpcService;

/** 
* @author andy
* @date 2017年9月16日 上午11:30:25 
*/
@LightRpcService("gameUserRemote")
public interface IGameUserRemote {
	/**
	 * 判断用户是否在线
	 * @param userId
	 * @return
	 */
	boolean isOnline(Long userId);
	
	/**
	 *  通知游戏服务器用户退出
	 * @param userId
	 */
	void logOut(Long userId);
	
	/**
	 * 通知游戏服务器锁定、解锁用户
	 * @param userId
	 * @param flag  1:锁定 2：解锁
	 */
	void lockUser(Long userId,Integer flag);

}
