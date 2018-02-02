package com.na.user.socketserver.aop;

import com.na.user.socketserver.util.TimeLeft;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by sunny on 2017/5/17 0017.
 */
@Aspect
@Component
public class ServiceTimeAop {
    private Logger logger = LoggerFactory.getLogger(ServiceTimeAop.class);

    @Around("execution(public * com.na.*.*.service.impl.*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        TimeLeft timeLeft = new TimeLeft();
        try {
            return pjp.proceed();
        }finally {
            long tt = timeLeft.end();
            if(tt>5) {
                logger.info(String.format("service【%s】花费时间:%d", pjp.getSignature().toString(), timeLeft.end()));
            }
        }
    }
}
