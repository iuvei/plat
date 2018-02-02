package com.na.roulette.socketserver.common.enums;


/**
 * 轮盘桌即时状态
 * @author Administrator
 *
 */
public enum RouletteGameTableInstantStateEnum {


	INACTIVE(0,"空闲"),
	NEWGAME(1,"新一局"),
	BETTING(2,"下注中"),
	AWAITING_RESULT(3,"等待开奖"),
	FINISH(4,"本局结束"),
	CLOSED(5,"游戏关闭"),
	PAUSE(6,"游戏暂停");

	int val;
	String desc;

	RouletteGameTableInstantStateEnum(int status, String desc) {
		this.val = status;
		this.desc = desc;
	}

	public int get() {
		return val;
	}
	
	public String getDesc() {
		return desc;
	}

	public static RouletteGameTableInstantStateEnum get(int status) {
		for(RouletteGameTableInstantStateEnum item : RouletteGameTableInstantStateEnum.values()){
			if(item.get()==status){
				return item;
			}
		}
		return null;
	}
}
