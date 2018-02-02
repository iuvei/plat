package com.na.manager.remote;

import java.util.List;

import com.na.manager.entity.UrlConfig;

/**
* @author Andy
* @version 创建时间：2017年9月14日 下午2:25:28
*/
public interface IConfigRemote {
	
	 List<UrlConfig> findAll();

}
