package com.na.betRobot.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.na.betRobot.ServerClient;
import com.na.betRobot.cache.RobotCache;
import com.na.betRobot.config.ClientConfig;
import com.na.betRobot.entity.LiveUser;
import com.na.betRobot.remote.IRobotActionRemote;
import com.na.betRobot.robot.Robot;
import com.na.betRobot.service.ILiveUserService;
import com.na.betRobot.util.SpringContextUtil;
import com.na.light.LightRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基本机器人操作
 * 
 * @author alan
 * @date 2017年8月15日 下午6:00:43
 */
//@RestController
//@RequestMapping("/basic")
@LightRpcService(value = "robotActionRemote")
public class RobotActionController implements IRobotActionRemote  {
	private final static Logger log = LoggerFactory.getLogger(RobotActionController.class);
	private static RateLimiter limiter = RateLimiter.create(50);
	/**
	 * 允许进入的桌子。
	 */
	@Value("#{'${robot.gametables}'.split(',')}")
	private int[] robotGametables;
	/**
	 * 换桌概率。:隔开。
	 */
	@Value("${robot.changeTableProbability}")
	private String changeTableProbability;
	
	@Autowired
	private ClientConfig clientConfig;
	
	@Autowired
	private ILiveUserService liveUserService;

	private ExecutorService clientPoolService = Executors.newCachedThreadPool();

	public String load(Integer number) {
		if(number == null) {
			number = 0;
		}
		LiveUser robotAgent = liveUserService.findByLoginName(clientConfig.getRobotAgent());
		List<LiveUser> robots = liveUserService.findByParentId(robotAgent.getId(), number);
		if(robots.size() < number) {
			throw new RuntimeException("机器人数量不够");
		}
		new Thread() {
			@Override
			public void run() {
				try {
					List<ServerClient> clients = RobotCache.getClients();
					for (int i = 0; i < robots.size(); i++) {
						//限速
						limiter.acquire();
						LiveUser liveUser = robots.get(i);
						ServerClient serverClient = SpringContextUtil.getBean(ServerClient.class);
						Robot robot = new Robot(liveUser,robotGametables,changeTableProbability);
						serverClient.setRobot(robot);
						clientPoolService.execute(serverClient);
						if(!clients.stream().anyMatch( item -> item.getRobot().getLiveUser().getId() == liveUser.getId())) {
                            clients.add(serverClient);
                        }
					}
					log.info("本次预启动机器人数量：{}，实际启动数量：{}",robots.size(),clients.size());
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}.start();
		
		return "ok";
	}
	
	public String logout() {
		log.info("机器人退出指令");
		new Thread(){
			@Override
			public void run() {
                try {
					List<ServerClient> clients = RobotCache.getClients();
					List<ServerClient> copyClients = new ArrayList<>(clients);
					while(copyClients.size() > 0) {
                        for(int i = 0; i < copyClients.size() ; i++) {
                            //限速
                            if(limiter.tryAcquire()) {
                                try {
                                    ServerClient client = copyClients.remove(i);
                                    client.robotDisconnect();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
					log.error("", e);
                }
			}
		}.start();
		return "ok";
	}

	@Override
	public String simpleLogin(Long id) {
		if(id == null) {
			throw new RuntimeException("id不能为空");
		}
		
		LiveUser liveUser = liveUserService.findById(id);
		if(liveUser == null) {
			throw new RuntimeException("未找到对应机器人");
		}

		ServerClient serverClient = SpringContextUtil.getBean(ServerClient.class);
		Robot robot = new Robot(liveUser,robotGametables,changeTableProbability);
		serverClient.setRobot(robot);
		clientPoolService.execute(serverClient);

		return "ok";
	}

	@Override
	public String simpleLogout(Long id) {
		log.info("单个机器人退出指令");
		if(id == null) {
			throw new RuntimeException("id不能为空");
		}
		List<ServerClient> clients = RobotCache.getClients();
		for(ServerClient client : clients) {
			if(client.getRobot()!= null && ((Robot)client.getRobot()).getLiveUser().getId().compareTo(id) == 0) {
				client.robotDisconnect();
			}
		}
		return "ok";
	}

}
