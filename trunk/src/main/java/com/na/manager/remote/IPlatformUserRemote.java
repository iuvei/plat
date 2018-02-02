package com.na.manager.remote;

import com.na.light.LightRpcService;

/**
 * 平台远程接口服务。
 * Created by sunny on 2017/8/9 0009.
 */
@LightRpcService("platformUserRemote")
public interface IPlatformUserRemote {
    void exit(Long userId);
    
    void sendRound(Long roundId);
    
    void syncRobotOnline(Long number);
    
    String hello(String t);
}
