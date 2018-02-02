package com.na.betRobot.robot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.na.betRobot.ServerClient;
import com.na.test.batchbet.common.CommandResponse;
import com.na.test.batchbet.common.RequestCommandEnum;

/**
 * 机器人行为接口
 * 
 * @author alan
 * @date 2017年8月15日 下午4:10:08
 */
public interface RobotAction {
	
	static RateLimiter limiter = RateLimiter.create(50);
	
	final static Logger log = LoggerFactory.getLogger(RobotAction.class);
	
	void loginInfo(CommandResponse response, ServerClient client);
	
	void logout(CommandResponse response, ServerClient client);
	
	void leaveRoom(CommandResponse response, ServerClient client);
	
	void join(CommandResponse response, ServerClient client);
	
	void startBet(CommandResponse response, ServerClient client);
	
	void stopBet(CommandResponse response, ServerClient client);
	
	void gameResult(CommandResponse response, ServerClient client);
	
	void userBalanceChange(CommandResponse response, ServerClient client);
	
	void dealerLeaveTable(CommandResponse response, ServerClient client);
	
	void selectRoom(CommandResponse response, ServerClient client);
	
	void userCard(CommandResponse response, ServerClient client);
	
	void sendLogin(ServerClient client);
	
	void sendJoin(ServerClient client);
	
	void sendHreatBeat(ServerClient client);
	
	/**
	 * 处理命令
	 */
	default void dealCmd(String event, CommandResponse response, ServerClient client) {
		RequestCommandEnum cmd = response.getTypeEnum();
		Class<? extends RobotAction> clazz = getClass();
		try {
			if ("ok".equals(event)) {
				if(cmd != RequestCommandEnum.SERVER_ALL_TABLE_STATUS) {
					Method mothod = clazz.getDeclaredMethod(response.getType(), CommandResponse.class, ServerClient.class);
					mothod.invoke(this, response, client);
				}
	        } else if ("error".equals(event)) {
	        	if(cmd == RequestCommandEnum.COMMON_JOIN_ROOM) {
	        		if("用户已在游戏中".equals(response.getMsg())) {
	        			client.robotDisconnect();
	        		} else {
	        			log.debug(client.getRobot().toString() + "重新尝试加入桌子");
		        		//限速
						limiter.acquire();
		        		this.sendJoin(client);
	        		}
	        	} else if (cmd == RequestCommandEnum.CLIENT_BET && "用户余额不足".equals(response.getMsg())) {
					stopBet(null,null);
	        		client.send(RequestCommandEnum.COMMON_LEAVE_ROOM, null);
	        	} else if(cmd == RequestCommandEnum.SELECT_ROOM && "无数据".equals(response.getMsg())) {
	        		this.sendJoin(client);
	        	}
	        	
	        	if("网络超时,请重进游戏".equals(response.getMsg())) {
	        		client.send(RequestCommandEnum.CLIENT_RECONNECT, null);
	        	}
	        	log.warn(client.getRobot().toString() + "【错误】系统返回：" + JSON.toJSONString(response));
	        } else {
	        	log.error(client.getRobot().toString() + "无法识别事件：" + event);
	        }
			
		} catch (NoSuchMethodException e) {
			//没有该方法
			if (cmd != RequestCommandEnum.HEART_BEAT) {
				log.warn(client.getRobot().toString() + "RobotAction未实现该方法 ：" + response.getType());
			}
		} catch (SecurityException e) {
			log.error(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(),e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(),e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(),e);
		}
		
	}
	
}
