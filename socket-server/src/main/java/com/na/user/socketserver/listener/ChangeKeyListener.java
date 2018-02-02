package com.na.user.socketserver.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.user.socketserver.command.send.UserCommand;
import com.na.user.socketserver.command.sendpara.ChangeKeyResponse;
import com.na.user.socketserver.command.sendpara.CommandResponse;
import com.na.user.socketserver.common.event.ChangeKeyEvent;
import com.na.user.socketserver.config.SocketIoConfig;
import com.na.user.socketserver.constant.Constant;
import com.na.user.socketserver.util.SocketUtil;
import com.na.user.socketserver.util.aes.AESEncryptKit;

/**
 * 服务器更换秘钥
 * 
 * @author alan
 * @date 2017年5月30日 下午5:28:25
 */
@Component
public class ChangeKeyListener implements ApplicationListener<ChangeKeyEvent>{
	
    private final static Logger log = LoggerFactory.getLogger(ChangeKeyListener.class);

    @Autowired
    private SocketIOServer socketIOServer;
    
    @Autowired
    private UserCommand userCommand;
    
    @Autowired
    private SocketIoConfig socketIoConfig;

    @Override
    public void onApplicationEvent(ChangeKeyEvent reconnectEvent) {
    	SocketIOClient client = reconnectEvent.getSource();
    	log.debug(String.format("【任务】：[%s] 触发更换秘钥任务",client.getRemoteAddress().toString()));
        if (client == null || !client.isChannelOpen()) {
			return;
		}
		
		String key = AESEncryptKit.genarateKey();
		String testStr = AESEncryptKit.generateRandomString(5);
		
		ChangeKeyResponse changeKeyResponse = new ChangeKeyResponse();
		changeKeyResponse.setKey(key);
		try {
			changeKeyResponse.setTest(AESEncryptKit.encrypt(testStr, key));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		
		client.set(Constant.SECRET_TEST, testStr);
		client.set(Constant.SECRET_NEW_KEY, key);
		client.set(Constant.CHANGE_KEY_TIMETSTAMP, String.valueOf((System.currentTimeMillis() + socketIoConfig.getChangeKeyInterval() * 1000)));
		
		//发送更换秘钥命令
		CommandResponse response = CommandResponse.createSuccess(RequestCommandEnum.COMMON_CHANGE_KEY,changeKeyResponse);
    	SocketUtil.send(client, ResponseCommandEnum.OK, response);
    }
}
