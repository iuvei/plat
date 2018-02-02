package com.na.baccarat.socketserver.common.enums;

/**
 * Created by Administrator on 2017/4/28 0028.
 */
public enum  GameTableTypeEnum {
//    NOR(0,"普通桌"),
//    VIP(1,"包桌"),
//    SUP_VIP(2,"未知");
	MI_NORMAL(1,"咪牌普通桌"),
	NOTMI_NORMAL(2,"不咪牌普通桌"),
	MI_BEING(3,"咪牌竞咪桌"),
	UNKNOWN(0,"未定义"),
	;

    private String desc;
    private int val;

    GameTableTypeEnum(int val,String desc){
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public static GameTableTypeEnum get(int val){
        for(GameTableTypeEnum item : GameTableTypeEnum.values()){
            if(item.get()==val){
                return item;
            }
        }
        return null;
    }
}
