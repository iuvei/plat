package com.na.manager.enums;

/**
 * Created by sunny on 2017/6/21 0021.
 */
public enum LiveUserSource {
    UNKNOWN(0,"未知"),
    PROXY(1,"代理网"),
    CASH(2,"现金网"),
    ALL(3,"全部")
    ;
    private int val;
    private String desc;

    LiveUserSource(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static LiveUserSource get(int val){
        for (LiveUserSource userType : LiveUserSource.values()) {
            if(userType.get()==val){
                return userType;
            }
        }
        return UNKNOWN;
    }
}
