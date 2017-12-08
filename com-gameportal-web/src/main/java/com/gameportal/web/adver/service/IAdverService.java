package com.gameportal.web.adver.service;

import java.util.List;
import java.util.Map;

import com.gameportal.web.adver.model.Adver;

public abstract interface IAdverService {
	/**
	 * 查询所有的广告数据。
	 * 
	 * @param paramMap
	 *            查询条件
	 * @return 返回符合条件的广告数据。
	 */
	List<Adver> queryAllAdver(Map<String, Object> values);
}
