package com.gameportal.web.api.sa;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("VerifyUsernameResponse")
public class VerifyUsername extends UserInfoResponse{

	@XStreamAlias("IsExist")
	private boolean exist;
	
	public VerifyUsername(){}

	public boolean getExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}
}
