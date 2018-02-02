package com.na.baccarat.socketserver.command.sendpara;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;


/**
 * 用户开牌响应数据
 * @author Administrator
 *
 */
public class UserCardResponse implements Serializable,IResponse {
	
	/**
	 * 卡片信息
	 */
	public class CardInfo implements Serializable {
		 @JSONField(name = "type")
		 public String type;
		 
		 @JSONField(name = "number")
		 public Integer number;
		 
		 @JSONField(name = "index")
		 public Integer index;
	}
	
	private Integer tableId;
	
	private List<CardInfo> cardList;
	
	/**
	 * 是否咪牌
	 */
	private Boolean isMiCard;
	
	/**
	 * 咪牌倒计时
	 */
	private Integer countDownSecond;
	
	/**
	 * 类型
	 */
	private Integer cardType;
	
	/**
	 *咪牌类型
	 */
	private String miType;
	

	public String getMiType() {
		return miType;
	}

	public void setMiType(String miType) {
		this.miType = miType;
	}

	public List<CardInfo> getCardList() {
		return cardList;
	}

	public void setCardList(List<CardInfo> cardList) {
		this.cardList = cardList;
	}

	public Boolean getIsMiCard() {
		return isMiCard;
	}

	public void setIsMiCard(Boolean isMiCard) {
		this.isMiCard = isMiCard;
	}

	public Integer getCountDownSecond() {
		return countDownSecond;
	}

	public void setCountDownSecond(Integer countDownSecond) {
		this.countDownSecond = countDownSecond;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}


	
	
	
}
