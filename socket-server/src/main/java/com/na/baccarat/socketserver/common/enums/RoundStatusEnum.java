package com.na.baccarat.socketserver.common.enums;

/**
 * 局状态枚举。
 * Created by sunny on 2017/4/28 0028.
 */
public enum  RoundStatusEnum {
    INACTIVE(0,"初始化"),
    IDLE(1,"新一靴"),
    SHUFFLE(2,"洗牌中"),
    NEWGAME(3,"新一局"),
    BETTING(4,"投注中"),
    AWAITING_RESULT(5,"截止投注"),
    FINISH(6,"结算完成"),
    CLOSED(7,"CLOSE"),
    PAUSE(8,"暂停");
    private String desc;
    private int val;

    RoundStatusEnum(int val,String desc){
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
		return desc;
	}
	public static RoundStatusEnum get(int val){
        for(RoundStatusEnum item : RoundStatusEnum.values()){
            if(item.get()==val){
                return item;
            }
        }
        return null;
    }
}
