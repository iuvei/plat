package com.na.gate.proto.bean;

import com.na.gate.enums.SyncTypeEnum;
import com.na.gate.proto.base.MyField;
import com.na.gate.proto.base.Request;
import com.na.gate.proto.base.STypeEnum;

/**
 * 真人玩家进入游戏服务器协议. Created by sunny on 2017/7/24 0024.
 */
public class LivePlayerLoginRequest implements Request {
	@MyField(order = 0, sourceType = STypeEnum.INT8)
	private Integer type = SyncTypeEnum.INCREMENT.get();
	
	@MyField(order = 1, sourceType = STypeEnum.INT32)
	private Integer count;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
