package com.na.baccarat.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;

/**
 * Created by Administrator on 2017/4/28 0028.
 */
public class ShuffleEndResponse implements IResponse {
	
	
    @JSONField(name = "cutCardSecond")
    private Integer cutCardSecond;
    
    /**
     * 桌子展示状态
     */
    private String showTableStatus;
    
    
	public Integer getCutCardSecond() {
		return cutCardSecond;
	}
	public void setCutCardSecond(Integer cutCardSecond) {
		this.cutCardSecond = cutCardSecond;
	}
	public String getShowTableStatus() {
		return showTableStatus;
	}
	public void setShowTableStatus(String showTableStatus) {
		this.showTableStatus = showTableStatus;
	}

}
