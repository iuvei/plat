package com.na.user.socketserver.service;

import java.util.List;

import com.na.baccarat.socketserver.entity.Play;



/**
 * 玩法
 * 
 * @author alan
 * @date 2017年4月27日 下午3:42:02
 */
public interface IPlayService {
	
	/**
	 * 获取所有玩法
	 */
	List<Play> getAll();
	
	/**
	 * 获取所有玩法
	 */
	List<Play> getPlayByGame(Integer gameId);
}
