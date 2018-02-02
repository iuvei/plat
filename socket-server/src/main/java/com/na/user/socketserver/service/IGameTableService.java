package com.na.user.socketserver.service;

import java.util.List;

import com.na.user.socketserver.entity.GameTablePO;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
public interface IGameTableService {
	
	public GameTablePO findGametableByTid(int tid);
	
	public List<GameTablePO> getTableByGame(int gameId, int status);
	
}
