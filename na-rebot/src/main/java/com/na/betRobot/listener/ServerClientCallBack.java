package com.na.betRobot.listener;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.na.betRobot.ServerClient;
import com.na.betRobot.robot.RobotAction;
import com.na.test.batchbet.common.CommandResponse;
import com.na.test.batchbet.common.RequestCommandEnum;
import com.na.test.batchbet.util.SocketUtil;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;

public class ServerClientCallBack implements IOCallback {
	
	private final static Logger log = LoggerFactory.getLogger(ServerClientCallBack.class);
	
	private ServerClient client;
	private RobotAction robot;
	
	public ServerClientCallBack(ServerClient client, RobotAction robot) {
		this.client = client;
		this.robot = robot;
	}
	
	@Override
	public void onMessage(JSONObject json, IOAcknowledge ack) {
		
	}
	
	@Override
	public void onMessage(String data, IOAcknowledge ack) {
		
	}
	
	@Override
	public void onError(SocketIOException socketIOException) {
		log.debug("onError", socketIOException);
	}
	
	@Override
	public void onDisconnect() {
		log.debug("onDisconnect");
	}
	
	@Override
	public void onConnect() {
		//发送登陆命令
		log.debug("发送登陆");
		robot.sendLogin(client);
	}
	
	@Override
	public void on(String event, IOAcknowledge ack, Object... args) {
		com.alibaba.fastjson.JSONObject content = SocketUtil.connectDetrypt((String) args[0], client.getKey(), client.getOldKey());
		CommandResponse response = content.toJavaObject(CommandResponse.class);
		if(response.getTypeEnum() != RequestCommandEnum.SERVER_ALL_TABLE_STATUS) {
//			log.debug("【接收】 命令： " + response.getType() + ",内容:" + content.toJSONString());
		}
		if(response.getTypeEnum() == RequestCommandEnum.COMMON_CHANGE_KEY) {
    		client.changeKey(response);
    	} else {
    		robot.dealCmd(event, response, client); 
    	}
	}
	
}
