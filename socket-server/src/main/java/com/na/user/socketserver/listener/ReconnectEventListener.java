package com.na.user.socketserver.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.send.TableCommand;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.send.UserCommand;
import com.na.user.socketserver.command.sendpara.ReconnectResponse;
import com.na.user.socketserver.common.event.ReconnectEvent;
import com.na.user.socketserver.entity.UserPO;

/**
 * 百家乐重连监听
 * 
 * @author alan
 * @date 2017年5月24日 下午12:06:45
 */
@Component
public class ReconnectEventListener implements ApplicationListener<ReconnectEvent> {
	
    private final static Logger log = LoggerFactory.getLogger(ReconnectEventListener.class);

    @Autowired
    private SocketIOServer socketIOServer;
    
    @Autowired
    private UserCommand userCommand;
    
    @Autowired
    private TableCommand tableCommand;

    @Override
    public void onApplicationEvent(ReconnectEvent reconnectEvent) {
    	SocketIOClient client = reconnectEvent.getSource();
        if(client == null) {
        	log.error("客户端已经断开");
            return ;
        }
    	
        UserPO userPO = AppCache.getUserByClient(client);
        if(userPO == null ) {
        	log.error("没找到用户");
            return ;
        }
        
        //用户掉线重连机制
  		if (!userPO.isInTable()) {
  			ReconnectResponse response = new ReconnectResponse();
  			
  			userCommand.send(client, RequestCommandEnum.CLIENT_RECONNECT, response);
  		}
    }
}
