package com.na.baccarat.socketserver.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.na.user.socketserver.entity.RoundExtPO;


/**
 * 游戏专有扩展表
 * 
 * @author alan
 * @date 2017年4月28日 上午10:27:35
 */
public class RoundExt implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private RoundExtPO roundExtPO;
	
	/**
	 * SetCard判断  防止重复通知
	 */
	private List<Integer> openCards;
	
	public RoundExt() {
		openCards = new ArrayList<>();
	}

	public RoundExt(RoundExtPO roundExtPO) {
		this.roundExtPO = roundExtPO;
		openCards = new ArrayList<>();
	}

	public RoundExtPO getRoundExtPO() {
		return roundExtPO;
	}

	public void setRoundExtPO(RoundExtPO roundExtPO) {
		this.roundExtPO = roundExtPO;
	}

	public List<Integer> getOpenCards() {
		if(null == openCards) {
			openCards = new ArrayList<>();
		}
		return openCards;
	}
	
	public boolean hasOpenCards(Integer opened) {
		if(openCards.contains(opened)) {
			return true;
		}
		return false;
	}

	public void setOpenCards(List<Integer> openCards) {
		this.openCards = openCards;
	}
	public void addOpenCard(Integer openCards) {
		this.openCards.add(openCards);
	}
	
}