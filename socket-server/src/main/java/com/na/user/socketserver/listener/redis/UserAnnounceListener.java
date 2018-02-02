package com.na.user.socketserver.listener.redis;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.command.sendpara.UserAnnounceResponse;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.send.UserCommand;
import com.na.user.socketserver.entity.UserAnnounce;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.service.IUserAnnounceService;
import com.na.user.socketserver.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户公告
 * @author Administrator
 *
 */
@Component
public class UserAnnounceListener {

	@Autowired
    private IUserAnnounceService userAnnounceService;
	@Autowired
    private IUserService userService;
	@Autowired
    private SocketIOServer socketIOServer;
	@Autowired
    private UserCommand userCommand;
	
	public void onMessage(Object message) {
		//Integer announceId =  (Integer) message;
		//Long announceId = JSONObject.parseObject(json,Long.class);
		//查数据库
		List<UserAnnounce> userAnnounceList = userAnnounceService.getAllUserAnnouce();
		List<UserAnnounceResponse.UserAnnounce> userAnnounceTemps = new ArrayList<>();
		UserAnnounceResponse userAnnounceResponse = new UserAnnounceResponse();
		for (UserAnnounce userAnnounce:userAnnounceList) {
			if(userAnnounce == null) continue;
			String[] userIds = userAnnounce.getUserId().split(",");
			List<Long> uidList = null;
			//代理团队
			if(userAnnounce != null && userAnnounce.getType() == 1 && userIds.length > 0){
				uidList = userService.listUserIdByParentPath(userIds);
			}
			//代理直线
			if(userAnnounce != null && userAnnounce.getType() == 2 && userIds.length > 0){
				uidList = userService.listUserIdBySuperiorID(userIds);
			}
			if(uidList != null){
				UserAnnounceResponse.UserAnnounce temp = userAnnounceResponse.new UserAnnounce();
				temp.title = userAnnounce.getAnnounceContent().getContentTitle();
				temp.content = userAnnounce.getAnnounceContent().getContentDesc();
				temp.uidList = uidList;
				userAnnounceTemps.add(temp);
			}
		}
		if(userAnnounceTemps.size() < 1 ) return;
        //返回数据
        Collection<SocketIOClient> allClients = socketIOServer.getAllClients();
        for (SocketIOClient client : allClients) {
        	UserPO user = AppCache.getUserByClient(client);
        	if(user == null){
        		continue;
        	}
			UserAnnounceResponse userAnnounce = new UserAnnounceResponse();
			userAnnounce.setUserAnnounceList(userAnnounceTemps.stream().filter(item ->{
				return item.uidList.contains(user.getId());
			}).collect(Collectors.toList()));
        	userCommand.sendUserAnnounce(client, userAnnounce);
		}
	}

}
