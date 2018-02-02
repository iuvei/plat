package com.na.manager.bean.vo;

import java.util.List;

/**
 * @author Andy
 * @version 创建时间：2017年12月22日 下午5:06:21
 */
public class BetRankVO {
	private List<Object> data;

	private String label;

	public BetRankVO(List<Object> data, String label) {
		super();
		this.data = data;
		this.label = label;
	}

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
