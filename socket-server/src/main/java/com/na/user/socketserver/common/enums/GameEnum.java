package com.na.user.socketserver.common.enums;

/**
 * 游戏Enum
 * 
 * @author alan
 * @date 2017年5月3日 上午11:17:12
 */
public enum GameEnum {
	
	BACCARAT("baccarat","百家乐"),
	ROULETTE("roulette","轮盘"),
	;


	String val;
	String desc;

	GameEnum(String val, String desc) {
		this.val = val;
		this.desc = desc;
	}

	public String get() {
		return val;
	}
	
	public String getDesc() {
		return desc;
	}

	public static GameEnum get(String val) {
		for(GameEnum item : GameEnum.values()){
			if(item.get().equals(val)){
				return item;
			}
		}
		return null;
	}
}