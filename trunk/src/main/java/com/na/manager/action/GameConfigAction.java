package com.na.manager.action;

import com.google.common.base.Preconditions;
import com.na.manager.bean.NaResponse;
import com.na.manager.cache.AppCache;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.GameConfig;
import com.na.manager.service.IGameConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * v
 *
 * @create 2017-06
 */
@RestController
@RequestMapping("/gameConfigFunction")
@Auth("GameConfig")
public class GameConfigAction {

    @Autowired
    private IGameConfigService gameConfigService;

    @RequestMapping("/searchGameConfig")
    public NaResponse<Object> searchGameConfig(){
        Preconditions.checkNotNull(AppCache.getCurrentUser(),"login.first");
        return NaResponse.createSuccess(gameConfigService.listAllGameConfig());
    }
    @RequestMapping("/updateGameConfig")
    public NaResponse<Object> updateGameConfig(@RequestBody GameConfig param){
        Preconditions.checkNotNull(param.getGameId(),"param.erroe");
        Preconditions.checkNotNull(param.getId(),"param.erroe");
        Preconditions.checkNotNull(param.getKey(),"param.erroe");
        Preconditions.checkNotNull(param.getRemark(),"param.erroe");
        Preconditions.checkNotNull(param.getValue(),"param.erroe");
        gameConfigService.updateGameConfig(param);
        return NaResponse.createSuccess();
    }

}
