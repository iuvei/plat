package com.gameportal.web.adver.service;

import java.util.List;
import java.util.Map;

import com.gameportal.web.adver.model.Bulletin;
import com.gameportal.web.adver.model.UserBulletin;

public abstract interface IUserBulletinService {
	/**
	 * 新增用户公告浏览记录。
	 * @param entity
	 */
	void save(UserBulletin entity);
	
	/**
	 * 更新用户公告浏览记录。
	 * @param entity
	 */
	void update(UserBulletin entity);

	
	/**
	 * 查询用户公告浏览记录。
	 * @param map
	 * @return
	 */
	List<UserBulletin> getAll(Map<String, Object> map);
	
	/**
	 * 判断是否已读。
	 * @param curBulletin
	 * @param bulletin
	 * @return
	 */
	boolean isRead(Bulletin curBulletin,UserBulletin bulletin);
}
