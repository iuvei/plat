package com.gameportal.web.agent.dao;

import java.util.List;
import java.util.Map;

import javax.sound.sampled.Port;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.gameportal.web.agent.model.ProxyApply;
import com.gameportal.web.agent.model.ProxyWebSitePv;
import com.gameportal.web.user.dao.BaseIbatisDAO;

@Repository
@SuppressWarnings("all")
public class ProxyWebSitePvDao extends BaseIbatisDAO{

	@Override
	public Class getEntityClass() {
		return ProxyWebSitePv.class;
	}
	
	public ProxyWebSitePv getProxyWebSitePv(Map<String, Object> map){
		List<ProxyWebSitePv> proxyWebSitePvs = getSqlMapClientTemplate().queryForList(getSelectQuery(),map);
		if(CollectionUtils.isNotEmpty(proxyWebSitePvs)){
			return proxyWebSitePvs.get(0);
		}
		return null;
	}
}
