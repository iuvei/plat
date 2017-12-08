package com.gameportal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameportal.domain.Page;
import com.gameportal.domain.ProxyTransferLog;
import com.gameportal.persistence.ProxyTransferLogMapper;
import com.gameportal.service.IProxyTransferLogservice;

@Service("proxyTransferLogservice")
public class ProxyTransferLogserviceImpl implements IProxyTransferLogservice {
	@Autowired
	private ProxyTransferLogMapper proxyTransferLogMapper;

	@Override
	public int insertProxyTransferLog(ProxyTransferLog log) {
		return proxyTransferLogMapper.insertProxyTransferLog(log);
	}

	@Override
	public List<ProxyTransferLog> pageProxyTransferLog(Page page) {
		return proxyTransferLogMapper.findlistPageProxyTransferLog(page);
	}

}
