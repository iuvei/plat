package com.na.user.socketserver.listener.redis;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisEventListener implements MessageListener {

    private final static Logger log = LoggerFactory.getLogger(UpdateGameTableListener.class);

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;
    @Autowired
    private UserBalanceListener userBalanceListener;
    @Autowired
    private UserAnnounceListener userAnnounceListener;
    @Autowired
    private UpdateGameTableListener updateGameTableListener;
    @Autowired
    private AbnormalGameResultRedisListener abnormalGameResultRedisListener;
    @Autowired
    private AccountManageListener accountManageListener;
    @Autowired
    private UpdateVirtualTableListener updateVirtualTableListener;
    @Autowired
    private UpdateGameConfigListener updateGameConfigListener;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
    	
    	JSONObject jSONObject = (JSONObject) redisTemplate.getValueSerializer().deserialize(message.getBody());
    	RedisEventData redisEventData = jSONObject.toJavaObject(RedisEventData.class);

        if (redisEventData == null) return;
        log.debug("【Redis订阅】   [{}]更新,内容：[{}]", redisEventData.getRedisEventTypeEnum().getDesc(), message.toString());

        switch (redisEventData.getRedisEventTypeEnum()) {
            case AbnormalGameResult:
                abnormalGameResultRedisListener.onMessage(redisEventData.getRedisEventData());
                break;
            case AccountManage:
                accountManageListener.onMessage(redisEventData.getRedisEventData());
                break;
            case ResetPercentage:
                log.debug("洗码比修改");
                break;
            case ReshAmount:
                userBalanceListener.onMessage(redisEventData.getRedisEventData());
                break;
            case UpdateGameTable:
                updateGameTableListener.onMessage(redisEventData.getRedisEventData());
                break;
            case UserAnnounce:
                userAnnounceListener.onMessage(redisEventData.getRedisEventData());
                break;
            case UpdateVirtualTable:
            	updateVirtualTableListener.onMessage(redisEventData.getRedisEventData());
            	break;
            case UpdateGameConfig:
            	updateGameConfigListener.onMessage(redisEventData.getRedisEventData());
            	break;
            default:
                break;
        }

    }


}
