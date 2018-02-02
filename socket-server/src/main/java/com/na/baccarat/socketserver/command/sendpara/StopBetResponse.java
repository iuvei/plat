package com.na.baccarat.socketserver.command.sendpara;

import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;

/**
 * 停止下注响应参数
 * 
 * @date 2017年5月23日 下午2:13:47
 */
public class StopBetResponse implements IResponse{
	
	
	@JSONField(name = "tableId")
	private Integer tableId;

	//前台动画
	private Map<String,Long> zhuYouGao;
	/**
     * 桌子展示状态
     */
    private String showTableStatus;
	
	
	public Map<String, Long> getZhuYouGao() {
		return zhuYouGao;
	}

	public void setZhuYouGao(Map<String, Long> zhuYouGao) {
		this.zhuYouGao = zhuYouGao;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public String getShowTableStatus() {
		return showTableStatus;
	}

	public void setShowTableStatus(String showTableStatus) {
		this.showTableStatus = showTableStatus;
	}
	
	
}
