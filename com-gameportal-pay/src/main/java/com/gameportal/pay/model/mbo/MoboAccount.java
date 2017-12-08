package com.gameportal.pay.model.mbo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("moboAccount")
public class MoboAccount {
    
	private String signMsg;
	
	@XStreamImplicit(itemFieldName="respData")
	private RespData data = new RespData();

	public String getSignMsg() {
		return signMsg;
	}

	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}

	public RespData getData() {
		return data;
	}

	public void setData(RespData data) {
		this.data = data;
	}
}
