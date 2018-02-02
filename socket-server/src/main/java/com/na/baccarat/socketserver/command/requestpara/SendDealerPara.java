package com.na.baccarat.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 给荷官发牌请求参数
 * 
 * @author alan
 * @date 2017年4月29日 下午4:37:36
 */
public class SendDealerPara extends CommandReqestPara {
	
    /**
     * 消息类型
     */
    @JSONField(name = "type")
    private String type;
    
    
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
    
    
}
