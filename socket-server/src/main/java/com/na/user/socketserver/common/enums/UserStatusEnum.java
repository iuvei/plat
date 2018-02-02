package com.na.user.socketserver.common.enums;

/**
 * 用户状态枚举类。
 * Created by sunny on 2017/4/27 0027.
 */
public enum  UserStatusEnum {
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

    UserStatusEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public static UserStatusEnum get(int val){
        for(UserStatusEnum item : UserStatusEnum.values()){
            if(item.get()==val){
                return item;
            }
        }
        return null;
    }
}
