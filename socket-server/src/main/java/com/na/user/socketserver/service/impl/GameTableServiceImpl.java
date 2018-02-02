package com.na.user.socketserver.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.user.socketserver.dao.IGameTableMapper;
import com.na.user.socketserver.entity.GameTablePO;
import com.na.user.socketserver.service.IGameTableService;
import com.na.user.socketserver.service.IPlayService;
import com.na.user.socketserver.service.IVirtualGameTableService;

/**
 * Created by sunny on 2017/4/26 0026.
 */
@Service
public class GameTableServiceImpl implements IGameTableService{
	
	@Autowired
    private IGameTableMapper gameTableMapper;
	

	@Autowired
	private IVirtualGameTableService virtualGameTableService;
	
	@Autowired
	private IPlayService playService;
	
	
	public GameTablePO findGametableByTid(int tid) {
		return gameTableMapper.findGametableByTid(tid);
	}
	
	@Override
	public List<GameTablePO> getTableByGame(int gameId, int status) {
		return gameTableMapper.findByGid(gameId, 0);
	}
	
	
}
