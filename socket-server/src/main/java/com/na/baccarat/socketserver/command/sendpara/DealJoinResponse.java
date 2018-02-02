package com.na.baccarat.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;


/**
 * 荷官加入桌子响应
 * 
 * @author alan
 * @date 2017年5月3日 下午5:11:25
 */
public class DealJoinResponse implements IResponse {
	
	@JSONField(name = "nickName")
	private String nickName;
	
	
	@JSONField(name = "dealerPic")
	private String dealerPic;
	
	@JSONField(name = "tid")
	private Integer tableId;
	
	
	
	
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	public String getDealerPic() {
		return dealerPic;
	}
	public void setDealerPic(String dealerPic) {
		this.dealerPic = dealerPic;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
	
}
