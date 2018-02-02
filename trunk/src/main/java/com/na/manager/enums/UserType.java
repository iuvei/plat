package com.na.manager.enums;

/**
 * Created by sunny on 2017/6/21 0021.
 */
public enum UserType {
    UNKNOWN(0,"未知"),
    SYS(1,"系统用户"),
    DEALER(2,"荷官"),
    SUB_ACCOUNT(3,"子账号"),
    LIVE(4,"真人")
    ;
    private int val;
    private String desc;

    UserType(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static UserType get(int val){
        for (UserType userType : UserType.values()) {
            if(userType.get()==val){
                return userType;
            }
        }
        return UNKNOWN;
    }
}
