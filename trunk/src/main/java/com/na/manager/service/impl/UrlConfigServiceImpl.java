package com.na.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.na.manager.dao.IUrlConfigMapper;
import com.na.manager.entity.UrlConfig;
import com.na.manager.service.IUrlConfigService;

/**
 * @author Andy
 * @version 创建时间：2017年9月14日 上午11:53:24
 */
@Service
@Transactional(propagation = Propagation.NESTED, noRollbackFor = Exception.class)
public class UrlConfigServiceImpl implements IUrlConfigService {
	@Autowired
	private IUrlConfigMapper urlConfigMapper;

	@Override
	@Transactional(readOnly = true)
	public List<UrlConfig> list() {
		return urlConfigMapper.list();
	}

	@Override
	public void update(UrlConfig urlConfig) {
		urlConfigMapper.update(urlConfig.getId(), urlConfig.getUrl().trim());
	}
}
