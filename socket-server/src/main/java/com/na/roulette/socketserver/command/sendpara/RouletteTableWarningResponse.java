package com.na.roulette.socketserver.command.sendpara;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.CommandResponse;

/**
 * 投注成功响应。
 * Created by sunny on 2017/5/3 0003.
 */
public class RouletteTableWarningResponse extends CommandResponse {
	
	@JSONField(name = "warningDesc")
    private String warningDesc;

	public String getWarningDesc() {
		return warningDesc;
	}

	public void setWarningDesc(String warningDesc) {
		this.warningDesc = warningDesc;
	}
    
    
    
}
