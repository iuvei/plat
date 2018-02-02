package com.na.manager.service;

import com.na.manager.entity.GameConfig;

import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
public interface IGameConfigService {

    List<GameConfig> listAllGameConfig();

    void updateGameConfig(GameConfig param);
}
