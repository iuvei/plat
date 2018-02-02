package com.na.manager.common.websocket;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.*;
import com.na.manager.common.I18nMessage;
import com.na.manager.common.SpringContextUtil;
import com.na.manager.common.redis.RedisClient;
import com.na.manager.entity.User;
import com.na.manager.enums.RedisKeyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by sunny on 2017/6/23 0023.
 */
@Component
@ServerEndpoint("/websocket")
public class MyWebSocket implements MessageListener {
    private final static Logger log = LoggerFactory.getLogger(MyWebSocket.class);

    private static LinkedList sessionMap = new LinkedList();
    @Autowired
    private RedisTemplate redisTemplate ;
    @Autowired
    private I18nMessage i18nMessage;

    public MyWebSocket(){

    }

    @OnOpen
    public void onOpen(Session session){
    }

    @OnClose
    public void onClose(Session session){
        synchronized (sessionMap) {
            Iterator<Client> it = sessionMap.iterator();
            while (it.hasNext()) {
                Client map = it.next();
                if (map.getSession() == session) {
                    it.remove();
                    log.info("移除一个{}");
                }
            }
        }
    }

    @OnMessage
    public void onWebSocketMessage(String message,Session session) {
        synchronized (sessionMap) {
            RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtil.getBean("redisTemplate");
            MyMessage msg = JSONObject.parseObject(message, MyMessage.class);
            if (msg.getCmd() == 1) {
                User currentUser = (User) redisTemplate.boundValueOps(RedisKeyEnum.USER_LOGIN_TOEKN.get(msg.getText())).get();
                if (currentUser != null) {
                    sessionMap.add(new Client(session, currentUser));
                }
            }
        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        synchronized (sessionMap) {
            String msg = new String(message.getBody());
            String data[] = msg.split(",");

            Long userId = Long.valueOf(data[0]);
            String token = data[1];
            String ip = data[2];

            Iterator<Client> it = sessionMap.iterator();
            while (it.hasNext()) {
                Client client = it.next();
                if (client.getUser().getId().equals(userId) && !client.getUser().getToken().equals(token)) {
                    try {
                        redisTemplate.delete(RedisKeyEnum.USER_LOGIN_TOEKN.get(client.getUser().getToken()));
                        String tips = i18nMessage.getMessage("user.force.exit", new Object[]{ip});
                        MyMessage logout = new MyMessage(CmdEnum.LOGOUT, tips);

                        if (client.getSession().isOpen()) {
                            client.getSession().getBasicRemote().sendText(JSONObject.toJSONString(logout));
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(),e);
                    }
                }
            }
        }

    }
}
