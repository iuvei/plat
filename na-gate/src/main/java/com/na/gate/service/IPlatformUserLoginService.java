package com.na.gate.service;

import java.util.Date;

/**
 * 平台用户登录记录服务。
 * Created by sunny on 2017/8/9 0009.
 */
public interface IPlatformUserLoginService {
    void add(Long userId);

    void delete(Long userId);

    Date findBy(Long userId);
}
