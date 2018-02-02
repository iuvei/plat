package com.na.user.socketserver.service;

import java.util.List;
import java.util.Map;

import com.na.user.socketserver.entity.GamePO;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
public interface IGameService {
    /**
     * 获取所有游戏。
     * @return
     */
    public List<GamePO> getAllGame();
    
    public Map<String,String> getConfigById(Integer id);
}
