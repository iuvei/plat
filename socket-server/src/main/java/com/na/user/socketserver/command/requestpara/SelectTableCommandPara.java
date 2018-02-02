package com.na.user.socketserver.command.requestpara;

import com.na.user.socketserver.command.requestpara.CommandReqestPara;

import java.util.List;

/**
 * Created by sunny on 2017/5/15 0015.
 */
public class SelectTableCommandPara extends CommandReqestPara {
    private List<Integer> gameTableIds;

    public List<Integer> getGameTableIds() {
        return gameTableIds;
    }

    public void setGameTableIds(List<Integer> gameTableIds) {
        this.gameTableIds = gameTableIds;
    }
}
