package com.na.user.socketserver.dao;

import com.na.baccarat.socketserver.entity.LoginStatus;
import org.apache.ibatis.annotations.*;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
@Mapper
public interface ILoginStatusMapper {
    @Insert("insert into loginstatus(uid,logintype) values(#{uid},#{loginType})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    public void add(LoginStatus loginStatus);

    @Select("select * from loginstatus where uid=#{uid} and loginType=#{loginType} limit 1")
    public LoginStatus getLoginStatus(LoginStatus loginStatus);

    @Delete("delete from loginstatus where id=#{loginId}")
    public void delete(@Param("loginId") Long loginId);
    
    @Delete("delete from loginstatus where uid=#{userId} and loginType=#{loginType}")
    public void deleteByUid(@Param("userId") Long userId, @Param("loginType") Integer loginType);
    
    @Delete("delete from loginstatus")
    public void deleteAll();
}
