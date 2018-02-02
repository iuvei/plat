package com.na.user.socketserver.service;

import com.na.user.socketserver.entity.UserAnnounce;

import java.util.List;

/**
 * 游戏公告
 * @author Administrator
 *
 */
public interface IUserAnnounceService {

	/**
	 * 通过公告ID获取公告
	 * @param announceId
	 * @return
	 */
	UserAnnounce getUserAnnouceById(Long announceId);

	/**
	 * 获取用户相关的最新公告
	 * @param uid
	 * @return
	 */
	UserAnnounce getNewAnnounce();

	/**
	 * 查所有的公告
	 * @return
	 */
	List<UserAnnounce> getAllUserAnnouce();
}
