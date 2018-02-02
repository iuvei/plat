package com.na.baccarat.socketserver.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.na.baccarat.socketserver.common.enums.TradeItemEnum;

/**
 * 竞咪游戏桌子
 */
public class BeingMiGameTable extends GameTable implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 旁注用户
	 * key为用户ID
	 */
	private List<Long> besideUser;
	
	/**
	 * 当前桌用户
	 * key 为座位号 
	 */
	private Map<Integer, Long> players = new ConcurrentHashMap<>();
	
	public List<Long> getBesideUser() {
		if(besideUser == null) {
			besideUser = new ArrayList<>();
		}
		return besideUser;
	}

	public void setBesideUser(List<Long> besideUser) {
		this.besideUser = besideUser;
	}

	public Map<Integer, Long> getPlayers() {
		return players;
	}

	public void setPlayers(Map<Integer, Long> players) {
		this.players = players;
	}

	public void setBankPlayerMap(TradeItemEnum tradeItemEnum, User user) {
		Set<User> list = new HashSet<>(1);
		list.add(user);
		super.getBankPlayerMap().put(tradeItemEnum, list);
	}
	
	
	
}
