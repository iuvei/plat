package com.na.manager.aop;

import com.na.manager.cache.AppCache;
import com.na.manager.common.annotation.I18NField;
import com.na.manager.entity.I18N;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 数据库动态国际化拦截器
 *
 * @author alan
 * @date 2017年6月22日 下午3:14:36
 */
@Intercepts({
        /*@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),*/
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
                if (o == null) continue;
                List<Field> fieldList = new ArrayList<>();
                Class tempClass = o.getClass();
                while (tempClass != null) {
                    fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
                    tempClass = tempClass.getSuperclass();
                }
                for (Field field : fieldList) {
                    field.setAccessible(true);
                    I18NField i18NField = field.getAnnotation(I18NField.class);
                    if (i18NField != null) {
                        I18N i18NPO = AppCache.getI18NMap(String.valueOf(field.get(o)));
                        if (i18NPO != null) {
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
