package com.na.manager.enums;
/**
* @author Andy
* @version 创建时间：2017年10月4日 上午9:47:18
*/
public enum ChangeBalanceTypeEnum {
	PROXY(1,"代理触发"),
    SELF(2,"玩家触发"),
    ;
    private int val;
    private String desc;

    ChangeBalanceTypeEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static ChangeBalanceTypeEnum get(int val){
        for (ChangeBalanceTypeEnum userType : ChangeBalanceTypeEnum.values()) {
            if(userType.get()==val){
                return userType;
            }
        }
        return null;
    }
}
