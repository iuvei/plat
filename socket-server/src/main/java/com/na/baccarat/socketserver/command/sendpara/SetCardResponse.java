package com.na.baccarat.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;

/**
 * 发牌响应参数
 * 
 * @author alan
 * @date 2017年5月2日 上午10:22:29
 */
public class SetCardResponse implements IResponse {
   
    @JSONField(name = "rid")
    private Long roundId;
    
    @JSONField(name = "i")
    private Integer index;

	public Long getRoundId() {
		return roundId;
	}

	public void setRoundId(Long roundId) {
		this.roundId = roundId;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
    
    
    
}
