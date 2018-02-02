package com.na.test.batchbet.para;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * 用户下注结果
 * 
 * @author alan
 * @date 2017年5月1日 下午12:26:08
 */
public class UserBetResultResponse {
	
	/**
	 * 卡片信息
	 */
	public static class CardInfo {
		 @JSONField(name = "type")
		 public String type;
		 
		 @JSONField(name = "number")
		 public Integer number;
		 
		 @JSONField(name = "index")
		 public Integer index;
	}
	
	/**
	 * 交易项结果信息
	 */
	public static class ResultInfo {
		 @JSONField(name = "type")
		 public String type;
		 
		 @JSONField(name = "number")
		 public BigDecimal number;
	}
	/**
     * 游戏结果
     */
    private String result;
    /**
     * 赢取金额
     */
    private BigDecimal money;
    /**
     * 牌
     */
    @JSONField(name = "cardList")
    private List<CardInfo> cardList;
    
    
    private Integer source;
    
    /**
     * 每个交易项输赢
     */
    @JSONField(name = "resultList")
    private List<ResultInfo> resultList;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public List<CardInfo> getCardList() {
		return cardList;
	}

	public void setCardList(List<CardInfo> cardList) {
		this.cardList = cardList;
	}

	public List<ResultInfo> getResultList() {
		return resultList;
	}

	public void setResultList(List<ResultInfo> resultList) {
		this.resultList = resultList;
	}
    
    
    
}
