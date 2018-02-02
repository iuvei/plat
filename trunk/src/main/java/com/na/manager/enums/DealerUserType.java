package com.na.manager.enums;

/**
 * Created by sunny on 2017/6/21 0021.
 */
public enum DealerUserType {
    UNKNOWN(0,"未知"),
    DEALER(1,"荷官"),
    SUPERVISOR(2,"荷官主管")
    ;
    private int val;
    private String desc;

    DealerUserType(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static DealerUserType get(int val){
        for (DealerUserType userType : DealerUserType.values()) {
            if(userType.get()==val){
                return userType;
            }
        }
        return UNKNOWN;
    }
}
