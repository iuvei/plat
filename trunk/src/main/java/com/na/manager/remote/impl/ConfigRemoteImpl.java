package com.na.manager.remote.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.na.light.LightRpcService;
import com.na.manager.entity.UrlConfig;
import com.na.manager.remote.IConfigRemote;
import com.na.manager.service.IUrlConfigService;

/**
 * 游戏相关配置
* @author Andy
* @version 创建时间：2017年9月14日 下午2:27:35
*/
@LightRpcService(value = "configRemote",group = "remote")
public class ConfigRemoteImpl implements IConfigRemote{
	
	@Autowired
	private IUrlConfigService urlConfigService;
	
	@Override
	public List<UrlConfig> findAll() {
		return urlConfigService.list();
	}

}
