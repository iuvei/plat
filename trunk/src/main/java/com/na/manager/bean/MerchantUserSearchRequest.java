package com.na.manager.bean;

/**
 * 商户管理
 * 
 * @author andy
 * @date 2017年6月23日 上午9:49:53
 * 
 */
public class MerchantUserSearchRequest extends PageCondition {
	
	private String parentId;
	
	private String number;
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	
	
}
