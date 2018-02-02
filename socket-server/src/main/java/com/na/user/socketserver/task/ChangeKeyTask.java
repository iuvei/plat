package com.na.user.socketserver.task;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.user.socketserver.command.sendpara.ChangeKeyResponse;
import com.na.user.socketserver.command.sendpara.CommandResponse;
import com.na.user.socketserver.constant.Constant;
import com.na.user.socketserver.util.SocketUtil;
import com.na.user.socketserver.util.aes.AESEncryptKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器更换秘钥
 */
public class ChangeKeyTask implements Runnable {
	private static Logger log = LoggerFactory.getLogger(ChangeKeyTask.class);

	private SocketIOClient client;
	
	public ChangeKeyTask(SocketIOClient client) {
		super();
		this.client = client;
	}
	
	@Override
	public void run() {
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
		client.set(Constant.CHANGE_KEY_TIMETSTAMP, String.valueOf((System.currentTimeMillis() + 3600 * 1000)));
		
		//发送更换秘钥命令
		CommandResponse response = CommandResponse.createSuccess(RequestCommandEnum.COMMON_CHANGE_KEY,changeKeyResponse);
    	SocketUtil.send(client, ResponseCommandEnum.OK, response);
		
	}

	public SocketIOClient getClient() {
		return client;
	}

	public void setClient(SocketIOClient client) {
		this.client = client;
	}
	
	
}
