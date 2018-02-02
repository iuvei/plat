package com.na.gate.remote;

import com.na.gate.common.SpringContextUtil;
import com.na.gate.proto.SocketClient;
import com.na.light.LightRpcService;

/**
 * Created by sunny on 2017/8/9 0009.
 */
@LightRpcService("platformUserRemote")
public class PlatformUserRemoteImpl implements IPlatformUserRemote{
    @Override
    public void exit(Long userId) {
        getSocketClient().loginOut(userId);
    }

    @Override
    public String hello(String t) {
        return "start success";
    }

    private SocketClient getSocketClient(){
        return SpringContextUtil.getBean(SocketClient.class);
    }

	@Override
	public void sendRound(Long roundId) {
		 getSocketClient().sendBetOrderByRoundId(roundId);
	}

	@Override
	public void sendDealerClassRecord(String dataJson) {
		getSocketClient().sendDealerClassRecord(dataJson);
	}

	@Override
	public void login(Long userId) {
		getSocketClient().login(userId);
	}

	@Override
	public void syncRobotOnline(Long number) {
		getSocketClient().syncRobotOnline(number);
	}
}
