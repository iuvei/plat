package com.na.gate.proto.bean;

import com.na.gate.proto.base.MyField;
import com.na.gate.proto.base.Request;

/**
 * Created by sunny on 2017/7/31 0031.
 */
public class MerchantRequest implements Request {
	@MyField(order = 0)
	private String json;

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	@Override
	public String toString() {
		return "MerchantRequest{" + "json='" + json + '\'' + '}';
	}
}
