package com.na.user.socketserver.task;

import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.sendpara.GameTableJson;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.send.UserCommand;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.util.TimeLeft;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PushTabelsStatusTask implements Job {
	
	private Logger log = LoggerFactory.getLogger(PushTabelsStatusTask.class);
	
	@Autowired
	private SocketIOServer socketIOServer;
	
	@Autowired
	private UserCommand userCommand;
	
	/**
	 * 观察现场  监听客户端是否需要更换秘钥
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		TimeLeft timeLeft = new TimeLeft();
		socketIOServer.getAllClients().forEach(client -> {
		    try {
                UserPO user = AppCache.getUserByClient(client);
                if (user == null ||
                        !(user.getUserTypeEnum() == UserTypeEnum.REAL)) {
                    return;
                }
                //在桌子中的用户不发送
                if (user.isInTable()) {
                    return;
                }
//            log.debug("用户 : " + user.getLoginName() + " :" + user.isInTable());

                Map<Integer, GameTable> gameTables = BaccaratCache.getGameTableMap();
                Map<Integer, RouletteGameTable> rouletteGameTables = RouletteCache.getGameTableMap();
                List<GameTableJson> gameTableJsonList = new ArrayList<>();
                gameTables.forEach((tableId, gameTable) -> {
                    gameTableJsonList.add(new GameTableJson(gameTable));
                });
                rouletteGameTables.forEach((tableId, gameTable) -> {
                    gameTableJsonList.add(new GameTableJson(gameTable));
                });

                Map<String, Object> response = new HashMap<>();
                response.put("onlineUserCount", socketIOServer.getAllClients().size());
                response.put("gameTableList", gameTableJsonList);

                userCommand.sendAllTableStatusToClient(client, response);
            }catch (Exception e){
		        log.error(e.getMessage(),e);
            }
         });
//		log.debug("【AllTable】 花费时间：" + timeLeft.end());
	}

}
