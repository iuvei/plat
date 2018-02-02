package com.na.baccarat.socketserver.command.sendpara;

import java.util.Collection;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;

/**
 * 发送荷官消息响应数据
 * 
 * @author alan
 * @date 2017年5月5日 上午10:33:20
 */
public class SendDealerResponse implements IResponse {
   
    @JSONField(name = "tableId")
    private Integer tableId;
    
    @JSONField(name = "cmd")
    private String command;
    
    /**
     * cmd为fanpai时
     * 使用咪牌倒计时
     */
    @JSONField(name = "cardList")
    private Collection<Integer> cardList;
    
    /**
     * cmd为miCD时
     * 使用咪牌倒计时
     */
    @JSONField(name = "miCD")
    private Integer miCoundDown;
    
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	public Collection<Integer> getCardList() {
		return cardList;
	}
	public void setCardList(Collection<Integer> cardList) {
		this.cardList = cardList;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
	public Integer getMiCoundDown() {
		return miCoundDown;
	}
	public void setMiCoundDown(Integer miCoundDown) {
		this.miCoundDown = miCoundDown;
	}
	
}
