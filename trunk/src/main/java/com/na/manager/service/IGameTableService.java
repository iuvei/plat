package com.na.manager.service;

import java.util.List;

import com.na.manager.entity.GameTable;

public interface IGameTableService {

	List<GameTable> listAllTable();

	List<GameTable> listGameTables(Integer gid, String name, Integer status,
			Integer type, Integer page, Integer pagesize);

	Integer countGameTables(Integer gid, String name, Integer status,
			Integer type);

	Boolean saveGameTable(Integer gid, String name, Integer status,
			Integer type, Integer countDownSeconds, Integer isMany,
			Integer miCountdownSeconds, Integer min, Integer max);

	Boolean updateTableStatus(Integer tid, Integer status);

	Boolean updateGameTable(Integer id, Integer gameId, String name, Integer status,
			Integer type, Integer countDownSeconds, Integer isMany,
			Integer miCountdownSeconds, Integer min, Integer max);

	List<Object> listAbnormalTables(Integer gameId, String name);

}
