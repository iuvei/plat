package com.na.gate.remote;

/**
 * 平台远程接口服务。
 * Created by sunny on 2017/8/9 0009.
 */
public interface IPlatformUserRemote {
    void exit(Long userId);
    
    void sendRound(Long roundId);
    
    void sendDealerClassRecord(String dataJson);
    
    void syncRobotOnline(Long number);
    
    void login(Long userId);
    
    String hello(String t);
}
