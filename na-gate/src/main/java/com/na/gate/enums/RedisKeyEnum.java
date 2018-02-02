package com.na.gate.enums;

/**
 * Created by Administrator on 2017/6/22 0022.
 */
public enum RedisKeyEnum {
    CAPTCHA_ATTR("admin.captcha.",60,"验证码"),
    USER_LOGIN_TOEKN("admin.session.token.",30*60,"后台系统TOKEN"),
    TRY_USER_LOGIN_TOKEN("admin.try.session.token",0,"后台系统试玩代理TOKEN"),
    EVENT_MANAGE_FORCE_EXIT("event.admin.manage.force.exit",0,"管理系统强制退出事件"),
    IP_BLACK_WHITE_LIST("ip.black.white.list",0,"黑白名单IP列表"),
    PLATFORM_USER_TOKEN("admin.session.platform.token.",60,"平台用户使用token登陆"),
    PLATFORM_GAME_USER_TOKEN("game.session.platform.authcode.",0,"平台玩家登陆验证码"),
    ;

    private String key;
    private String desc;
    //有效期，单位：秒
    private int ttl;

    RedisKeyEnum(String key, int ttl, String desc) {
        this.key = key;
        this.ttl = ttl;
        this.desc = desc;
    }

    public String get() {
        return key;
    }

    /**返回拼凑好的key*/
    public String get(String val){
        return new StringBuilder(this.key).append(val).toString();
    }

    public int getTtl() {
        return ttl;
    }

    public String getDesc() {
        return desc;
    }
}
