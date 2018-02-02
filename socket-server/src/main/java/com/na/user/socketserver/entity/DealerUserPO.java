package com.na.user.socketserver.entity;

import com.na.user.socketserver.common.enums.DealerUserTypeEnum;

/**
 * 荷官用户
 * 
 * @author alan
 * @date 2017年6月21日 下午6:02:11
 */
public class DealerUserPO extends UserPO {
	
	/**
	 * 1 荷官 2荷官主管
	 */
	private Integer type;
	
	/**
	 * 荷官交接班记录
	 */
	private Integer dealerClassRecordId;
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public DealerUserTypeEnum getTypeEnum() {
		return DealerUserTypeEnum.get(this.type);
	}
	public Integer getDealerClassRecordId() {
		return dealerClassRecordId;
	}
	public void setDealerClassRecordId(Integer dealerClassRecordId) {
		this.dealerClassRecordId = dealerClassRecordId;
	}

}
