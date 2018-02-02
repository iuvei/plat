package com.na.user.socketserver.common.enums;

/**
 * @author andy
 * @date 2017年6月28日 下午1:45:28
 * 
 */
public enum IpBlackWhiteType {
	UNKNOWN(0, "未知"), WHITE(1, "白名单"), BLACK(2, "黑名单"),
	;
	private int val;
	private String desc;

	IpBlackWhiteType(int val, String desc) {
		this.val = val;
		this.desc = desc;
	}

	public int get() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	public static IpBlackWhiteType get(int val) {
		for (IpBlackWhiteType type : IpBlackWhiteType.values()) {
			if (type.get() == val) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
