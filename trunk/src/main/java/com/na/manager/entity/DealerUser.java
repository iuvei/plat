package com.na.manager.entity;

import com.na.manager.enums.DealerUserType;

import java.io.Serializable;

/**
 * 荷官表
 * @author andy
 * @date 2017年6月22日 上午11:55:57
 * 
 */
public class DealerUser extends User implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	/**
	 * 1 荷官 2荷官主管
	 */
	private Integer type;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public void setTypeEnum(DealerUserType dealerUserType){
		this.type = dealerUserType.get();
	}
	public Integer getTypeEnum(){
		return DealerUserType.get(this.type).get();
	}
}
