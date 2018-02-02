package com.na.gate.dao;

import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * 平台玩家登录记录。
 * Created by sunny on 2017/8/9 0009.
 */
@Mapper
public interface IPlatformUserLoginMapper {
    @Insert("INSERT INTO `platform_user_login` (`user_id`) values(#{userId})")
    void add(@Param("userId") Long userId);

    @Delete("DELETE from platform_user_login where user_id=#{userId}")
    void delete(Long userId);

    @Select("select login_time from platform_user_login where user_id=#{userId}")
    Date findBy(Long userId);
}
