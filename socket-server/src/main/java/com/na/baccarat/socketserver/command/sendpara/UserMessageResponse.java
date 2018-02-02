package com.na.baccarat.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;


/**
 * 用户通知
 * 
 * @author alan
 * @date 2017年5月3日 下午5:11:25
 */
public class UserMessageResponse implements IResponse {
	
	@JSONField(name = "content")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	
	
	
}
