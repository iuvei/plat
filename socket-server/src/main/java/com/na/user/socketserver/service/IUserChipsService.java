package com.na.user.socketserver.service;

import java.util.List;

import com.na.baccarat.socketserver.entity.UserChips;


/**
 * 用户限红
 * 
 * @author alan
 * @date 2017年4月27日 下午3:42:02
 */
public interface IUserChipsService {
	
	/**
	 * 获取用户限红
	 * @param cid
	 * @return
	 */
	public List<UserChips> getUserChips(String cid);

}
