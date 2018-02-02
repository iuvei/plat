package com.na.baccarat.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 洗牌结束请求参数
 * 
 * @author alan
 * @date 2017年4月29日 下午4:37:36
 */
public class SetCardPara extends CommandReqestPara {
	
    /**
     * 卡片类型 H红桃  D方片  S黑桃 C梅花
     */
    @JSONField(name = "ct")
    private String cardType;
    /**
     * 卡片数字
     */
    @JSONField(name = "cn")
    private Integer cardNum;
    /**
     * 卡片位置
     */
    @JSONField(name = "i")
    private Integer index;
    
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Integer getCardNum() {
		return cardNum;
	}
	public void setCardNum(Integer cardNum) {
		this.cardNum = cardNum;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}

    
	
    
}
