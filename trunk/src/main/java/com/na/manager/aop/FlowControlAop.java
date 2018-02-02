package com.na.manager.aop;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.RateLimiter;
import com.na.manager.bean.Rate;
import com.na.manager.enums.RedisKeyEnum;
import com.na.manager.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 * 流量控制。<br>
 *     1. 根据用户限流。<br>
 *     2. 根据IP限流。<br>
 * Created by sunny on 2017/8/19 0019.
 */
@Component
public class FlowControlAop extends HandlerInterceptorAdapter {
    @Value("${spring.flowcontrol.user.number}")
    private int userNumber;
    @Value("${spring.flowcontrol.user.timer}")
    private int userTimer;
    @Value("${spring.flowcontrol.ip.number}")
    private int ipNumber;
    @Value("${spring.flowcontrol.ip.timer}")
    private int ipTimer;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private Rate userRate;
    private Rate ipRate;
    private DefaultRedisScript<Long> redisScript;

    @PostConstruct
    public void init(){
        userRate = new Rate(userNumber,userTimer);
        ipRate = new Rate(ipNumber,ipTimer);
        redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/redis-lua/Rate.lua")));
        redisScript.setResultType(Long.class);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = RequestUtil.getIpAddr(request);
        long ipResult = redisTemplate.execute(redisScript, Collections.singletonList(RedisKeyEnum.FLOW_CONTROL_IP.get(ip)), ipRate.getTimer() + "", ipRate.getNumber() + "");
        Preconditions.checkArgument(ipResult==1, "IP访问受限，稍后再试……");

        String auth = request.getHeader("Authorization");
        if (auth!=null) {
            long result = redisTemplate.execute(redisScript, Collections.singletonList(RedisKeyEnum.FLOW_CONTROL_USER.get(auth)), userRate.getTimer() + "", userRate.getNumber() + "");
            Preconditions.checkArgument(result == 1, "操作太快，稍后再试……");
        }

        return super.preHandle(request, response, handler);
    }
}
