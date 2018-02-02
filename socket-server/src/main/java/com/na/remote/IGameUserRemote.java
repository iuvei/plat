package com.na.remote;

/** 
* @author andy
* @date 2017年9月16日 上午11:30:25 
*/
public interface IGameUserRemote {
	
	boolean isOnline(Long userId);
	
	void logOut(Long userId);
	
	/**
	 * 锁定用户
	 * @param userId  用户ID
	 * @param flag	1表示锁定    2表示解锁
	 */
	void lockUser(Long userId, Integer flag);
}
