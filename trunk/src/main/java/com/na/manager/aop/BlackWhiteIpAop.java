package com.na.manager.aop;


import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.google.common.base.Preconditions;
import com.na.manager.cache.AppCache;
import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.IpBlackWhiteAddr;
import com.na.manager.entity.User;
import com.na.manager.enums.IpBlackWhitePlatType;
import com.na.manager.enums.IpBlackWhiteType;
import com.na.manager.enums.UserType;
import com.na.manager.util.IpUtils;
import com.na.manager.util.RequestUtil;

/**
 *  IP黑白名单拦截器。<br>
 *  Created by sunny on 2017/6/8 0008.
 */
@Aspect
@Configuration
@Order(3)
public class BlackWhiteIpAop {
    private static final Logger log = LoggerFactory.getLogger(BlackWhiteIpAop.class);
    // 定义切点Pointcut
    @Pointcut("execution(* com.na.manager.action.*Action.*(..))")
    public void excudeService() {
    }
    
    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    	log.debug("访问ip过滤开始...");
        String ip = RequestUtil.getIpAddr();
		int type = -5;
		User user = AppCache.getCurrentUser();
		if (user == null) {
			Object result = pjp.proceed();
			return result;
		};
		if (user.getUserTypeEnum() == UserType.SUB_ACCOUNT) { // 子账号
			ChildAccountUser childAccountUser = (ChildAccountUser)user;
			user =childAccountUser.getParentUser();
		}
		if(user.getUserTypeEnum() == UserType.SYS) {
			type = IpBlackWhitePlatType.SYSTEM.get();
		} else if(user.getUserTypeEnum() == UserType.LIVE){
			type = IpBlackWhitePlatType.MANAGE.get();
		}
		String whiteKey = StringUtils.join(new Object[] { type, IpBlackWhiteType.WHITE.get() }, ".");
		String blackKey = StringUtils.join(new Object[] { type, IpBlackWhiteType.BLACK.get() }, ".");
		/**
		 * 1、如果设置了白名单，且访问IP在其内，则直接放行。
		 * 2、如果没有设置白名单或者不在其内，但设置了黑名单，且访问IP在其内，则直接拦截。
		 * 3、如果没有设置黑白名单、直接放行。
		 */
		if(AppCache.getBlackWhiteIpMap().containsKey(whiteKey) && validateIp(whiteKey, ip)){
			Object result = pjp.proceed();
			return result;
		}

		if(AppCache.getBlackWhiteIpMap().containsKey(blackKey) && validateIp(blackKey, ip)) {
			log.info("ip[{}]访问受限",ip);
			Preconditions.checkArgument(false,"ip.is.blocked");
		}
		Object result = pjp.proceed();
		return result;
    }
    
    public boolean validateIp(String key,String ip){
    	boolean flag = false;
    	long ipNum = IpUtils.ip2Num(ip);
    	for(IpBlackWhiteAddr ipAddr : AppCache.getBlackWhiteIpMap().get(key)){
    		if(ipNum>=ipAddr.getStartNum() && ipNum<=ipAddr.getEndNum()){
    			flag =true;
    			break;
    		}
    	}
    	return flag;
    }
}
