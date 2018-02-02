package com.na.baccarat.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;

public class ChangeDealerResponse implements IResponse{

	@JSONField(name = "dpic")
	private String dealerPic;
	@JSONField(name = "duid")
	private Long dealerUid;
	@JSONField(name = "dname")
	private String dealerName;
	@JSONField(name = "loginName")
	private String loginName;

	public String getDealerPic() {
		return dealerPic;
	}
	public void setDealerPic(String dealerPic) {
		this.dealerPic = dealerPic;
	}
	public Long getDealerUid() {
		return dealerUid;
	}
	public void setDealerUid(Long dealerUid) {
		this.dealerUid = dealerUid;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
