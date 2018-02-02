package com.na.user.socketserver.annotation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要动态国家化的数据库字段
 * 
 * @author alan
 * @date 2017年6月22日 下午3:17:41
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={FIELD})
public @interface I18NField {
	
}
