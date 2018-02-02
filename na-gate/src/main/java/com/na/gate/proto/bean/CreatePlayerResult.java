package com.na.gate.proto.bean;

import com.na.gate.proto.base.MyField;
import com.na.gate.proto.base.Request;
import com.na.gate.proto.base.STypeEnum;

/**
 * @author Andy
 * @version 创建时间：2017年9月27日 下午12:42:36
 */
public class CreatePlayerResult implements Request {
	@MyField(order = 0, sourceType = STypeEnum.UINT32)
	private long userId;

	@MyField(order = 1, sourceType = STypeEnum.INT32)
	private int status; // 0为成功，1需要再次推送，2返回客户端错误信息

	@MyField(order = 2)
	private String msg=""; // 错误信息

	public CreatePlayerResult(long userId, int status, String msg) {
		super();
		this.userId = userId;
		this.status = status;
		this.msg = msg;
	}
	
	public CreatePlayerResult(long userId, int status) {
		super();
		this.userId = userId;
		this.status = status;
	}

	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
