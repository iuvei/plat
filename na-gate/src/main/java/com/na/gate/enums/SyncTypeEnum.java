package com.na.gate.enums;

/**
 * Created by sunny on 2017/6/21 0021.
 */
public enum SyncTypeEnum {
	INCREMENT(1,"增量更新"),
	COVER(2,"全量覆盖"),
    ;
    private int val;
    private String desc;

    SyncTypeEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static SyncTypeEnum get(int val){
        for (SyncTypeEnum userType : SyncTypeEnum.values()) {
            if(userType.get()==val){
                return userType;
            }
        }
        return null;
    }
}
