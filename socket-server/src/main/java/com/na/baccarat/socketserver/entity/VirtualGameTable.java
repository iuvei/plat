package com.na.baccarat.socketserver.entity;


import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import com.na.user.socketserver.entity.VirtualGameTablePO;

/**
 * 虚拟游戏桌子
 * 与gameTable现实桌台 一对多关系
 * @author nick
 * @date 2017年4月24日 上午11:43:58
 */
public class VirtualGameTable implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private VirtualGameTablePO virtualGameTablePO;
	/**
	 * 当前桌用户
	 * key 为座位号 
	 */
//	private Map<Integer, Long> players = new ConcurrentHashMap<>();
	
	/**
	 * 下注记录类(仅结算时使用)
	 */
	private List<BetOrder> berOrderList = new CopyOnWriteArrayList<BetOrder>();
	
	/**
	 * 旁注用户
	 * key为用户ID
	 */
	private Set<Long> besideUser;

	public Set<Long> getBesideUser() {
		if(besideUser == null) {
			besideUser = new HashSet<>();
		}
		return besideUser;
	}

	public void setBesideUser(Set<Long> besideUser) {
		this.besideUser = besideUser;
	}
	
//	public Map<Integer, Long> getPlayers() {
//		return players;
//	}
//
//	public void setPlayers(Map<Integer, Long> players) {
//		this.players = players;
//	}

	public List<BetOrder> getBerOrderList() {
		return berOrderList;
	}
	
	public void setBerOrderList(List<BetOrder> berOrderList) {
		this.berOrderList = berOrderList;
	}

	public VirtualGameTablePO getVirtualGameTablePO() {
		return virtualGameTablePO;
	}

	public void setVirtualGameTablePO(VirtualGameTablePO virtualGameTablePO) {
		this.virtualGameTablePO = virtualGameTablePO;
	}


}
