package com.na.manager.facade.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.na.manager.bean.RoundCorrectDataRequest;
import com.na.manager.common.redis.RedisClient;
import com.na.manager.common.redis.RedisEventData;
import com.na.manager.entity.LiveUser;
import com.na.manager.enums.AccountRecordType;
import com.na.manager.enums.ChangeBalanceTypeEnum;
import com.na.manager.enums.LiveUserType;
import com.na.manager.enums.UserType;
import com.na.manager.facade.IFinancialFacade;
import com.na.manager.service.IBetOrderService;
import com.na.manager.service.ILiveUserService;

/**
 * 资金操作
 * 
 * @author andy
 * @date 2017年7月3日 下午3:07:51
 * 
 */
@Component
public class FinancialFacadeImpl implements IFinancialFacade {
	private Logger logger =LoggerFactory.getLogger(FinancialFacadeImpl.class);
	@Autowired
	private ILiveUserService liveUserService;
	@Autowired
	private IBetOrderService betOrderService;
	
	@Value("${spring.na.platform.proxy.path}")
	private String proxyPath; //成都代理网
	
	@Value("${spring.na.platform.merchant.path}")
	private String merchantPath; //成都现金网
		
    @Autowired
	private RedisClient redisClient;
    
    @Override
	public void updateBalance(Long userId, Long parentId, UserType userType, AccountRecordType accountRecordType,
			ChangeBalanceTypeEnum changeType,BigDecimal balance, String remark) {
		liveUserService.updateBalance(userId, parentId, userType, accountRecordType,changeType,balance, remark);
		Map<String, Object> redisMap = new HashMap<String, Object>();
		LiveUser liveUser = liveUserService.findById(userId);
		if (LiveUserType.MEMBER == liveUser.getTypeEnum() && liveUser.getParentPath().indexOf(proxyPath) ==-1
				 && liveUser.getParentPath().indexOf(merchantPath) ==-1) {
			redisMap.put("uid", liveUser.getId());
			redisMap.put("newBalance", liveUser.getBalance());

			RedisEventData redisEventData = new RedisEventData();
			redisEventData.setRedisEventType(RedisEventData.RedisEventType.ReshAmount.get());
			redisEventData.setRedisEventData(redisMap);
			
			String sendData =JSON.toJSONString(redisEventData);
			logger.info("增量更新{}余额，Redis发送余额变动通知[{}]",liveUser.getLoginName(),sendData);
			redisClient.publishGameServer(sendData);
		}
	}
    
    @Override
	public void modifyBalance(Long userId, Long parentId, UserType userType, AccountRecordType accountRecordType,
			ChangeBalanceTypeEnum changeType,BigDecimal balance, String remark) {
		liveUserService.modifyBalance(userId, parentId, userType, accountRecordType,changeType,balance, remark);
		Map<String, Object> redisMap = new HashMap<String, Object>();
		LiveUser liveUser = liveUserService.findById(userId);
		if (LiveUserType.MEMBER == liveUser.getTypeEnum() && liveUser.getParentPath().indexOf(proxyPath) ==-1
				 && liveUser.getParentPath().indexOf(merchantPath) ==-1) {
			redisMap.put("uid", liveUser.getId());
			redisMap.put("newBalance", liveUser.getBalance());

			RedisEventData redisEventData = new RedisEventData();
			redisEventData.setRedisEventType(RedisEventData.RedisEventType.ReshAmount.get());
			redisEventData.setRedisEventData(redisMap);
			
			String sendData =JSON.toJSONString(redisEventData);
			logger.info("覆盖更新{}余额，Redis发送余额变动通知[{}]",liveUser.getLoginName(),sendData);
			redisClient.publishGameServer(sendData);
		}
	}


	@Override
	public void invalidBetOrders(RoundCorrectDataRequest param) {
		Set<Long> ids =betOrderService.invalidBetOrders(param);
		Map<String, Object> redisMap = null;
		if(!CollectionUtils.isEmpty(ids)){
			for(Long userId :ids){
				redisMap = new HashMap<String, Object>();
				redisMap.put("uid", userId);

				RedisEventData redisEventData = new RedisEventData();
				redisEventData.setRedisEventType(RedisEventData.RedisEventType.ReshAmount.get());
				redisEventData.setRedisEventData(redisMap);
				
				String sendData =JSON.toJSONString(redisEventData);
				logger.info("后台处理异常订单，Redis发送余额变动通知[{}]",sendData);
				redisClient.publishGameServer(sendData);
			}
		}
	}
}
