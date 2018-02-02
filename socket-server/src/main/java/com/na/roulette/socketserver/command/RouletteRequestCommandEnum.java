package com.na.roulette.socketserver.command;

/**
 * 命令集合.
 * <ul>
 * <LI>DEALER_开头的为荷官发出的指令。</LI>
 * <LI>CLIENT_开头的为客户发出的指令。</LI>
 * <LI>COMMON_开头的为多个不同端发出的指令。</LI>
 * <LI>SERVER_开头的为本服务器端发出的指令。</LI>
 * </ul>
 * Created by Sunny on 2017/4/27 0027.
 */
public enum RouletteRequestCommandEnum {
	
	//结算
    COMMON_GAME_RESULT("rouletteGameResult","结算"),
	//离开房间
    COMMON_LEAVE_TABLE("rouletteLeaveTable","离开桌子"),
    //离开房间
    COMMON_OTHER_LEAVE_TABLE("rouletteOtherLeaveTable","别人离开桌子"),
	//桌子状态
	DEALER_TABLESTATUS("rouletteTableStatus","桌子状态"),
	//系统错误
	COMMON_SYSTEM("system","系统问题"),
	//停止下注
	COMMOM_STOP_BET("rouletteStoptBet","停止下注"),
	//新一局
	COMMOM_BEW_GAME("rouletteNewGame","新一局"),
	//开始下注
    DEALER_START_BET ( "rouletteStartBet","开始下注"),
    //清除路子
    DEALER_CLEAR_ROUND ( "rouletteClearRound","清除路子"),
    //用户投注
    CLIENT_BET("rouletteBet","用户投注"),
    //加入桌子
    COMMON_JOIN_TABLE("rouletteJoin","加入桌子"),
    //别人加入桌子
    COMMON_OTHER_JOIN_TABLE("rouletteOtherJoin","别人加入桌子"),
	//荷官进入桌子
    DEALER_JOIN ("rouletteDealerJoin","荷官进入桌子"),
    //荷官离开桌子
    DEALER_LEAVE ("rouletteDealerLeave","荷官离开桌子"),
    CLIENT_QUICK_CHANGE_ROOM("quickChangeRoom","用户快速换桌"),
    ;

    private String val;
    private String desc;
    public String get(){return val;}
    RouletteRequestCommandEnum(String val,String desc){
        this.val = val;
        this.desc = desc;
    }
    public static RouletteRequestCommandEnum get(String val){
        for (RouletteRequestCommandEnum commandEnum : RouletteRequestCommandEnum.values()) {
            if(commandEnum.val.equalsIgnoreCase(val)){
                return commandEnum;
            }
        }
        return null;
    }
}
