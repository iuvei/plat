package com.gameportal.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameportal.persistence.ProxyWebSitePvMapper;
import com.gameportal.service.IProxyWebSitePvService;

@Service("proxyWebSitePvService")
public class ProxyWebSitePvServiceImpl implements IProxyWebSitePvService {
	@Autowired
	private ProxyWebSitePvMapper proxyWebSitePvMapper;
	
	@Override
	public Long queryCount(Map<String, Object> map) {
		return proxyWebSitePvMapper.queryCount(map);
	}
}
