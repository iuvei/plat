package com.na.manager.enums;

/**
 * @author andy
 * @date 2017年6月24日 上午11:55:51
 * 
 */
public enum StatisType {
	UNKNOWN(0, "未知"), TEAM(1, "代理团队"), AGENT(2, "下级团队"), MEMBERSUM(3, "会员汇总"), MEMBER(4, "会员"),
	;

	public Integer val;
	public String text;

	private StatisType(Integer val, String text) {
		this.val = val;
		this.text = text;
	}

	public int get() {
		return val;
	}
	
	public String getText() {
		return text;
	}

	public static StatisType get(int val) {
		for (StatisType statisType : StatisType.values()) {
			if (statisType.get() == val) {
				return statisType;
			}
		}
		return UNKNOWN;
	}
}
