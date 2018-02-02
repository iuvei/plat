package com.na.manager.aop;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.google.common.collect.Multimap;
import com.na.manager.bean.NaResponse;
import com.na.manager.cache.AppCache;
import com.na.manager.common.annotation.I18NField;
import com.na.manager.entity.I18N;

/**
 * action返回的结果进行国际化
 */
@Aspect
@Configuration
@Order(-11)
public class I18NAop {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 定义切点Pointcut
    @Pointcut("execution(* com.na.manager.action.*Action.*(..)) || execution(* com.na.manager.remote.impl.*RemoteImpl.*(..))")
    public void excudeService() {
    }

    @AfterReturning(returning = "data", pointcut = "excudeService()")
    public void doAfterReturning(Object data) throws Throwable {
        //进行国际化
        if (data != null){
        	if(data instanceof NaResponse) {
	            if (((NaResponse) data).getStatus() == 0 && ((NaResponse) data).getData() !=null) {
	                localObject(((NaResponse) data).getData());
	            }
	        }else if(!data.getClass().isPrimitive()){
            	localObject(data);
            }
        }
    }


    public void localObject(Object data) throws Throwable {
        if (data instanceof List) {
            for (Object obj : (List) data) {
                localObject(obj);
            }
        }else if (data instanceof Set) {
            for (Object obj : (Set) data) {
                localObject(obj);
            }
        }else if (data instanceof Map) {
            for (Object obj : ((Map) data).values()) {
                localObject(obj);
            }
        } else if (data instanceof Multimap) {
            for (Object obj : ((Multimap) data).values()) {
                localObject(obj);
            }
        } else {
            List<Field> fieldList = new ArrayList<>();
            Class tempClass = data.getClass();
            while (tempClass != null) {
                fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
                tempClass = tempClass.getSuperclass();
            }
            for (Field field : fieldList) {
                field.setAccessible(true);
                if (field.get(data) instanceof List ||
                        field.get(data) instanceof Set ||
                           field.get(data) instanceof Map ||
                        field.get(data) instanceof Multimap) {
                    localObject(field.get(data));
                }
            }
            List<Field> fields = FieldUtils.getFieldsListWithAnnotation(data.getClass(), I18NField.class);
            if (fields.size() > 0) {
                localOriginalObject(data);
            }
        }

    }

    private void localOriginalObject(Object data) throws Throwable {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(data.getClass(), I18NField.class);
        for (Field field : fields) {
            field.setAccessible(true);
            I18N i18NPO = AppCache.getI18NMap(String.valueOf(field.get(data)));
            if (i18NPO == null) continue;
            if (AppCache.getCurrentUser() == null) {
                field.set(data, i18NPO.getZh());
            } else {
                switch (AppCache.getCurrentUser().getLanguageType()) {
                    case China:
                        field.set(data, i18NPO.getZh());
                        break;
                    case ChinaTaiwan:
                        field.set(data, i18NPO.getTw());
                        break;
                    case English:
                        field.set(data, i18NPO.getEn());
                        break;
                    case Korean:
                        field.set(data, i18NPO.getKo());
                        break;
                    default:
                        field.set(data, i18NPO.getZh());
                }

            }
        }
    }
}
