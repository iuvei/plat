package com.na.betRobot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.na.betRobot.cache.RobotCache;
import com.na.betRobot.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.na.betRobot.config.ClientConfig;
import com.na.betRobot.config.QuartzConfig;
import com.na.betRobot.listener.ServerClientCallBack;
import com.na.betRobot.robot.Robot;
import com.na.betRobot.robot.RobotAction;
import com.na.betRobot.task.RobotHreatBeatTask;
import com.na.test.batchbet.common.CommandRequest;
import com.na.test.batchbet.common.CommandResponse;
import com.na.test.batchbet.common.RequestCommandEnum;
import com.na.test.batchbet.util.AESEncryptKit;
import com.na.test.batchbet.util.SocketUtil;

import io.socket.SocketIO;

/**
 * 连接客户端
 * 
 * @author alan
 * @date 2017年8月15日 下午3:46:40
 */
public class ServerClient implements Runnable {
	
	private final static Logger log = LoggerFactory.getLogger(ServerClient.class);
	
	private SocketIO socketIO = new SocketIO();
	
	private Robot robot;
	
	@Autowired
	private ClientConfig clientConfig;
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	private String key;
    private String oldKey;

	@Override
	public void run() {
		final ServerClient client = this;
		key = clientConfig.getDefaultKey();
		if(this.robot == null) {
			throw new RuntimeException("没有设置机器人");
		}
		Thread.currentThread().setName("[Thread-" + this.robot.toString() + " ]");
		try {
			socketIO.connect(clientConfig.getServerUrl(), new ServerClientCallBack(this,robot));

			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("client", client);
			dataMap.put("robot", robot);
			Thread.sleep(1000);
			if(socketIO.isConnected()) {
				log.debug("添加心跳任务");
				QuartzConfig.addJob(schedulerFactoryBean, RobotHreatBeatTask.class, RobotHreatBeatTask.class.getName() + ((Robot)robot).getLiveUser().getId(), "*/1 * * * * ?", dataMap);
			}

		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
	}

	public void robotDisconnect() {
		try {
			if(robot != null) {
				QuartzConfig.removeJob(schedulerFactoryBean, RobotHreatBeatTask.class.getName() + ((Robot)this.robot).getLiveUser().getId());
				QuartzConfig.removeJob(schedulerFactoryBean, "RobotBetTask" + ((Robot)this.robot).getLiveUser().getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.send(RequestCommandEnum.COMMON_LEAVE_ROOM, null);
	}
	
	public void disconnect() {
		socketIO.disconnect();
		List<ServerClient> clients = RobotCache.getClients();
		clients.remove(this);
		log.debug(robot.toString() + ",断线成功");
	}
	
	public void changeKey(CommandResponse response) {
		com.alibaba.fastjson.JSONObject body = (com.alibaba.fastjson.JSONObject)response.getBody();
        String key = body.getString("key");
        String test = body.getString("test");
        try {
            String msg = AESEncryptKit.detrypt(test, key);
            CommandRequest commandRequest = new CommandRequest();

            commandRequest.setMsg("success");
            commandRequest.setTest(msg);
            String str = SocketUtil.connectEncrypt(commandRequest,key);
            socketIO.emit("changeKey",str);

            this.oldKey = this.key;
            this.key = key;
            log.debug("【更换秘钥】 oldKey:" + this.oldKey + ",Key:" + this.key);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
	}
	
	public void send(RequestCommandEnum cmd,Object bean) {
		CommandRequest res = new CommandRequest();
        res.setBody(bean);
        res.setCmd(cmd.get());
        String str = SocketUtil.connectEncrypt(res,key);
        socketIO.send(str);

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOldKey() {
		return oldKey;
	}

	public void setOldKey(String oldKey) {
		this.oldKey = oldKey;
	}

	public Robot getRobot() {
		return robot;
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}

}
