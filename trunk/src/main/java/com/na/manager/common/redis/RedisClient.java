package com.na.manager.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisClient {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private StringRedisTemplate stringRedisTemplate ;
	
	
	public void publish(String channel, String message) {
		log.info("【Redis发布】   [{}]更新,内容：[{}]",channel,message);
		stringRedisTemplate.convertAndSend(channel, message);
	}
	
	public void publishGameServer( String message) {
		publish("GameRedisEvent",message);
	}
}
