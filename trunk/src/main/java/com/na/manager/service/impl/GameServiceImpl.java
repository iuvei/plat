package com.na.manager.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.manager.dao.IGameMapper;
import com.na.manager.entity.Game;
import com.na.manager.service.IGameService;


@Service
public class GameServiceImpl implements IGameService {
	
	@Autowired
	private IGameMapper gameMapper;

	@Override
	public List<Game> listGameTypes() {
		List<Game> list= gameMapper.listGameTypes();
		return list.stream().filter(item->item.getStatus()==0).collect(Collectors.toList());
	}

}
