package com.na.baccarat.socketserver.common.enums;

/**
 * 好路枚举
 * 
 * @author Administrator
 *
 */
public enum GoodRoadsEnum {

	SINGLEJUMPBANK(1), SINGLEJUMPPLAY(2), LONGBANK(3), LONGPLAY(4), ONETOWBANK(
			5), ONETOWPLAY(6), LONGTOWBANK(7), LONGTOWPLAY(8), TOWLONGBANK(9), TOWLONGPLAY(
			10), TWOTOWBANK(11), TWOTOWPLAY(12);

	private Integer val;

	GoodRoadsEnum(Integer val) {
		this.val = val;
	}

	public int get() {
		return val;
	}

	public static GoodRoadsEnum get(int val) {
		for (GoodRoadsEnum item : GoodRoadsEnum.values()) {
			if (item.get() == val) {
				return item;
			}
		}
		return null;
	}
}
