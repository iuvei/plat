package com.na.manager.remote;

import com.na.light.LightRpcService;

/** 
* @author andy
* @date 2017年9月16日 上午11:30:25 
*/
@LightRpcService("gameRemote")
public interface IGameRemote {
	
	public void clearRouletteDealer();
	
	/**
	 * 维护时强制踢出在线用户
	 */
	public void clearOnlineUser();
}
