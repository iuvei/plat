package com.na.manager.enums;

/**
 * Created by sunny on 2017/6/21 0021.
 */
public enum LiveUserType {
    UNKNOWN(0,"未知"),
    PROXY(1,"代理"),
    MEMBER(2,"会员"),
    GENERAL_PROXY(3,"总代理")

    ;
    private int val;
    private String desc;

    LiveUserType(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static LiveUserType get(int val){
        for (LiveUserType userType : LiveUserType.values()) {
            if(userType.get()==val){
                return userType;
            }
        }
        return UNKNOWN;
    }
}
