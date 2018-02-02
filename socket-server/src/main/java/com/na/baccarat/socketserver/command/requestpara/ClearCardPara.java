package com.na.baccarat.socketserver.command.requestpara;

import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 清除以扫牌请求参数
 * 
 * @author alan
 * @date 2017年4月29日 下午4:37:36
 */
public class ClearCardPara extends CommandReqestPara {

	/**
	 * 密码
	 */
    private String password;
	/**
	 * 索引
	 */
	private int index;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
