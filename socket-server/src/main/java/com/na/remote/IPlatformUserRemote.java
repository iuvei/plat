package com.na.remote;

import com.na.light.LightRpcService;

/**
 * 平台远程接口服务。
 * Created by sunny on 2017/8/9 0009.
 */
@LightRpcService("platformUserRemote")
public interface IPlatformUserRemote {
    String hello(String t);
    void login(Long userId);
    void exit(Long userId);
    void sendRound(Long roundId);
    void sendDealerClassRecord(String dataJson);
}
