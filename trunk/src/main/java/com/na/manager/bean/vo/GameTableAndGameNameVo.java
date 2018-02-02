package com.na.manager.bean.vo;

import com.na.manager.common.annotation.I18NField;
import com.na.manager.entity.GameTable;

/**
 * @create 2017-06
 */
public class GameTableAndGameNameVo extends GameTable{
	
	@I18NField
    String gameName;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
