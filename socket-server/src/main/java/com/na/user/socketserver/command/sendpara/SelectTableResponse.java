package com.na.user.socketserver.command.sendpara;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.na.baccarat.socketserver.command.sendpara.GameTableJson;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.roulette.socketserver.entity.RouletteGameTable;

/**
 * Created by Administrator on 2017/5/15 0015.
 */
public class SelectTableResponse {
    List<GameTableJson> gameTableList;

    public List<GameTableJson> getGameTableList() {
        return gameTableList;
    }

    public void setGameTableList(List<GameTableJson> gameTableList) {
        this.gameTableList = gameTableList;
    }
    
    public void addGameTable(GameTable gameTable){
        if (gameTableList==null){
            gameTableList = new ArrayList<>();
        }
        if(gameTable!=null){
            gameTableList.add(new GameTableJson(gameTable));
        }
    }
    
    public void addGameTable(RouletteGameTable gameTable){
        if (gameTableList==null){
            gameTableList = new ArrayList<>();
        }
        if(gameTable!=null){
            gameTableList.add(new GameTableJson(gameTable));
        }
    }
    
    public void sort() {
    	Collections.sort(this.gameTableList, new Comparator<GameTableJson>() {
            public int compare(GameTableJson arg0, GameTableJson arg1) {
            	if(arg0.getGameId().compareTo(arg1.getGameId()) == 0) {
            		return arg0.getId().compareTo(arg1.getId());
            	} else {
            		return arg0.getGameId().compareTo(arg1.getGameId());
            	}
            }
        });
    }
    
}
