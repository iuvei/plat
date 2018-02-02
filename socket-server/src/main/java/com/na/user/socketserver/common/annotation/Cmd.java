package com.na.user.socketserver.common.annotation;

import java.lang.annotation.*;

/**
 * Created by Sunny on 2017/4/28 0028.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cmd {
    /**
     * 输入参数。
     * @return
     */
    Class<?> paraCls();

    /**
     * 指令名称。
     * @return
     */
    String name();
}
