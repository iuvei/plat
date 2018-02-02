package com.na.roulette.socketserver.command.sendpara;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.user.socketserver.command.sendpara.CommandResponse;

/**
 * 发送荷官消息响应数据
 * 
 * @author alan
 * @date 2017年5月5日 上午10:33:20
 */
public class RouletteSendDealerResponse extends CommandResponse {
   
    @JSONField(name = "tableId")
    private Integer tableId;
    
    @JSONField(name = "cmd")
	 public String command;
    
    @JSONField(name = "cardList")
    private List<Integer> cardList;
    
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	public List<Integer> getCardList() {
		return cardList;
	}
	public void setCardList(List<Integer> cardList) {
		this.cardList = cardList;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
	/**
     * 返回成功的响应。
     * @param commandEnum
     * @param body
     * @return
     */
    public static RouletteSendDealerResponse createSuccess(RequestCommandEnum commandEnum, Object body){
        RouletteSendDealerResponse res = new RouletteSendDealerResponse();
        res.setType(commandEnum.get());
        res.setMsg(ResponseCommandEnum.OK.get());
        res.setBody(body);
        return res;
    }
}
