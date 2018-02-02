package com.na.manager.aop;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.InputStreamSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONArray;
import com.na.manager.cache.AppCache;
import com.na.manager.entity.User;
import com.na.manager.util.RequestUtil;
import com.na.manager.util.TimeLeft;

/**
 * 方法调用计时。
 * Created by sunny on 2017/6/22 0022.
 */
@Aspect
@Configuration
@Order(2)
public class TimeLeftAop {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 定义切点Pointcut
    @Pointcut("execution(* com.na.manager.action.*Action.*(..)) || execution(* com.na.manager.remote.impl.*RemoteImpl.*(..))")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        User currentUser = AppCache.getCurrentUser();
        String ip = RequestUtil.getIpAddr(request);
        Object[] args = pjp.getArgs();
        List<Object> para = new ArrayList<>();
        for (Object item : args){
            if(!(item instanceof ServletRequest
                    || item instanceof ServletResponse
                    || item instanceof InputStreamSource
                    || item instanceof OutputStream
                    || item instanceof InputStream
            )){
                para.add(item);
            }
        }
        MDC.put("eventId", UUID.randomUUID().toString().replace("-",""));

        TimeLeft timeLeft = new TimeLeft();
        try {
            Object result = pjp.proceed();
            return result;
        }finally {
            logger.info("用户 [{}] 通过IP [{}] 请求方法 [{}] ,花费时间 [{}] ,参数为 {}", currentUser==null?"未知" : currentUser.getLoginName(),ip,pjp.getSignature().toShortString(),timeLeft.end(),JSONArray.toJSONString(para));
        }

    }
}
