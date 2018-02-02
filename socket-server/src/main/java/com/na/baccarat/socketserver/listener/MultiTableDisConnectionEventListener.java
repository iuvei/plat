package com.na.baccarat.socketserver.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.entity.User;
import com.na.baccarat.socketserver.listener.event.MultiDisConnectionEvent;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.request.LogoutCommand;


/**
 * 删除用户数据任务
 * 
 * @author alan
 * @date 2017年5月5日 下午3:12:04
 */
@Component
public class MultiTableDisConnectionEventListener implements ApplicationListener<MultiDisConnectionEvent> {
	
	private Logger log = LoggerFactory.getLogger(MultiTableDisConnectionEventListener.class);
	
	@Autowired
	private LogoutCommand logoutCommand;

	@Async
	@Override
	public void onApplicationEvent(MultiDisConnectionEvent event) {
		Long userId = (Long) event.getSource();
        if(userId == null ) {
        	log.error("没找到相应多台用户" + userId);
            return ;
        }
        try {
        	User user = BaccaratCache.removeUser(userId);
			SocketIOClient client = AppCache.removeDisConnectClient(userId);
			if (client != null) {
				if(user != null) {
					log.debug("【任务】删除该多台用户缓存数据 ： " + user.getUserPO().getLoginName());
				}
				user.getUserPO().setInTable(false);
				user.getUserPO().setTableId(null);
				AppCache.removeDisConnectUser(userId);
				logoutCommand.exec(client, null);
			}
		}catch (Exception e){
    		log.error(e.getMessage(),e);
		}
	}

}
