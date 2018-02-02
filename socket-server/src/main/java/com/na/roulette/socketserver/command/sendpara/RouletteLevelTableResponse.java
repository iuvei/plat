package com.na.roulette.socketserver.command.sendpara;

/**
 *
 * Created by sunny on 2017/4/29 0029.
 */
public class RouletteLevelTableResponse{
    private Long loginId;
    private Integer tableId;
    private Integer userType;

    public Long getLoginId() {
        return loginId;
    }

    public void setLoginId(Long loginId) {
        this.loginId = loginId;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
