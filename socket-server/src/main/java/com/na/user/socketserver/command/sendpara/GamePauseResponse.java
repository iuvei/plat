package com.na.user.socketserver.command.sendpara;

/**
 * v
 * 游戏恢复暂停
 * @create 2017-07
 */
public class GamePauseResponse implements IResponse{
    private Integer tid;
    private Integer tableStatus;

    public Integer getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(Integer tableStatus) {
        this.tableStatus = tableStatus;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }
}
