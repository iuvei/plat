package com.na.user.socketserver.listener.redis;

import com.alibaba.fastjson.JSONObject;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.common.event.UserBalanceChangeEvent;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.service.IUserService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by Sunny on 2017/6/1 0001.
 */
@Component
public class UserBalanceListener{
  
    @Autowired
    private IUserService userService;

    @Autowired
    private ApplicationContext applicationContext;
    
    public void onMessage(Object message) {
    	JSONObject obj = (JSONObject) message;
        UserPO userPO = AppCache.getLoginUser(obj.getLong("uid"));
        if(userPO!=null) {
            UserPO temp = userService.getUserById(obj.getLong("uid"));
            userPO.setBalance(temp.getBalance());
            Map map = new HashMap<>();
            map.put("user", userPO);
            map.put("affect", "Manage");
            applicationContext.publishEvent(new UserBalanceChangeEvent(map));
        }
    }

    public static class Reshamount{
        public Long uid;
        public Double newBalance;
        public Double addBalance;
    }
}
