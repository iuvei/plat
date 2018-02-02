package com.na.user.socketserver.listener.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.na.user.socketserver.common.event.AbnormalGameResultEvent;
import com.na.user.socketserver.exception.SocketException;

/**
 * Redis订阅监听器
 * 异常桌台结算
 * 
 * @author alan
 * @date 2017年6月9日 下午2:11:15
 */
@Component
public class AbnormalGameResultRedisListener {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
    private RedisTemplate<String,String> redisTemplate;
	
    public void onMessage(Object message) {
    	
    	Integer params = (Integer) message;
    	
		if (params == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		//通知不同游戏进行异常结算
		applicationContext.publishEvent(new AbnormalGameResultEvent(params));
		
    }
}
