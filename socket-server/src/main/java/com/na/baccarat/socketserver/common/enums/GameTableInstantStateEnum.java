package com.na.baccarat.socketserver.common.enums;

/**
 * 桌子即时状态
 */
public enum GameTableInstantStateEnum {
	/**
	 * 0：就是新一靴  2：洗牌中 3：新一局
	 * 
	 * 4：下注中 5：等待开奖 6：本局结束 7：游戏关闭
	 * 
	 * 游戏暂停 Pause 8
	 */
	INACTIVE(0,"空闲"),
	SHUFFLE(2,"洗牌中"),
	NEWGAME(3,"新一局"),
	BETTING(4,"下注中"),
	AWAITING_RESULT(5,"等待开奖"),
	FINISH(6,"本局结束"),
	CLOSED(7,"游戏关闭"),
	PAUSE(8,"游戏暂停");


	int val;
	String desc;

	GameTableInstantStateEnum(int status, String desc) {
		this.val = status;
		this.desc = desc;
	}

	public int get() {
		return val;
	}
	
	public String getDesc() {
		return desc;
	}

	public static GameTableInstantStateEnum get(int status) {
		for(GameTableInstantStateEnum item : GameTableInstantStateEnum.values()){
			if(item.get()==status){
				return item;
			}
		}
		return null;
	}
}
