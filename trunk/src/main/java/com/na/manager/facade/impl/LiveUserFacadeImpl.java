package com.na.manager.facade.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.na.manager.bean.vo.AccountVO;
import com.na.manager.common.redis.RedisClient;
import com.na.manager.common.redis.RedisEventData;
import com.na.manager.entity.LiveUser;
import com.na.manager.enums.LiveUserIsBet;
import com.na.manager.enums.LiveUserType;
import com.na.manager.enums.UserStatus;
import com.na.manager.facade.ILiveUserFacade;
import com.na.manager.service.ILiveUserService;

/**
 * 用户状态操作
 * 
 * @author andy
 * @date 2017年7月3日 下午3:07:51
 * 
 */
@Component
public class LiveUserFacadeImpl implements ILiveUserFacade {

	@Autowired
	private ILiveUserService liveUserService;
		
    @Autowired
	private RedisClient redisClient;

	@Override
	public void modifyStatus(Long id, LiveUserType liveUserType,
			UserStatus userStatus) {
		LiveUser liveUser = liveUserService.modifyStatus(id, liveUserType, userStatus);
		
		if (LiveUserType.MEMBER == liveUser.getTypeEnum()) {
			Map<String, Object> redisMap = new HashMap<String, Object>();
			redisMap.put("uid", liveUser.getId());
			redisMap.put("status", liveUser.getUserStatus());

			RedisEventData redisEventData = new RedisEventData();
			redisEventData.setRedisEventType(RedisEventData.RedisEventType.AccountManage.get());
			redisEventData.setRedisEventData(redisMap);
			redisClient.publishGameServer(JSON.toJSONString(redisEventData));
		} else if(LiveUserType.GENERAL_PROXY == liveUser.getTypeEnum() || 
				LiveUserType.PROXY == liveUser.getTypeEnum()){
			List<AccountVO> userList = liveUserService.findOnlineAllUserByParentId(liveUser.getParentPath());
			if(userList != null && userList.size() > 0) {
				List<AccountVO> onlineUserList = userList.stream().filter( item -> {
					return item.isOnline() && LiveUserType.MEMBER == item.getTypeEnum();
				}).collect(Collectors.toList());
				
				onlineUserList.forEach( item -> {
					Map<String, Object> redisMap = new HashMap<String, Object>();
					redisMap.put("uid", item.getId());
					redisMap.put("status", item.getUserStatus());

					RedisEventData redisEventData = new RedisEventData();
					redisEventData.setRedisEventType(RedisEventData.RedisEventType.AccountManage.get());
					redisEventData.setRedisEventData(redisMap);
					redisClient.publishGameServer(JSON.toJSONString(redisEventData));
				});
			}
		}
	}
	
	@Override
	public void modifyBetStatus(Long id, LiveUserType liveUserType,
			LiveUserIsBet isBet) {
		LiveUser liveUser = liveUserService.modifyBetStatus(id, liveUserType, isBet);
		
		if (LiveUserType.MEMBER == liveUser.getTypeEnum()) {
			Map<String, Object> redisMap = new HashMap<String, Object>();
			redisMap.put("uid", liveUser.getId());
			redisMap.put("isbet", liveUser.getIsBet());

			RedisEventData redisEventData = new RedisEventData();
			redisEventData.setRedisEventType(RedisEventData.RedisEventType.AccountManage.get());
			redisEventData.setRedisEventData(redisMap);
			redisClient.publishGameServer(JSON.toJSONString(redisEventData));
		} else if(LiveUserType.GENERAL_PROXY == liveUser.getTypeEnum() || 
				LiveUserType.PROXY == liveUser.getTypeEnum()){
			List<AccountVO> userList = liveUserService.findOnlineAllUserByParentId(liveUser.getParentPath());
			if(userList != null && userList.size() > 0) {
				List<AccountVO> onlineUserList = userList.stream().filter( item -> {
					return item.isOnline() && LiveUserType.MEMBER == item.getTypeEnum();
				}).collect(Collectors.toList());
				
				onlineUserList.forEach( item -> {
					Map<String, Object> redisMap = new HashMap<String, Object>();
					redisMap.put("uid", item.getId());
					redisMap.put("isbet", item.getIsBet());

					RedisEventData redisEventData = new RedisEventData();
					redisEventData.setRedisEventType(RedisEventData.RedisEventType.AccountManage.get());
					redisEventData.setRedisEventData(redisMap);
					redisClient.publishGameServer(JSON.toJSONString(redisEventData));
				});
			}
		}
		
	}
}
