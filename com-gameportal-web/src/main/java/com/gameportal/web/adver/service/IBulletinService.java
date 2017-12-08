package com.gameportal.web.adver.service;

import java.util.List;
import java.util.Map;

import com.gameportal.web.adver.model.Bulletin;

public abstract interface IBulletinService {
	/**
	 * 查询所有的公告数据。
	 * @param paramMap 查询条件
	 * @return 返回符合条件的公告数据。
	 */
	List<Bulletin> queryAllBulletin(Map<String, Object> values);
	
	/**
	 * 添加公告
	 * @param bulletin
	 */
	void saveBulletin(Bulletin bulletin);
	
	/**
	 * 修改公告
	 * @param bulletin
	 */
	void updateBulletin(Bulletin bulletin);
}
