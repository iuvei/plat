package com.na.gate.enums;

/**
 * Created by sunny on 2017/6/21 0021.
 */
public enum PlatformUserAdapterType {
    UNKNOWN(0,"未知"),
    LINE(1,"线路商"),
    MERCHANT(2,"商户"),
    PLAYER(3,"会员"),
    ADMIN(4,"管理员"),
    PROXY(5,"代理"),
    ;
    private int val;
    private String desc;

    PlatformUserAdapterType(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static PlatformUserAdapterType get(int val){
        for (PlatformUserAdapterType userType : PlatformUserAdapterType.values()) {
            if(userType.get()==val){
                return userType;
            }
        }
        return null;
    }
}
