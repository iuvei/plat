package com.na.manager.aop;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.base.Preconditions;
import com.na.manager.cache.AppCache;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.Permission;
import com.na.manager.entity.User;
import com.na.manager.enums.RedisKeyEnum;

/**
 * 权限拦截器.
 * Created by sunny on 2017/6/23 0023.
 */
@Aspect
@Configuration
@Order(1)
public class AuthControlAop {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisTemplate redisTemplate;

    // 定义切点Pointcut
    @Pointcut("execution(* com.na.manager.action.*Action.*(..))")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        HttpServletResponse res = sra.getResponse();
        String token = request.getHeader("Authorization");
        try {
            Class cls = pjp.getSignature().getDeclaringType();
            Optional<Auth> authOptional = getAuth(pjp);

            Preconditions.checkArgument(authOptional.isPresent(),"user.action.no.auth");
            
            User currentUser = null;
            if(StringUtils.isNoneBlank(token)) {
                currentUser = (User) redisTemplate.boundValueOps(RedisKeyEnum.USER_LOGIN_TOEKN.get(token)).get();
                redisTemplate.boundValueOps(RedisKeyEnum.USER_LOGIN_TOEKN.get(token))
                        .expire(RedisKeyEnum.USER_LOGIN_TOEKN.getTtl(), TimeUnit.SECONDS);
                AppCache.setCurrentUser(currentUser);
            }

            if(!authOptional.get().isPublic()){
                Preconditions.checkNotNull(currentUser,"user.token.invalid");

                boolean hasPermission = checkHasPermission(authOptional.get().value(),currentUser);
                Preconditions.checkArgument(hasPermission, "user.has.no.permission");
            }

            Object result = pjp.proceed();
            return result;
        }finally {
        }

    }

    private boolean checkHasPermission(String permissionsName,User user){
        if (user.getPermissions()==null) return false;

        for (Permission permission : user.getPermissions()){
            if (permission.getPermissionName().equalsIgnoreCase(permissionsName)){
                return true;
            }
        }

        return false;
    }

    private Optional<Auth> getAuth(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        Method method1 = methodSignature.getMethod();
        if (method1.isAnnotationPresent(Auth.class) ){
            return Optional.of(method1.getAnnotation(Auth.class));
        }

        Class cls = pjp.getSignature().getDeclaringType();
        if(cls.isAnnotationPresent(Auth.class)){
            return Optional.of((Auth) cls.getAnnotation(Auth.class));
        }
        return Optional.empty();
    }
}
