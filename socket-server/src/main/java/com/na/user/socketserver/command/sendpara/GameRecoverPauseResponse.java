package com.na.user.socketserver.command.sendpara;

import com.na.roulette.socketserver.common.enums.RouletteGameTableInstantStateEnum;

/**
 * v
 * 游戏恢复暂停
 * @create 2017-07
 */
public class GameRecoverPauseResponse implements IResponse{
    private Integer tableStatus;
    private Integer tid;

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Integer getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(Integer tableStatus) {
        this.tableStatus = tableStatus;
    }
    
    public void setTableStatusEnum(RouletteGameTableInstantStateEnum tableStatus) {
        this.tableStatus = tableStatus.get();
    }
}
