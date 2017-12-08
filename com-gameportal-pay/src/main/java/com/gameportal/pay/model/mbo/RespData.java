package com.gameportal.pay.model.mbo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("respData")
public class RespData {
	
	private String respCode;
	
	private String respDesc;
	
	private String codeUrl;

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespDesc() {
		return respDesc;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public String getCodeUrl() {
		return codeUrl;
	}

	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}
}
