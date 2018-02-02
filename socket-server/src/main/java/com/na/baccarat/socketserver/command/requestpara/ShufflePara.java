package com.na.baccarat.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 洗牌请求参数
 * 
 * @author alan
 * @date 2017年4月29日 下午4:37:36
 */
public class ShufflePara extends CommandReqestPara {
	
	/**
     * 实体桌ID
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
