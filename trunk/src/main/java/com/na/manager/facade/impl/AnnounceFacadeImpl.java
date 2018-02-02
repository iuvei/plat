package com.na.manager.facade.impl;

import com.alibaba.fastjson.JSON;
import com.na.manager.common.redis.RedisClient;
import com.na.manager.common.redis.RedisEventData;
import com.na.manager.entity.AnnounceContent;
import com.na.manager.entity.UserAnnounce;
import com.na.manager.facade.IAnnounceFacade;
import com.na.manager.service.IAnnounceManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 公告操作
 * @author andy
 * @date 2017年7月3日 下午3:56:22
 * 
 */
@Component
public class AnnounceFacadeImpl implements IAnnounceFacade {

	@Autowired
	private IAnnounceManageService announceManagerService;
	
	@Autowired
	private RedisClient redisClient;
	
	@Override
	public void insertAnnounceConent(AnnounceContent announceContent) {
		UserAnnounce ua =announceManagerService.insertAnnounceConent(announceContent);
		if(ua !=null){
			RedisEventData redisEventData = new RedisEventData();
			redisEventData.setRedisEventData(ua.getId());
			redisEventData.setRedisEventType(RedisEventData.RedisEventType.UserAnnounce.get());
			redisClient.publishGameServer(JSON.toJSONString(redisEventData));
		}
	}

	@Override
	public void updateUserAnnounce(UserAnnounce userAnnounce) {
		announceManagerService.updateUserAnnounce(userAnnounce);
		Map<String, String> mapList = announceManagerService.dealUserNameList(userAnnounce.getUserName());
		userAnnounce.setUserName(mapList.get("namePath"));
		userAnnounce.setUserId(mapList.get("idPath"));
		RedisEventData redisEventData = new RedisEventData();
		redisEventData.setRedisEventData(userAnnounce.getId());
		redisEventData.setRedisEventType(RedisEventData.RedisEventType.UserAnnounce.get());
		redisClient.publishGameServer(JSON.toJSONString(redisEventData));
	}

	@Override
	public void delete(Long id) {
		announceManagerService.delete(id);
		RedisEventData redisEventData = new RedisEventData();
		redisEventData.setRedisEventType(RedisEventData.RedisEventType.UserAnnounce.get());
		redisClient.publishGameServer(JSON.toJSONString(redisEventData));
	}

}
