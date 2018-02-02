package com.na.betRobot.entity.enums;

/**
 * 用户状态枚举类。
 * Created by sunny on 2017/4/27 0027.
 */
public enum  UserStatus {
    /**正常*/
    NORMAL(1,"正常"),
    /**停用*/
    LOCKED(2,"停用"),
    /**锁定*/
    FREEZE(3,"锁定");
    private int val;
    private String desc;

    public int get() {
        return val;
    }

    UserStatus(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public static UserStatus get(int val){
        for(UserStatus item : UserStatus.values()){
            if(item.get()==val){
                return item;
            }
        }
        return null;
    }
}
