package com.gameportal.web.agent.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.web.agent.dao.ProxyApplyDao;
import com.gameportal.web.agent.model.ProxyApply;
import com.gameportal.web.agent.service.IProxyApplyService;
/**
 * 代理业务处理类。
 * @author sum
 *
 */
@Service("proxyApplyServiceImpl")
public class ProxyApplyServiceImpl implements IProxyApplyService{
	@Resource(name="proxyApplyDao")
	private ProxyApplyDao proxyApplyDao;
	
	public ProxyApplyServiceImpl(){
		super();
	}

	@Override
	public ProxyApply saveWebProxy(ProxyApply proxy) {
		return (ProxyApply)proxyApplyDao.save(proxy);
	}

	@Override
	public long queryProxyApplyCount(Map<String, Object> paramMap) {
		return proxyApplyDao.getRecordCount(paramMap);
	}
}
