package com.gameportal.manage.api.sa;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("LoginRequestTryToPlayResponse")
public class LoginRequestTryToPlay extends EGameLoginResponse{
	
	@XStreamAlias("SlotGameURL")
	private String slotGameURL;

	public String getSlotGameURL() {
		return slotGameURL;
	}

	public void setSlotGameURL(String slotGameURL) {
		this.slotGameURL = slotGameURL;
	}
}
