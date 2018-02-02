package com.na.manager.bean.vo;

import com.na.manager.entity.LiveUser;

/**
 * 账户管理搜索VO
 * 
 * @author alan
 * @date 2017年7月4日 下午12:03:34
 */
public class AccountVO extends LiveUser {
	
	/**
	 * 是否在线
	 */
	private boolean isOnline;

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	
	
	
}
