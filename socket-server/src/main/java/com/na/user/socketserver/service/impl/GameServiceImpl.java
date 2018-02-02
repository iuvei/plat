package com.na.user.socketserver.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.user.socketserver.dao.IGameMapper;
import com.na.user.socketserver.entity.GamePO;
import com.na.user.socketserver.service.IGameService;

/**
 * Created by sunny on 2017/4/26 0026.
 */
@Service
public class GameServiceImpl implements IGameService {
	
    @Autowired
    private IGameMapper gameMapper;

    @Override
    public List<GamePO> getAllGame() {
        List<GamePO> gameList = gameMapper.findAll();
        gameList.forEach(item->{
            List<Map<String,String>> configList = gameMapper.findGameConfigByGameId(item.getId());
            Map<String,String> configMap = new HashMap<>();
            configList.forEach(config->{
                configMap.put(config.get("key"),config.get("value"));
            });
            item.setGameConfig(configMap);
        });
        return gameList;
    }

	@Override
	public Map<String,String> getConfigById(Integer id) {
		return gameMapper.findGameConfigById(id);
	}
    
    
}
