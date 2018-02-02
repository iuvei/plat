package com.na.manager.enums;

/**
 * @author andy
 * @date 2017年7月1日 下午6:29:12
 * 
 */
public enum LoginStatusEnum {
	SUCCESS(0, "成功"), 
	FAIL(1, "失败"), 
	UNKNOWN(3, "未知"),
	;
	
	private int val;
	private String desc;

	LoginStatusEnum(int val, String desc) {
		this.val = val;
		this.desc = desc;
	}

	public int get() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	public static LoginStatusEnum get(int val) {
		for (LoginStatusEnum status : LoginStatusEnum.values()) {
			if (status.get() == val) {
				return status;
			}
		}
		return UNKNOWN;
	}
}
