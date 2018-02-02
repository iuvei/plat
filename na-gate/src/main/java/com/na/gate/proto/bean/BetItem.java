package com.na.gate.proto.bean;

import com.na.gate.proto.base.MyField;
import com.na.gate.proto.base.Request;
import com.na.gate.proto.base.STypeEnum;

/**
 * @author Andy
 * @version 创建时间：2017年11月16日 下午4:49:13
 */
public class BetItem implements Request{
	@MyField(order = 0, sourceType = STypeEnum.UINT32)
	private long userId;

	@MyField(order = 1, sourceType = STypeEnum.UINT32)
	private Double betTotalAmount;

	public BetItem() {
	}

	public BetItem(long userId, Double betTotalAmount) {
		this.userId = userId;
		this.betTotalAmount = betTotalAmount;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Double getBetTotalAmount() {
		return betTotalAmount;
	}

	public void setBetTotalAmount(Double betTotalAmount) {
		this.betTotalAmount = betTotalAmount;
	}
}
