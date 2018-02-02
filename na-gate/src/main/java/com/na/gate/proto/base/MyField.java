package com.na.gate.proto.base;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * Created by Administrator on 2017/7/22 0022.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={FIELD})
public @interface MyField {
    int order();
    Class type() default Class.class;
    STypeEnum sourceType() default STypeEnum.UNKNOWN;
}
