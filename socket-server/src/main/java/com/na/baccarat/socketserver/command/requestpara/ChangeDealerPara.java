package com.na.baccarat.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 更换荷官命令接受信息
 * @author Administrator
 *
 */
public class ChangeDealerPara extends CommandReqestPara {

	@JSONField(name = "bc")
	private String barcode;


	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

}
