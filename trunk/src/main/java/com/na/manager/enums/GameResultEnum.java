package com.na.manager.enums;

/**
 * 游戏结果
 * 
 * @author andy
 * @date 2017年6月22日 下午12:10:39
 * 
 */
public enum GameResultEnum {
	B(1, "庄"), P(2, "闲"), T(3, "和");

	private int index;
	private String value;

	GameResultEnum(int idx, String val) {
		this.index = idx;
		this.value = val;
	}

	public String getValue() {
		return value;
	}

	public int getIndex() {
		return index;
	}

	public static GameResultEnum get(int val) {
		for (GameResultEnum result : GameResultEnum.values()) {
			if (result.getIndex() == val) {
				return result;
			}
		}
		return null;
	}
}
