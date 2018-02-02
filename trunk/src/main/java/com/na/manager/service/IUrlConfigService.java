package com.na.manager.service;

import java.util.List;

import com.na.manager.entity.UrlConfig;

/**
* @author Andy
* @version 创建时间：2017年9月14日 上午11:52:44
*/
public interface IUrlConfigService {
	
	List<UrlConfig> list();
	
	void update(UrlConfig urlConfig);
}
