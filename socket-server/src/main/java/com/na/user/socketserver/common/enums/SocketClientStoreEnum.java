package com.na.user.socketserver.common.enums;

/**
 * socketIOClient 存储键名称。
 * Created by sunny on 2017/4/28 0028.
 */
public enum SocketClientStoreEnum {
    APP_NAME("an","客户端 属性 -- 游戏名称。加入房间赋予"),
//    LOGIN_ID("login_id","用户登录ID"),
    USER_ID("user_id","用户ID"),
//    USER_TYPE("user_type","用户类型"),
    TABLE_ID("table_id","实体桌ID"),
    VIRTUAL_TABLE_ID("virtual_table_id","虚拟桌ID"),
    GAME_CODE("game_code","游戏GameCode"),
//    DEVICE_TYPE("device_type","设备类型"),
//    DEVICE_INFO("device_info","设备信息"),
    LANG("LANG","语言"),
    DISCONNECT_FLAG("disconnect_flag","主动断线标志位"),
    ;

    private String desc;
    private String val;
    SocketClientStoreEnum(String val,String desc){
        this.val = val;
        this.desc = desc;
    }
    public String get(){return val;}

    public static SocketClientStoreEnum get(int val){
        for(SocketClientStoreEnum item : SocketClientStoreEnum.values()){
            if(item.get().equals(val)){
                return item;
            }
        }
        return null;
    }
}
