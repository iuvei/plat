package com.na.manager.enums;

/**
 * Created by sunny on 2017/6/21 0021.
 */
public enum LiveUserIsBet {
    UNKNOWN(0,"未知"),
    TRUE(1,"允许投注"),
    FALSE(2,"禁止投注")
    ;
    private int val;
    private String desc;

    LiveUserIsBet(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static LiveUserIsBet get(int val){
        for (LiveUserIsBet userType : LiveUserIsBet.values()) {
            if(userType.get()==val){
                return userType;
            }
        }
        return UNKNOWN;
    }
}
