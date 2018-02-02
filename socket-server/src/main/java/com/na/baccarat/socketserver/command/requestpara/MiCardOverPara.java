package com.na.baccarat.socketserver.command.requestpara;

import java.util.List;

import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 咪牌结束命令参数
 * @author Administrator
 *
 */
public class MiCardOverPara extends CommandReqestPara {
	
	/**
	 * 开牌的卡片index
	 */
	private List<Integer> index;
	
	
	public List<Integer> getIndex() {
		return index;
	}

	public void setIndex(List<Integer> index) {
		this.index = index;
	}
	
}
