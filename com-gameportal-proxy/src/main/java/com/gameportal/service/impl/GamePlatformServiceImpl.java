package com.gameportal.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameportal.domain.GamePlatform;
import com.gameportal.persistence.GamePlatFormMapper;
import com.gameportal.service.IGamePlatformService;

@Service("iGamePlatformService")
public class GamePlatformServiceImpl implements IGamePlatformService {
	
	@Autowired
	private GamePlatFormMapper gamePlatformMapper;
	
	@Override
	public List<GamePlatform> getGameList(Map<String, Object> params) {
		return gamePlatformMapper.findGamePlatform(params);
	}
}
