package com.na.user.socketserver.aop;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.na.user.socketserver.config.DatabaseContextHolder;

@Aspect
@Component
public class DataSourceAspect {

	/**
	 * 使用空方法定义切点表达式
	 */
	@Pointcut("execution(* com.na.user.socketserver.dao.*.*(..))")
	public void declareJointPointExpression() {
	}

	/**
	 * 使用定义切点表达式的方法进行切点表达式的引入
	 * 采用Mysql主从复制 读写分离
	 */
	@Before("declareJointPointExpression()")
	public void setDataSourceKey(JoinPoint point) {
		// 获取Method名称
		String methodName = point.getSignature().getName();
		boolean flag = false;
		flag = StringUtils.startsWithAny(methodName, "query", "find", "get");
		if (flag) {
			DatabaseContextHolder.setDatabaseType(DatabaseContextHolder.SLAVE);
		} else {
			DatabaseContextHolder.setDatabaseType(DatabaseContextHolder.MASTER);
		}
	}

}
