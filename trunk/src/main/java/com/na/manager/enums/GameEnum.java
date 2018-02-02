package com.na.manager.enums;

/**
 * @author Andy
 * @version 创建时间：2017年7月27日 上午10:46:11
 */
public enum GameEnum {
	UNKNOWN(0, "未知"), BACCARAT(1, "百家乐"), DRAGONTIGER(2, "龙虎"), ROULETTE(3, "轮盘"), SICBO(4, "骰宝");
	private int val;
	private String desc;

	GameEnum(int val, String desc) {
		this.val = val;
		this.desc = desc;
	}

	public int get() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	public static GameEnum get(int val) {
		for (GameEnum userType : GameEnum.values()) {
			if (userType.get() == val) {
				return userType;
			}
		}
		return UNKNOWN;
	}
}
