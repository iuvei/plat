package com.gameportal.domain;

import net.sf.json.JSONObject;

public class BasePojo {
	public String toJSON(){
		return JSONObject.fromObject(this).toString();
	}
}
