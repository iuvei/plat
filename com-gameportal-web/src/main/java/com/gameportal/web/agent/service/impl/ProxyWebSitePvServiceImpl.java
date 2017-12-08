package com.gameportal.web.agent.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.web.agent.dao.ProxyWebSitePvDao;
import com.gameportal.web.agent.model.ProxyWebSitePv;
import com.gameportal.web.agent.service.IProxyWebSitePvService;
/**
 * 代理域名访问业务类
 * @author Administrator
 *
 */
@Service("proxyWebSitePvService")
public class ProxyWebSitePvServiceImpl implements IProxyWebSitePvService{
	@Resource(name="proxyWebSitePvDao")
	private ProxyWebSitePvDao proxyWebSitePvDao;

	@Override
	public void save(ProxyWebSitePv entity) {
		proxyWebSitePvDao.save(entity);
	}

	@Override
	public void update(ProxyWebSitePv entity) {
		proxyWebSitePvDao.update(entity);
	}

	@Override
	public long count(Map<String, Object> map) {
		return proxyWebSitePvDao.getRecordCount(map);
	}

	@Override
	public ProxyWebSitePv getProxyWebSitePv(Map<String, Object> map) {
		return proxyWebSitePvDao.getProxyWebSitePv(map);
	}
}
