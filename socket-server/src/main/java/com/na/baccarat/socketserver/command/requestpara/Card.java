package com.na.baccarat.socketserver.command.requestpara;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * 卡片信息，包含花色 和 点数
 */
public class Card {
	
	private Logger log = LoggerFactory.getLogger(Card.class);
	
	public Card(String cardType, int cardNum) {
		this.cardType = cardType;
		this.cardNum = cardNum;
	}

	/**
	 * 卡片 花色
	 */
	@JSONField(name="ct")
	private String cardType;
	/**
	 * 卡片 点数
	 */
	@JSONField(name="cn")
	private Integer cardNum;
	/**
	 * 是否咪过
	 */
	@JSONField(name="os")
	private boolean openStatus;
	
	
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
	public boolean getOpenStatus() {
		return openStatus;
	}
	public synchronized void setOpenStatus(boolean openStatus) {
		log.debug(cardType + ",  " + cardNum + ",  " + openStatus);
		this.openStatus = openStatus;
	}

}
