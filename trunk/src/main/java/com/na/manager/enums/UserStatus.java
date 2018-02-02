package com.na.manager.enums;

/**
 * Created by sunny on 2017/6/21 0021.
 */
public enum  UserStatus {
    UNKNOWN(0,"未知"),
    ENABLED(1,"启用"),
    DIS_ENABLED(2,"停用"),
    LOCK(3,"锁定"),
    ;
    private int val;
    private String desc;

    UserStatus(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static UserStatus get(int val){
        for (UserStatus userType : UserStatus.values()) {
            if(userType.get()==val){
                return userType;
            }
        }
        return UNKNOWN;
    }
}
