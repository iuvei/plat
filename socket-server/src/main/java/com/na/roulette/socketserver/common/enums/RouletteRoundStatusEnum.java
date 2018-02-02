package com.na.roulette.socketserver.common.enums;

/**
 * 局状态枚举。
 * Created by sunny on 2017/4/28 0028.
 */
public enum  RouletteRoundStatusEnum {
	
    INACTIVE(0,"初始化"),
    NEWGAME(1,"新一局"),
    BETTING(2,"投注中"),
    AWAITING_RESULT(3,"截止投注"),
    FINISH(4,"结算完成"),
    CLOSED(5,"CLOSE"),
    PAUSE(6,"暂停");
    
    private String desc;
    private int val;

    RouletteRoundStatusEnum(int val,String desc){
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public static RouletteRoundStatusEnum get(int val){
        for(RouletteRoundStatusEnum item : RouletteRoundStatusEnum.values()){
            if(item.get()==val){
                return item;
            }
        }
        return null;
    }
}
