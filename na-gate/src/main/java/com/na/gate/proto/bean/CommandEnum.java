package com.na.gate.proto.bean;

/**
 *
 * Created by sunny on 2017/7/21 0021.
 */
public enum CommandEnum {
    UNKNOWN(0,null,null,null,"未知"),
    HEARTBEAT(1,Direct.LIVE_PLATFORM,Method.REQUEST,HeartbeatRequest.class,"心跳"),
    HANDSHAKE(2,Direct.LIVE_PLATFORM,Method.REQUEST,HandShakeRequest.class,"握手"),
    PLAYER_LOGIN(21,Direct.LIVE_PLATFORM,Method.REQUEST,PlayerLoginRequest.class,"玩家进入游戏服务器"),
    PLAYER_LOGOUT(22,Direct.LIVE_PLATFORM,Method.REQUEST,PlayerLogoutRequest.class,"玩家离开游戏服务器"),
    ACCOUNT_DATA(19,Direct.LIVE_PLATFORM,Method.REQUEST,AccountRequest.class,"游戏服务器投递账单数据"),
    BETORDER_DATA(20,Direct.LIVE_PLATFORM,Method.REQUEST,BetOrderRequest.class,"游戏服务器投递交易记录数据"),
    SEARCH_MERCHANT(35,Direct.LIVE_PLATFORM,Method.REQUEST,MerchantRequest.class,"游戏服务器向中心服务器获取商户信息"),
    PLAYER_REGISTER_RESPOSNE(36,Direct.LIVE_PLATFORM,Method.REQUEST,CreatePlayerResult.class,"游戏服务器向中心服务返回用户登录结果信息"),
    LIVE_PLAYER_LOGIN(38,Direct.LIVE_PLATFORM,Method.REQUEST,LivePlayerLoginRequest.class,"游戏服务器投递机器人数量变化"),
    MYSTERY_AWARD(39,Direct.LIVE_PLATFORM,Method.REQUEST,BetItem.class,"神秘大奖数据"),

    EXCEPTION_RESPONSE(1,Direct.PLATFORM_LIVE,Method.RESPONSE,ExceptionResponse.class,"异常响应"),
    HANDSHAKE_RESPONSE(2,Direct.PLATFORM_LIVE,Method.RESPONSE,HandShakeResponse.class,"握手响应"),
    SEND_PLAY_DATA(4,Direct.PLATFORM_LIVE,Method.REQUEST,PlayerData.class,"中心服务器向游戏服务器发送玩家数据协议"),
    SEND_MERCHANT_DATA(28,Direct.PLATFORM_LIVE,Method.REQUEST,MerchantRequest.class,"中心服务器向游戏服务器发送商户与线路商数据协议"),
    ACCOUNT_CONFIRM_SUCCESS(30,Direct.PLATFORM_LIVE,Method.REQUEST,AccountConfirmSuccessRequest.class,"中心服务器向游戏服务器发送账单确认成功数据协议"),
    PLAYER_EXCEPTION_LOGINOUT(31,Direct.PLATFORM_LIVE,Method.REQUEST,ExceptionLoginoutRequest.class,"中心服务器向游戏服务器玩家异常退出数据协议"),
    ;
    CommandEnum(int val, Direct direct, Method method, Class cls, String desc){
        this.val = val;
        this.desc = desc;
        this.cls = cls;
        this.direct = direct;
        this.method = method;
    }

    private int val;
    private String desc;
    private Class cls;
    private Direct direct;
    private Method method;

    public enum Direct{ PLATFORM_LIVE,LIVE_PLATFORM};
    public enum Method{REQUEST,RESPONSE}

    public int get() {
        return val;
    }
    
    public String getDesc(){
    	return desc;
    }

    public Class getCls() {
        return cls;
    }

    public static CommandEnum get(int val, Direct direct){
        for (CommandEnum requestCommandEnum : CommandEnum.values()) {
            if(direct==requestCommandEnum.direct && val== requestCommandEnum.get()){
                return requestCommandEnum;
            }
        }
        return UNKNOWN;
    }

    public static CommandEnum get(Class cls){
        for (CommandEnum requestCommandEnum : CommandEnum.values()) {
            if(cls== requestCommandEnum.cls){
                return requestCommandEnum;
            }
        }
        return UNKNOWN;
    }

}
