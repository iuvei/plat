package com.na.manager.bean;

/**
 * 登陆有两种策略.
 * 1. 通过用户名\密码\验证码 登陆
 * 2. 通过token登陆
 * Created by sunny on 2017/6/21 0021.
 */
public class LoginRequest {
    public String username;
    public String password;
    public String captcha;
    public String token;
    public Integer language;
}
