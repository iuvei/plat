package com.na.manager.enums;

/**
 * v
 *
 * @create 2017-06
 */
public enum BetOrderStatus {

    NEW(1,"新建"),
    CONFIRM(2,"确认"),
    SETTLE(3,"结算"),
    CANCEL(0,"取消"),
    ;
    private int val;
    private String desc;

    BetOrderStatus(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static BetOrderStatus get(int val){
        for (BetOrderStatus status : BetOrderStatus.values()) {
            if(status.get()==val){
                return status;
            }
        }
        return null;
    }
}
