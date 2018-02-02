package com.na.user.socketserver.aop;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.na.user.socketserver.annotation.I18NField;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.entity.I18NPO;

/**
 * 数据库动态国际化拦截器
 * 
 * @author alan
 * @date 2017年6月22日 下午3:14:36
 */
@Intercepts({
	@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),  
	@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),  
//	@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}),
//	@Signature(type=StatementHandler.class,method="prepare",args={Connection.class,Integer.class})
})
public class I18NInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object result = invocation.proceed(); //执行请求方法，并将所得结果保存到result中
        if (result instanceof ArrayList) {
            ArrayList resultList = (ArrayList) result;
            for (Object o : resultList) {
            	if(o == null) continue;
            	List<Field> fieldList = new ArrayList<>();
            	Class tempClass = o.getClass();
            	while(tempClass != null) {
            		fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            		tempClass = tempClass.getSuperclass();
            	}
            	for(Field field : fieldList) {
            		field.setAccessible(true);
            		I18NField i18NField = field.getAnnotation(I18NField.class);
            		if(i18NField != null) {
            			I18NPO i18NPO = AppCache.getI18NMap(String.valueOf(field.get(o)));
            			if(i18NPO != null) {
            				field.set(o, i18NPO.getZh());
            			}
            		}
            	}
            }
        } else if (result instanceof Map) {
        	
        }
		
        return result;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		
	}

}
