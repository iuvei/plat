package com.na.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.na.manager.common.redis.RedisClient;
import com.na.manager.common.redis.RedisEventData;
import com.na.manager.dao.IGameConfigMapper;
import com.na.manager.entity.GameConfig;
import com.na.manager.service.IGameConfigService;

/**
 * v
 *
 * @create 2017-06
 */
@Service
@Transactional(propagation= Propagation.NESTED,rollbackFor=Exception.class)
public class GameConfigServiceImpl implements IGameConfigService{
    @Autowired
    private IGameConfigMapper gameConfigMapper;
    @Autowired
	private RedisClient redisClient;

    @Override
    public List<GameConfig> listAllGameConfig() {
        return gameConfigMapper.listAllGameConfig();
    }

    @Override
    public void updateGameConfig(GameConfig param) {
        GameConfig config = gameConfigMapper.findGameConfigById(param.getId());
        config.setGameId(param.getGameId());
        config.setKey(param.getKey());
        config.setValue(param.getValue());
        config.setRemark(param.getRemark());
        gameConfigMapper.updateGameConfig(config);
        RedisEventData redisEventData = new RedisEventData();
		redisEventData.setRedisEventData(config.getId());
		redisEventData.setRedisEventType(RedisEventData.RedisEventType.UpdateGameConfig.get());
		redisClient.publishGameServer(JSON.toJSONString(redisEventData));
    }
}
