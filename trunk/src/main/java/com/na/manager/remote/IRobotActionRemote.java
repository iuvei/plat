package com.na.manager.remote;

import com.na.light.LightRpcService;

/**
 * 机器人动作远程调用接口
 * 
 * @author alan
 * @date 2017年8月22日 下午12:20:21
 */
@LightRpcService("robotActionRemote")
public interface IRobotActionRemote {
	
	public String load(Integer number);
	
	public String logout();
	
	public String simpleLogin(Long id);
	
	public String simpleLogout(Long id);
	
}
