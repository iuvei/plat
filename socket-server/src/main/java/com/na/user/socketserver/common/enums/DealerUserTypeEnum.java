package com.na.user.socketserver.common.enums;


/**
 * 用户类型
 */
public enum DealerUserTypeEnum {
	
	DEALER(1, "荷官用户"), CHECKER(2, "荷官主管");

    private int value;
    private String desc;

    public int get() {
        return value;
    }

    DealerUserTypeEnum(int v, String desc) {
        value = v;
    }
    public static DealerUserTypeEnum get(int val){
        for(DealerUserTypeEnum item: DealerUserTypeEnum.values()){
            if(item.value==val){
                return item;
            }
        }
        return null;
    }
}
