package com.na.roulette.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 清除路子参数
 * 
 * @author alan
 * @date 2017年5月12日 下午5:27:01
 */
public class ClearRoundPara extends CommandReqestPara {
	
    /**
     * 游戏桌ID
     */
    @JSONField(name = "tid")
    private Integer tableId;

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }
}
